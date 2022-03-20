package com.example.examen_grupo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityEditar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

     /*   // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(ActivityVerContactos.EXTRA_MESSAGE);
        String message2 = intent.getStringExtra(ActivityVerContactos.EXTRA_MESSAGE2);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.idTVPhone);
        TextView textView1 = findViewById(R.id.idTVName);
        textView.setText(message);
        textView1.setText(message2);*/
    }
    public void onClickLlamada(View v) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse(""));
        startActivity(i);
    }
}