package com.example.examen_grupo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView picture;
    ImageButton openCamera;

    private static final int REQUEST_PERMISSION_CAMERA=101;
    private static final int REQUEST_IMAGE_CAMERA=101;

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picture = findViewById(R.id.picture);
        openCamera= findViewById(R.id.bfoto);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
                        goToCamera();
                    } else{
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA);
                    }
                }else{
                    goToCamera();
                }
            }
        });
    }

    // @Override
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode== REQUEST_PERMISSION_CAMERA){
            if(permissions.length> 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                goToCamera();
            }else{
                Toast.makeText(this,"Se requieren Permisos", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAMERA){
            if(resultCode== Activity.RESULT_OK){
//Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //       picture.setImageBitmap(bitmap);
                //       Log.i("TAG","Result=>" + bitmap);
                picture.setImageURI(Uri.parse(currentPhotoPath));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void goToCamera()
    {
        Intent cameraIntent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
        if (cameraIntent.resolveActivity((getPackageManager()))!=null){
            //startActivityForResult(cameraIntent,REQUEST_IMAGE_CAMERA);
            File photoFile = null;
            try {
                photoFile = createFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photoFile !=null){
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        "com.example.examen_grupo",
                        photoFile
                );
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
               startActivityForResult(cameraIntent,REQUEST_IMAGE_CAMERA);
            }
        }
    }

    private File createFile() throws IOException {
        String timeStamp= new SimpleDateFormat("yyyyMMdd_HH-mm-ss", Locale.getDefault()).format(new Date());
        String imgFileName= "IMG_" + timeStamp + "_";
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image= File.createTempFile(
                imgFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}