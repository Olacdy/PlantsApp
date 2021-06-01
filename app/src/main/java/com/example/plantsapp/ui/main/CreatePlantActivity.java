package com.example.plantsapp.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plantsapp.DBHelper;
import com.example.plantsapp.Plant;
import com.example.plantsapp.R;

import java.io.IOException;

public class CreatePlantActivity extends AppCompatActivity {

    Button get_image_button, create_plant_button;
    EditText plantName, wateringFrequency;
    Bitmap plantImage;
    DBHelper plants_db;
    Plant new_plant;
    public final static int PICK_PHOTO_CODE = 1046;
    public final static int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 9834;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plant);
        get_image_button = findViewById(R.id.add_image_button);
        create_plant_button = findViewById(R.id.save_plant_button);
        plantName = findViewById(R.id.editTextPlantName);
        wateringFrequency = findViewById(R.id.editTextWateringFrequency);
        get_image_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreatePlantActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    requestStoragePermission();
                }
            }
        });
        create_plant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(TextUtils.isEmpty(plantName.getText().toString())) {
                        plantName.setError("No plant name!");
                        return;
                    }
                    if (plantImage == null) {
                        if (TextUtils.isEmpty(wateringFrequency.getText().toString()))
                            new_plant = new Plant(plantName.getText().toString(), BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.plant), 1);
                        else {
                            new_plant = new Plant(plantName.getText().toString(), BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.plant), Integer.parseInt(wateringFrequency.getText().toString()));
                        }
                    } else {
                        if (TextUtils.isEmpty(wateringFrequency.getText().toString()))
                            new_plant = new Plant(plantName.getText().toString(), plantImage, 1);
                        else {
                            new_plant = new Plant(plantName.getText().toString(), plantImage, Integer.parseInt(wateringFrequency.getText().toString()));
                        }
                    }
                    MainActivity.plants.add(new_plant);
                    MainActivity.update();
                    Toast.makeText(getApplicationContext(), "Plant is added successfully.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(getApplicationContext(), "Plant is not added.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            if(Build.VERSION.SDK_INT > 27){
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            plantImage = loadFromUri(photoUri);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    public void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }
}