package com.example.goodphone.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.goodphone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dialog_Specifications extends Dialog {
    Context context;
    String idProduct;
    TextView tvScreenSize,tvScreenTechnology,tvScreenFeature,tvRearCamera,tvRearVideo,
            tvFrontCamera,tvFontVideo,tvChipset,tvRam,tvRom,tvPin,tvCharging,
            tvOperatingSystem,tvNetwork,tvWifi,tvBluetooth, tvGPS,tvWaterproof,tvSound;
    Button btn_Close;
    FirebaseFirestore dbProduct;
    public Dialog_Specifications(@NonNull Context context,String id_Product) {
        super(context);
        this.context = context;
        this.idProduct = id_Product;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tskthuat);
        init();
        getData();
        click();

    }
    public void click(){
        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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
                                tvScreenSize.setText(document.getString("ScreenSize"));
                                tvScreenTechnology.setText(document.getString("ScreenTechnology"));
                                tvScreenFeature.setText(document.getString("ScreenFeature"));
                                tvRearCamera.setText(document.getString("RearCamera"));
                                tvRearVideo.setText(document.getString("VideoRearCamera"));
                                tvFrontCamera.setText(document.getString("FrontCamera"));
                                tvFontVideo.setText(document.getString("VideoFrontCamera"));
                                tvRam.setText(document.getString("Ram"));
                                tvRom.setText(document.getString("Rom"));
                                tvPin.setText(document.getString("Pin"));
                                tvCharging.setText(document.getString("Charger"));
                                tvOperatingSystem.setText(document.getString("OperatingSystem"));
                                tvWifi.setText(document.getString("WifiProduct"));
                                tvBluetooth.setText(document.getString("Bluetooth"));
                                tvGPS.setText(document.getString("GPS"));
                                tvWaterproof.setText(document.getString("Waterproof"));
                                tvSound.setText(document.getString("AudioTechnology"));
                            } else {
                                Toast.makeText(context, "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Lỗi lấy dữ liệu",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void init(){
        tvScreenSize =findViewById(R.id.tv_Screen_Size_T);
        tvScreenTechnology = findViewById(R.id.tv_Screen_Technology_T);
        tvScreenFeature = findViewById(R.id.tv_Screen_Feature_T);
        tvRearCamera = findViewById(R.id.tv_Rear_Camera_T);
        tvRearVideo = findViewById(R.id.tv_Rear_Video_T);
        tvFrontCamera = findViewById(R.id.tv_Front_Camera_T);
        tvFontVideo = findViewById(R.id.tv_Front_Camera_T);
        tvRam = findViewById(R.id.tv_Ram_T);
        tvRom = findViewById(R.id.tv_Rom_T);
        tvPin = findViewById(R.id.tv_Pin_T);
        tvCharging = findViewById(R.id.tv_Charging_T);
        tvOperatingSystem = findViewById(R.id.tv_Operating_System_T);
        tvWifi = findViewById(R.id.tv_Wifi_T);
        tvBluetooth = findViewById(R.id.tv_Bluetooth_T);
        tvGPS = findViewById(R.id.tv_GPS_T);
        tvWaterproof = findViewById(R.id.tv_Waterproof_T);
        tvSound = findViewById(R.id.tv_Sound);
        tvNetwork = findViewById(R.id.tv_Network_T);
        tvChipset = findViewById(R.id.tv_Chipset);
        btn_Close= findViewById(R.id.btn_Close);
        dbProduct = FirebaseFirestore.getInstance();
    }
}
