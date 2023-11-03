package com.example.goodphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodphone.adapter.Cart_Adapter;
import com.example.goodphone.adapter.Payment_Adapter;
import com.example.goodphone.dialog.Dialog_Other;
import com.example.goodphone.dialog.Dialog_Successful_Payment;
import com.example.goodphone.model.List_Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.DateTime;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formattable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Payment extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView btnBack;
    Button btnOder;
    FirebaseFirestore DBUser,DBProduct;
    StorageReference storageRef, imgRef;
    FirebaseStorage img;
    TextView tvCity,tvSumPriceProduct,tvShippingFee,tvSumPrice,tvSumPricePayment;
    EditText edtName,edtPhoneNumber,edtDistrict,edtWards ,edtHouseNumber,edtNote;
    Button btnConfirm, btnRefuse;
    TextView tvTitleConfirm,tvDetailConfirm;
    Dialog dialog;
    Calendar calendar;
    Date date= new Date();
    RecyclerView recyclerView;
    ArrayList<String> listId ;
    List<List_Product> arrProduct;
    String idProduct,idUser, name,url;
    Double getQuantityDB, getPriceDB;
    Payment_Adapter paymentAdapter;
    int quantity, price,sumPrice;
    boolean phoneValidate;
    String firstName, lastName, phoneNumber, city,district,wards, addressNumber,nameU,phoneNumberN,districtN,wardsN, addressNumberN,nameUN, PHONE_NUMBER_PATTERN,dateTime,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
        button();
        checkUser();

    }
    public void button(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }
    public void checkUser(){
        if (user != null){
            idUser = user.getUid();
            idProduct = getIntent().getStringExtra("id");
            quantity = getIntent().getIntExtra("quantity",0);
            if (idProduct != null && quantity != 0 ){
                getProductDB(idProduct,quantity);
            }else {
                getPutExtra();
            }
            getUserDB();
            getAddressUser();
        }else {
            idProduct = getIntent().getStringExtra("id");
            quantity = getIntent().getIntExtra("quantity",0);
            getProductDB(idProduct,quantity);
        }
    }
    public void setDataUser(){
        edtName.setText(lastName +" " + firstName);
        edtPhoneNumber.setText(phoneNumber);
    }
    public void setDataAddress (){
        tvCity.setText("TP. Hồ Chí Minh");
        edtDistrict.setText(district);
        edtWards.setText(wards);
        edtHouseNumber.setText(addressNumber);
    }
    public void getData(){
        PHONE_NUMBER_PATTERN = "^(\\+?84|0)(1[2689]|3[2-9]|5[2689]|7[06789]|8[0-9])(\\d{7})$";
        nameUN = edtName.getText().toString().trim();
        phoneNumberN = edtPhoneNumber.getText().toString().trim();
        districtN = edtDistrict.getText().toString().trim();
        wardsN = edtWards.getText().toString().trim();
        addressNumberN = edtHouseNumber.getText().toString().trim();
        phoneValidate = validatePhoneNumber(phoneNumberN.trim(),PHONE_NUMBER_PATTERN);
        if (phoneValidate){
            checkData();

        }else {
            Toast.makeText(Payment.this, "Kiểm tra lại số điện thoại",Toast.LENGTH_SHORT).show();
        }

    }
    public void checkData(){

        if (nameUN.isEmpty() || districtN.isEmpty() || wardsN.isEmpty() || addressNumberN.isEmpty()) {
            Toast.makeText(Payment.this, "Nhập thiếu thông tin",Toast.LENGTH_SHORT).show();
        } else {
            if (nameUN.length() > 100 || districtN.length() > 100 || wardsN.length() > 100| addressNumberN.length() > 100){
                Toast.makeText(Payment.this, "Không nhập quá 100 kí tự",Toast.LENGTH_SHORT).show();

            }else {
                openDialog();
            }
        }
    }
    public void openDialog(){
        dialog =  new Dialog(Payment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comfirm);
        Window window = dialog.getWindow();
        if(window == null ){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        tvDetailConfirm = dialog.findViewById(R.id.tv_Detail_Confirm);
        tvTitleConfirm = dialog.findViewById(R.id.tv_Title_Confirm);
        btnConfirm = dialog.findViewById(R.id.btn_Confirm);
        btnRefuse = dialog.findViewById(R.id.btn_Refuse);
        tvTitleConfirm.setText("Xác nhận đặt hàng");
        if (user != null ){
            tvDetailConfirm.setText("Bạn có chắc là muốn đặt hàng ? \nXin hãy kiểm tra lại trước khi đặt hàng! ");
        }else {
            tvDetailConfirm.setText("Bạn có chắc là muốn đặt hàng ? " +
                    "\nXin hãy kiểm tra lại trước khi đặt hàng! " +
                    "\nQuý khách đang đặt hàng bằng cách không đăng nhập vui lòng ghi nhớ và kiểm tra số điện thoại thật kĩ trước khi đặt hàng !");
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null){
                    addOtherUser();
                }else {
                    addOtherNoUser();
                }
                dialog.dismiss();
            }
        });
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void addOtherUser(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        dateTime = "Ngày: " + day + "/" + month + "/" + year  +" " +"Thời gian: " + hour + ":" + minute + ":" + second;
        address = addressNumberN +" /"+wardsN +" / "+districtN+" / "+city;
        Map<String,Object> data = new HashMap<>();
        data.put("bookingDate",dateTime);
        data.put("Address",address);
        data.put("sumPrice",sumPrice);
        data.put("status", "Chờ xác nhận");
        DBUser.collection("User")
                .document(idUser)
                .collection("Other")
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        DocumentReference documentReference = task.getResult();
                        String id = documentReference.getId();
                        for(List_Product listProduct : arrProduct){
                            Map<String,Object> dataDetailOther = new HashMap<>();
                            idProduct = listProduct.getId();
                            dataDetailOther.put("idP",idProduct);
                            dataDetailOther.put("quantity",listProduct.getQuantity());
                            dataDetailOther.put("price", listProduct.getPrice());
                            Log.e("",listProduct.getId());
                            detailOtherUser(id, dataDetailOther,idProduct);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void detailOtherUser(String id, Map<String,Object> dataDetailOther, String idProduct){
        DBUser.collection("User")
                .document(idUser)
                .collection("Other")
                .document(id)
                .collection("OtherDetail")
                .add(dataDetailOther);
        deleteCart(id,idProduct);
    }
    public void deleteCart(String id,String idProduct){
        DBUser.collection("User")
                .document(idUser)
                .collection("Cart")
                .document(idProduct)
                .delete();
        openDialogSuccessful(id);

    }
    public void openDialogSuccessful(String id){
        final Dialog_Successful_Payment dialogSuccessfulPayment = new Dialog_Successful_Payment(Payment.this, id,address,nameUN,dateTime,sumPrice,phoneNumber);
        dialogSuccessfulPayment.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(Payment.this, android.R.color.transparent)));
        dialogSuccessfulPayment.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogSuccessfulPayment.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogSuccessfulPayment.show();
        dialogSuccessfulPayment.getWindow().setAttributes(lp);

    }
    public void addOtherNoUser(){
        city = tvCity.getText().toString();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        dateTime = "Ngày: " + day + "/" + month + "/" + year  +" " +"Giờ: " + hour + ":" + minute + ":" + second;
        address = addressNumberN +" /"+wardsN +" / "+districtN+" / "+city;
        Map<String,Object> data = new HashMap<>();
        data.put("Name", nameUN);
        data.put("phoneNumber", phoneNumberN);
        data.put("bookingDate",dateTime);
        data.put("Address",address);
        data.put("sumPrice",sumPrice );
        DBUser.collection("Other")

                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        DocumentReference documentReference = task.getResult();
                        String id = documentReference.getId();
                        Map<String,Object> dataDetailOther = new HashMap<>();
                        dataDetailOther.put("idP",idProduct);
                        dataDetailOther.put("quantity",quantity);
                        dataDetailOther.put("idOther",id);
                        dataDetailOther.put("price", price);
                        detailOtherNoUser(id, dataDetailOther);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void detailOtherNoUser(String id, Map<String,Object> dataDetailOther){
        DBUser.collection("Other")
                .document(id)
                .collection("Detail")
                .add(dataDetailOther);
    }
    public static boolean validatePhoneNumber(String input, String phoneNumberPattern) {
        Pattern pattern = Pattern.compile(phoneNumberPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public void getUserDB(){
        DBUser.collection("User")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        firstName = document.getString("firstName");
                        lastName = document.getString("lastName");
                        phoneNumber = document.getString("phoneNumber");
                        setDataUser();
                    }
                });
    }
    public void getAddressUser(){
        DBUser.collection("User")
                .document(idUser)
                .collection("Address")
                .document("Home")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            addressNumber= document.getString("addressNumber");
                            district = document.getString("district");
                            wards = document.getString("wards");
                            setDataAddress();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void sumPrice(int price, int quantity){
        sumPrice += price * quantity;
        setPrice(sumPrice);
    }
    public void setPrice(int sumPrice){
        sumPrice += 30000;
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        String formatSumPriceProduct = currencyFormat.format(sumPrice);
        String formatShippingFee = currencyFormat.format(30000);
        String formatSumPrice = currencyFormat.format(sumPrice );
        tvSumPricePayment.setText(formatSumPrice);
        tvShippingFee.setText(formatShippingFee);
        tvSumPriceProduct.setText(formatSumPriceProduct);
        tvSumPrice.setText(formatSumPrice);
    }
    public void getPutExtra(){
        listId = getIntent().getStringArrayListExtra("listId");
        for(String data :  listId){
            idProduct = data;
            getCartDB(idProduct);
        }
    }
    public void getCartDB(String idProduct){
        DBUser.collection("User")
                .document(idUser)
                .collection("Cart")
                .document(idProduct)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            getQuantityDB = document.getDouble("quantity");
                            quantity = getQuantityDB != null ? getQuantityDB.intValue() : 0;
                            getProductDB(idProduct,quantity);
                        }
                    }
                });
    }
    public void getProductDB(String id, int quantity){
        DBProduct.collection("Product")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            name = document.getString("Name");
                            getPriceDB = document.getDouble("Price");
                            price = getPriceDB != null ? getPriceDB.intValue() : 0;
                            getImg(id,quantity,price,name);
                            sumPrice(price, quantity);
                        }
                    }
                });
    }
    public void getImg(String idProduct,int quantity, int price,String name){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        imgRef = storageRef.child("Product/"+idProduct +".jpg");
        imgRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url = uri.toString();
                        arrProduct.add(new List_Product(idProduct, quantity,price,name,url));
                        paymentAdapter= new Payment_Adapter(Payment.this, arrProduct);
                        recyclerView.setAdapter(paymentAdapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(Payment.this, "anh lỗi",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void init(){
        DBUser = FirebaseFirestore.getInstance();
        DBProduct = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        img = FirebaseStorage.getInstance();
        storageRef = img.getReference();
        arrProduct = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv_Payment);
        edtPhoneNumber = findViewById(R.id.edt_PhoneNumber_Payment);
        edtName = findViewById(R.id.edt_Name_Payment);
        edtDistrict = findViewById(R.id.edt_District_Payment);
        edtWards = findViewById(R.id.edt_Wards_Payment);
        edtHouseNumber = findViewById(R.id.edt_HouseNumber_Payment);
        edtNote = findViewById(R.id.edt_NoteAddress_Payment);
        tvCity = findViewById(R.id.tv_City_Payment);
        tvSumPriceProduct = findViewById(R.id.tv_SumPrice_Product_Payment);
        tvShippingFee = findViewById(R.id.tv_ShippingFee_Payment);
        tvSumPrice = findViewById(R.id.tvSumPrice_Payment);
        tvSumPricePayment = findViewById(R.id.pricePayment);
        btnBack = findViewById(R.id.ic_back);
        btnOder = findViewById(R.id.btn_Oder_Payment);
        calendar =Calendar.getInstance();

    }
}