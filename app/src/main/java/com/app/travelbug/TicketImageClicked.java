package com.app.travelbug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class TicketImageClicked extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_image_clicked);

        ImageView imageView = findViewById(R.id.ticketImage);
        TextView textView = findViewById(R.id.ticketName);

        Intent intent = getIntent();

        if(intent.getExtras() != null){
            String selectedName = intent.getStringExtra("name");
            String selectedImage = intent.getStringExtra("image");

            textView.setText(selectedName);
            imageView.setImageURI(Uri.parse(selectedImage));
        }
    }
}