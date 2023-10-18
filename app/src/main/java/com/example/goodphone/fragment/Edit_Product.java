package com.example.goodphone.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.goodphone.DetailProduct;
import com.example.goodphone.Home;
import com.example.goodphone.QLSanPhamActivity;
import com.example.goodphone.R;
import com.example.goodphone.adapter.SanPhamAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Edit_Product extends Fragment {
    View view;
    Uri imageUri;
    QLSanPhamActivity  qlSanPhamActivity;
    RecyclerView recyclerView;
    SanPhamAdapter sanPhamAdapter;
    Button btnConfirm, btnRefuse;
    TextView tvTitleConfirm,tvDetailConfirm;
    AlertDialog.Builder builder;
    Dialog dialog;

    Button btnEdit, btnExit;
    FirebaseFirestore dbProduct;
    FirebaseStorage storage;
    StorageReference storageReference,imageRef;
    StorageReference storageRef;
    DocumentReference documentReference;
    ImageButton btnEditImage;
    String idProduct;
    int price,quantity;
    int RESULT_OK = -1;
    private static final int PICK_IMAGE_REQUEST = 1;
    boolean isImageSelected = false;
    EditText edtScreenSize,edtScreenTechnology,edtScreenFeature,edtRearCamera,edtRearVideo,
            edtFrontCamera,edtFontVideo,edtChipset,edtRam,edtRom,edtPin,edtCharging,edtName,
            edtOperatingSystem,edtNetwork,edtWifi,edtBluetooth, edtGPS,edtWaterproof,edtSound,
            edtPrice,edtProductionCompanyProduct,edtQuantity;
    String Name, ProductionCompany,ScreenSize,ScreenTechnology,ScreenFeature,RearCamera,
            VideoRearCamera,FrontCamera,VideoFrontCamera,Chipset,Ram,Rom,Pin,Charger,OperatingSystem,NetworkSupport,
            WifiProduct,Bluetooth,GPS,Waterproof,AudioTechnology;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        init();
        button();
        getData();
        return view;
    }
    public void button(){
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                checkData();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(Edit_Product.this);
                transaction.commit();
            }
        });
        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        qlSanPhamActivity = (QLSanPhamActivity) getActivity();
        qlSanPhamActivity.reload();
    }
    public void setData(){
        if (!(edtPrice.getText().toString()).isEmpty()  ){
            if(!(edtQuantity.getText().toString()).isEmpty()){
                Name = edtName.getText().toString();
                price = Integer.parseInt(edtPrice.getText().toString());
                ProductionCompany = edtProductionCompanyProduct.getText().toString();
                quantity = Integer.parseInt(edtQuantity.getText().toString());
                ScreenSize = edtScreenSize.getText().toString();
                ScreenFeature = edtScreenFeature.getText().toString();
                ScreenTechnology = edtScreenTechnology.getText().toString();
                RearCamera = edtRearCamera.getText().toString();
                VideoRearCamera = edtRearCamera.getText().toString();
                FrontCamera = edtFrontCamera.getText().toString();
                VideoFrontCamera = edtFrontCamera.getText().toString();
                Chipset = edtChipset.getText().toString();
                Ram = edtRam.getText().toString();
                Rom = edtRom.getText().toString();
                Pin = edtPin.getText().toString();
                Charger = edtCharging.getText().toString();
                OperatingSystem = edtOperatingSystem.getText().toString();
                NetworkSupport = edtNetwork.getText().toString();
                WifiProduct = edtWifi.getText().toString();
                Bluetooth = edtBluetooth.getText().toString();
                GPS = edtGPS.getText().toString();
                Waterproof = edtWaterproof.getText().toString();
                AudioTechnology = edtSound.getText().toString();
            }else{
                Name = edtName.getText().toString();
                price = 1;
                ProductionCompany = edtProductionCompanyProduct.getText().toString();
                quantity = 0;
                ScreenSize = edtScreenSize.getText().toString();
                ScreenFeature = edtScreenFeature.getText().toString();
                ScreenTechnology = edtScreenTechnology.getText().toString();
                RearCamera = edtRearCamera.getText().toString();
                VideoRearCamera = edtRearCamera.getText().toString();
                FrontCamera = edtFrontCamera.getText().toString();
                VideoFrontCamera = edtFrontCamera.getText().toString();
                Chipset = edtChipset.getText().toString();
                Ram = edtRam.getText().toString();
                Rom = edtRom.getText().toString();
                Pin = edtPin.getText().toString();
                Charger = edtCharging.getText().toString();
                OperatingSystem = edtOperatingSystem.getText().toString();
                NetworkSupport = edtNetwork.getText().toString();
                WifiProduct = edtWifi.getText().toString();
                Bluetooth = edtBluetooth.getText().toString();
                GPS = edtGPS.getText().toString();
                Waterproof = edtWaterproof.getText().toString();
                AudioTechnology = edtSound.getText().toString();
            }
        }else {
            Name = edtName.getText().toString();
            price = 0;
            ProductionCompany = edtProductionCompanyProduct.getText().toString();
            quantity = 1;
            ScreenSize = edtScreenSize.getText().toString();
            ScreenFeature = edtScreenFeature.getText().toString();
            ScreenTechnology = edtScreenTechnology.getText().toString();
            RearCamera = edtRearCamera.getText().toString();
            VideoRearCamera = edtRearCamera.getText().toString();
            FrontCamera = edtFrontCamera.getText().toString();
            VideoFrontCamera = edtFrontCamera.getText().toString();
            Chipset = edtChipset.getText().toString();
            Ram = edtRam.getText().toString();
            Rom = edtRom.getText().toString();
            Pin = edtPin.getText().toString();
            Charger = edtCharging.getText().toString();
            OperatingSystem = edtOperatingSystem.getText().toString();
            NetworkSupport = edtNetwork.getText().toString();
            WifiProduct = edtWifi.getText().toString();
            Bluetooth = edtBluetooth.getText().toString();
            GPS = edtGPS.getText().toString();
            Waterproof = edtWaterproof.getText().toString();
            AudioTechnology = edtSound.getText().toString();
        }

    }
    public void getData(){
        dbProduct.collection("Product")
                .document(idProduct)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Double getPrice = document.getDouble("Price");
                                Double getQuantity = document.getDouble("Quantity");
                                price = getPrice != null ? getPrice.intValue() : 0;
                                quantity = getQuantity != null ? getQuantity.intValue() : 0;
                                String formattedPrice = String.valueOf(price);
                                String stQuantity = String.valueOf(quantity);
                                edtQuantity.setText(stQuantity);
                                edtPrice.setText(formattedPrice);
                                edtProductionCompanyProduct.setText(document.getString("ProductionCompany"));
                                edtName.setText(document.getString("Name"));
                                edtScreenSize.setText(document.getString("ScreenSize"));
                                edtScreenTechnology.setText(document.getString("ScreenTechnology"));
                                edtScreenFeature.setText(document.getString("ScreenFeature"));
                                edtRearCamera.setText(document.getString("RearCamera"));
                                edtRearVideo.setText(document.getString("VideoRearCamera"));
                                edtFrontCamera.setText(document.getString("FrontCamera"));
                                edtFontVideo.setText(document.getString("VideoFrontCamera"));
                                edtRam.setText(document.getString("Ram"));
                                edtRom.setText(document.getString("Rom"));
                                edtPin.setText(document.getString("Pin"));
                                edtCharging.setText(document.getString("Charger"));
                                edtOperatingSystem.setText(document.getString("OperatingSystem"));
                                edtWifi.setText(document.getString("WifiProduct"));
                                edtBluetooth.setText(document.getString("Bluetooth"));
                                edtGPS.setText(document.getString("GPS"));
                                edtWaterproof.setText(document.getString("Waterproof"));
                                edtSound.setText(document.getString("AudioTechnology"));
                                storage = FirebaseStorage.getInstance("gs://goodphone-687e7.appspot.com/");
                                storageRef = storage.getReference().child("Product");
                                imageRef = storageRef.child(idProduct +".jpg");

                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String fileUrl = uri.toString();
                                        Glide.with(getContext()).load(fileUrl).fitCenter().into(btnEditImage);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void checkData(){

        if (!Name.trim().isEmpty()) {
            if (!ProductionCompany.trim().isEmpty()) {
                if (!ScreenSize.trim().isEmpty()) {
                    if (!ScreenTechnology.trim().isEmpty()) {
                        if (!ScreenFeature.trim().isEmpty()) {
                            if (!RearCamera.trim().isEmpty()) {
                                if (!VideoRearCamera.trim().isEmpty()) {
                                    if (!FrontCamera.trim().isEmpty()) {
                                        if (!VideoFrontCamera.trim().isEmpty()) {
                                            if (!Chipset.trim().isEmpty()) {
                                                if (!Ram.trim().isEmpty()) {
                                                    if (!Rom.trim().isEmpty()) {
                                                        if (!Pin.trim().isEmpty()) {
                                                            if (!Charger.trim().isEmpty()) {
                                                                if (!OperatingSystem.trim().isEmpty()) {
                                                                    if (!NetworkSupport.trim().isEmpty()) {
                                                                        if (!WifiProduct.trim().isEmpty()) {
                                                                            if (!Bluetooth.trim().isEmpty()) {
                                                                                if (!GPS.trim().isEmpty()) {
                                                                                    if (!Waterproof.trim().isEmpty()) {
                                                                                        if (!AudioTechnology.trim().isEmpty()) {
                                                                                            if(price > 0){
                                                                                                if(quantity > 0){
                                                                                                    openDialogConfirm(Gravity.CENTER);
                                                                                                }else {
                                                                                                    Toast.makeText(getContext(), "Không được bỏ trống số lượng và lớn hơn 0", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }else{
                                                                                                Toast.makeText(getContext(), "Không được bỏ trống Tiền và lớn hơn 0", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        } else {
                                                                                            Toast.makeText(getContext(), "Không được bỏ trống công nghệ âm thanh", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    } else {
                                                                                        Toast.makeText(getContext(), "Không được bỏ trống chỉ số kháng nước, bụi", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                } else {
                                                                                    Toast.makeText(getContext(), "Không được bỏ trống GPS", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } else {
                                                                                Toast.makeText(getContext(), "Không được bỏ trống Bluetooth", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            Toast.makeText(getContext(), "Không được bỏ trống wifi", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(getContext(), "Không được bỏ trống hỗ trợ mạng", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(getContext(), "Không được bỏ trống hệ điều hành", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(getContext(), "Không được bỏ trống công nghệ xạc", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(getContext(), "Không được bỏ trống Pin", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(getContext(), "Không được bỏ trống bộ nhớ tron", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getContext(), "Không được bỏ trống dung lượng Ram", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Không được bỏ trống Chipset", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Không được bỏ trống quay video camera trước", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Không được bỏ trống camera trước", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Không được bỏ trống quay video camera sau", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Không được bỏ trống camera sau", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Không được bỏ trống tính năng màn hình", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Không được bỏ trống công nghệ màn hình", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không được bỏ trống kính thước màn hình", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Không được bỏ trống hãng sản phẩm", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Không được bỏ trống Tên", Toast.LENGTH_SHORT).show();
        }

    }
    public void editProduct(){
        Map<String,Object> data = new HashMap<>();
        data.put("Name", Name);
        data.put("Price", price);
        data.put("ProductionCompany", ProductionCompany);
        data.put("Sold",0);
        data.put("Quantity",quantity);
        data.put("SumRating", 0);
        data.put("ScreenSize",ScreenSize);
        data.put("ScreenTechnology", ScreenTechnology);
        data.put("ScreenFeature",ScreenFeature);
        data.put("RearCamera", RearCamera);
        data.put("VideoRearCamera",VideoRearCamera);
        data.put("FrontCamera",FrontCamera);
        data.put("VideoFrontCamera",VideoFrontCamera);
        data.put("Chipset",Chipset);
        data.put("Ram",Ram);
        data.put("Rom",Rom);
        data.put("Pin",Pin);
        data.put("Charger",Charger);
        data.put("OperatingSystem",OperatingSystem);
        data.put("NetworkSupport",NetworkSupport);
        data.put("WifiProduct", WifiProduct);
        data.put("Bluetooth",Bluetooth);
        data.put("GPS",GPS);
        data.put("Waterproof",Waterproof);
        data.put("AudioTechnology",AudioTechnology);
        documentReference= dbProduct.collection("Product").document(idProduct);

        documentReference
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(isImageSelected == true){
                            deleteImage();
                            uploadImageToFirebase(imageUri);
                        }

                        Toast.makeText(getContext(), "Chỉnh sủa th ành công",
                                Toast.LENGTH_SHORT).show();
                        qlSanPhamActivity = (QLSanPhamActivity) getActivity();
                        qlSanPhamActivity.reload();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Chỉnh sủa loi",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void openDialogConfirm(int gravity){
        dialog =  new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comfirm);
        Window window = dialog.getWindow();
        if(window == null ){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        tvDetailConfirm = dialog.findViewById(R.id.tv_Detail_Confirm);
        tvTitleConfirm = dialog.findViewById(R.id.tv_Title_Confirm);
        btnConfirm = dialog.findViewById(R.id.btn_Confirm);
        btnRefuse = dialog.findViewById(R.id.btn_Refuse);
        tvTitleConfirm.setText("Xác nhận chỉnh sửa");
        tvDetailConfirm.setText("Bạn có chắc là lưu thông tin điện thoại như vậy không ?");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                editProduct();
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
    public void init(){
        edtProductionCompanyProduct = view.findViewById(R.id.edt_Edit_Production_Company_Product);
        edtName = view.findViewById(R.id.edt_Edit_Name_Product);
        edtPrice = view.findViewById(R.id.edt_Edit_Price_Product);
        edtScreenSize = view.findViewById(R.id.edt_Edit_Screen_Size_Product);
        edtScreenTechnology = view.findViewById(R.id.edt_Edit_Screen_Technology_Product);
        edtScreenFeature = view.findViewById(R.id.edt_Edit_Screen_Feature_Product);
        edtRearCamera = view.findViewById(R.id.edt_Edit_Rear_Camera_Product);
        edtRearVideo = view.findViewById(R.id.edt_Edit_Video_Rear_Camera_Product);
        edtFrontCamera = view.findViewById(R.id.edt_Edit_Front_Camera_Product);
        edtFontVideo = view.findViewById(R.id.edt_Edit_Video_Front_Camera_Product);
        edtChipset = view.findViewById(R.id.edt_Edit_Chipset_Product);
        edtRam = view.findViewById(R.id.edt_Edit_Ram_Product);
        edtRom = view.findViewById(R.id.edt_Edit_Rom_Product);
        edtPin = view.findViewById(R.id.edt_Edit_Pin_Product);
        edtCharging = view.findViewById(R.id.edt_Edit_Charger_Product);
        edtOperatingSystem = view.findViewById(R.id.edt_Edit_Operating_System_Product);
        edtNetwork = view.findViewById(R.id.edt_Edit_Network_Support_Product);
        edtWifi = view.findViewById(R.id.edt_Edit_Wifi_Product);
        edtBluetooth = view.findViewById(R.id.edt_Edit_Bluetooth_Product);
        edtGPS = view.findViewById(R.id.edt_Edit_GPS_Product);
        edtWaterproof = view.findViewById(R.id.edt_Edit_Waterproof_Product);
        edtSound = view.findViewById(R.id.edt_Edit_Audio_Technology_Product);
        edtQuantity = view.findViewById(R.id.edt_Edit_Quantity_Product);

        btnExit = view.findViewById(R.id.btn_Edit_Close);
        btnEdit = view.findViewById(R.id.btn_Edit_Save);
        btnEditImage = view.findViewById(R.id.btn_Edit_Image_Product);

        dbProduct = FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();

        idProduct = getArguments().getString("id");

    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            isImageSelected = true;
            imageUri = data.getData();
            btnEditImage.setImageURI(imageUri);

        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = storage.getReference().child("Product/"+idProduct+".jpg");
        storageRef.putFile(imageUri);
    }
    public void deleteImage(){
        storageReference = storage.getReference().child("Product");
        imageRef= storageReference.child(idProduct +".jpg");
        imageRef.delete();
    }
}