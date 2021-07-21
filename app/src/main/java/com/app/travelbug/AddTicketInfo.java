package com.app.travelbug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.travelbug.data.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddTicketInfo extends AppCompatActivity {


    ImageView imageView;
    TextView textView;
    Button addImageButton;
    Button save;
    EditText editText;
    DatabaseHelper db;
    Bitmap bitmap;
    String image;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket_info);

        db = new DatabaseHelper(this);

        imageView = findViewById(R.id.ticketImage);
        editText = findViewById(R.id.ticketName);
        textView = findViewById(R.id.ticketName);
        addImageButton = findViewById(R.id.addImageButton);
        save = findViewById(R.id.save);

        addImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);




                /*String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Download/"+editText.getText().toString()+".jpeg";
                Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                byte[] bytesImage = byteArrayOutputStream.toByteArray();
                db.addTicket(stringFilePath, bytesImage);*/

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null){
                    Toast.makeText(AddTicketInfo.this, "Please select an image", Toast.LENGTH_SHORT).show();
                } else if (editText.getText() == null) {
                    Toast.makeText(AddTicketInfo.this, "Please enter a title for your image", Toast.LENGTH_SHORT).show();
                } else {
                    db.addTicket(editText.getText().toString(), image);
                    Intent intent = new Intent(AddTicketInfo.this, TicketInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();



            /*Bitmap image = BitmapFactory.decodeFile(data.getData().toString().ge);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(image);
            imageView.setImageDrawable(bitmapDrawable);*/

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                //bitmap.compress(Bitmap.CompressFormat.PNG, 90, bitmap)
                //imageView.setImageBitmap(bitmap);
                image = data.getData().toString();
                imageView.setImageURI(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //imageView.setImageBitmap(BitmapFactory.decodeFile(data.getData().toString()));
        }
    }

}