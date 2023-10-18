package com.example.goodphone.fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.goodphone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Edit_Product extends Fragment {
    View view;
    Button btnEdit, btnExit;
    FirebaseFirestore dbProduct;
    FirebaseStorage storage;
    ImageButton editImage;
    public String idProduct;
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
        return view;
    }
    public void button(){

    }
    public void init(){
        edtProductionCompanyProduct = view.findViewById(R.id.edt_Add_Production_Company_Product);
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
        btnExit = view.findViewById(R.id.btn_Edit_Close);
        btnEdit = view.findViewById(R.id.btn_Edit_Save);
        dbProduct = FirebaseFirestore.getInstance();
        storage= FirebaseStorage.getInstance();
        editImage = view.findViewById(R.id.btn_Edit_Image_Product);
        edtQuantity = view.findViewById(R.id.edt_Edit_Quantity_Product);

    }
    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

}