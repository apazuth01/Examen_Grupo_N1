package com.example.examen_grupo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ImageView picture;
    ImageButton openCamera, btngaleria;
    ImageView imageView;
    Button  btnsalvar;
    TextView textview1;
    EditText tnombre, ttelefono,tlatitud,tlongitud,tv;
    Bitmap photo;


    static final int RESULT_GALLERY_IMG = 200;
    private static final int REQUEST_PERMISSION_CAMERA=101;
    private static final int REQUEST_IMAGE_CAMERA=101;

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picture = (ImageView) findViewById(R.id.picture);
        openCamera= (ImageButton) findViewById(R.id.bfoto);
        btngaleria= (ImageButton) findViewById(R.id.btnGaleria);
        btnsalvar= (Button) findViewById(R.id.btnSalvar);
        tnombre = (EditText) findViewById(R.id.txtNombre);
        ttelefono =(EditText) findViewById(R.id.txtTelefono);
        tlatitud = (EditText)findViewById(R.id.txtLatitud);
        tlongitud =(EditText) findViewById(R.id.txtlongitud);
        tv =(EditText) findViewById(R.id.Tv);


        btnsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String imageBase64 = GetStringImage( photo);
                //textview1.setText(imageBase64);
                try {
                    String resu = "";
                    //comprobamos que se ha seleccionado alguna de las operaciones a realizar

                    if (tv.getText().toString().isEmpty()) {
                        resu = "Seleccione una Imagen de Contacto";
                        Toast.makeText(getApplicationContext(), resu, Toast.LENGTH_LONG).show();
                    } else {
                        if (tnombre.getText().toString().isEmpty()) {
                            resu = "Ingrese el Nombre del Contacto";
                            Toast.makeText(getApplicationContext(), resu, Toast.LENGTH_LONG).show();
                        } else {
                            if (ttelefono.getText().toString().isEmpty()) {
                                resu = "Ingrese el Numero de Telefono del Contacto";
                                Toast.makeText(getApplicationContext(), resu, Toast.LENGTH_LONG).show();
                            } else {
                                if(tlatitud.getText().toString().isEmpty()){
                                    resu = "Ingrese La Latitud";
                                    Toast.makeText(getApplicationContext(), resu, Toast.LENGTH_LONG).show();
                                }else {
                                if (tlongitud.getText().toString().isEmpty()) {
                                    resu = "Ingrese la Longitud!";
                                    Toast.makeText(getApplicationContext(), resu, Toast.LENGTH_LONG).show();
                                } else {
                                    SendImage();
                                }
                            }
                        }
                    }
                    }
                } catch (Exception e) {
                    //en caso de error se muestra la exception
                    System.out.println("Error!! Exception: " + e);
                }

            }
        });

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

        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {  GaleriaImagenes(); }
        });
    }
    private void ClearScreen() {
        tnombre.setText("");
        ttelefono.setText("");
        tlatitud.setText("");
        tlongitud.setText("");
        //currentPhotoPath.isEmpty();
        //photo.toString().isEmpty();
        tv.setText("");

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
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQUEST_IMAGE_CAMERA){
            if(resultCode== Activity.RESULT_OK ){
               // Bitmap bitmap = (Bitmap) data.getExtras().get(currentPhotoPath);
                //       picture.setImageBitmap(bitmap);
                //       Log.i("TAG","Result=>" + bitmap);

                picture.setImageURI(Uri.parse(currentPhotoPath));

              //  photo= bitmap;
               // Bitmap image= BitmapFactory.decodeResource(context.getResources(), R.drawable.image_name);
                tv.setText("1");
        }
        }else{


        if(resultCode == RESULT_OK && requestCode == RESULT_GALLERY_IMG)
            tv.setText("1");
        {
            try
            {
                Uri imageUri;
                imageUri = data.getData();
                picture.setImageURI(imageUri);
                tv.setText("1");
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                photo = rotateImageIfRequired(photo, imageUri);


            }
            catch (Exception ex)
            {

            }
        }}
        }



    private void GaleriaImagenes()
    {
        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentGaleria, RESULT_GALLERY_IMG );
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
                Uri photoUri = FileProvider.getUriForFile( this,"com.example.examen_grupo",  photoFile );
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
               startActivityForResult(cameraIntent,REQUEST_IMAGE_CAMERA);

            }
        }

    }

    private File createFile() throws IOException {
        String timeStamp= new SimpleDateFormat("yyyyMMdd_HH-mm-ss", Locale.getDefault()).format(new Date());
        String imgFileName= "IMG_" + timeStamp + "_";
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image= File.createTempFile(imgFileName,".jpg", storageDir );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void SendImage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApiMethods.EndPointImageUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)   {
                String resu;
                Log.d("Respuesta ", response.toString());
                resu = "Registro Ingresado Exitosamente";
                Toast.makeText(getApplicationContext(), resu, Toast.LENGTH_LONG).show();
                ClearScreen();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Respuesta ", error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams () throws AuthFailureError
            {
                String image = GetStringImage(photo);
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("nombre",tnombre.getText().toString());
                parametros.put("telefono",ttelefono.getText().toString());
                parametros.put("latitud",tlatitud.getText().toString());
                parametros.put("longitud",tlongitud.getText().toString());
                parametros.put("imagen", image);
                return parametros;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private String GetStringImage(Bitmap photo)
    {
        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, ba);
            byte[] imagebyte = ba.toByteArray();
            String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);
            return encode;
        }
        catch (Exception ex)
        { ex.toString(); }

        return "";

    }
    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public void clickNew(View view)
    {
        Intent intent = new Intent(this,ActivityVerContactos.class);
        startActivity(intent);

    }

}