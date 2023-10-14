package com.example.goodphone.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goodphone.QLSanPhamActivity;
import com.example.goodphone.R;
import com.example.goodphone.Register;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Add_Product extends Fragment {
    private View view;
    QLSanPhamActivity qlSanPhamActivity;
    FirebaseFirestore dbProduct;
    Uri imageUri;
    FirebaseStorage storage;
    Button btnAdd, btnExit;
    int RESULT_OK = -1;
    ImageButton btnAddImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edtScreenSize,edtScreenTechnology,edtScreenFeature,edtRearCamera,edtRearVideo,
            edtFrontCamera,edtFontVideo,edtChipset,edtRam,edtRom,edtPin,edtCharging,edtName,
            edtOperatingSystem,edtNetwork,edtWifi,edtBluetooth, edtGPS,edtWaterproof,edtSound,
            edtPrice,edtProductionCompanyProduct,edtQuantity;
    String Name, ProductionCompany,ScreenSize,ScreenTechnology,ScreenFeature,RearCamera,
            VideoRearCamera,FrontCamera,VideoFrontCamera,Chipset,Ram,Rom,Pin,Charger,OperatingSystem,NetworkSupport,
            WifiProduct,Bluetooth,GPS,Waterproof,AudioTechnology;
    int Price,Quantity;
    public Add_Product(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add__product, container, false);
        init();
        button();
        //addProduct();
        return view;
    }
    public void button(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(Add_Product.this);
                transaction.commit();
            }
        });
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    public void addProduct(){
        Name = edtName.getText().toString();
        Price = Integer.parseInt(edtPrice.getText().toString());
        ProductionCompany = edtProductionCompanyProduct.getText().toString();
        Quantity = Integer.parseInt(edtQuantity.getText().toString());
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
        Map<String,Object> data = new HashMap<>();
        data.put("Name", Name);
        data.put("Price", Price);
        data.put("ProductionCompany", ProductionCompany);
        data.put("Sold",0);
        data.put("Quantity",Quantity);
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
        dbProduct.collection("Product")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        uploadImageToFirebase(imageUri);
                        Toast.makeText(getContext(), "them thanh cong",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            btnAddImage.setImageURI(imageUri);
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = storage.getReference().child("Product/"+Name+".jpg");
        storageRef.putFile(imageUri);
    }

    public void init(){
        edtProductionCompanyProduct = view.findViewById(R.id.edt_Add_Production_Company_Product);
        edtName = view.findViewById(R.id.edt_Add_Name_Product);
        edtPrice = view.findViewById(R.id.edt_Add_Price_Product);
        edtScreenSize = view.findViewById(R.id.edt_Add_Screen_Size_Product);
        edtScreenTechnology = view.findViewById(R.id.edt_Add_Screen_Technology_Product);
        edtScreenFeature = view.findViewById(R.id.edt_Add_Screen_Feature_Product);
        edtRearCamera = view.findViewById(R.id.edt_Add_Rear_Camera_Product);
        edtRearVideo = view.findViewById(R.id.edt_Add_Video_Rear_Camera_Product);
        edtFrontCamera = view.findViewById(R.id.edt_Add_Front_Camera_Product);
        edtFontVideo = view.findViewById(R.id.edt_Add_Video_Front_Camera_Product);
        edtChipset = view.findViewById(R.id.edt_Add_Chipset_Product);
        edtRam = view.findViewById(R.id.edt_Add_Ram_Product);
        edtRom = view.findViewById(R.id.edt_Add_Rom_Product);
        edtPin = view.findViewById(R.id.edt_Add_Pin_Product);
        edtCharging = view.findViewById(R.id.edt_Add_Charger_Product);
        edtOperatingSystem = view.findViewById(R.id.edt_Add_Operating_System_Product);
        edtNetwork = view.findViewById(R.id.edt_Add_Network_Support_Product);
        edtWifi = view.findViewById(R.id.edt_Add_Wifi_Product);
        edtBluetooth = view.findViewById(R.id.edt_Add_Bluetooth_Product);
        edtGPS = view.findViewById(R.id.edt_Add_GPS_Product);
        edtWaterproof = view.findViewById(R.id.edt_Add_Waterproof_Product);
        edtSound = view.findViewById(R.id.edt_Add_Audio_Technology_Product);
        btnExit = view.findViewById(R.id.btn_Close);
        btnAdd = view.findViewById(R.id.btn_Save);
        dbProduct = FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();
        btnAddImage = view.findViewById(R.id.btn_Add_Image_Product);
        edtQuantity = view.findViewById(R.id.edt_Add_Quantity_Product);
    }
}