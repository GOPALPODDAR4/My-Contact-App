package com.example.mycontact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class profile extends AppCompatActivity {
TextView name,number;
ImageView photo,backBtn;
LinearLayout callLayout,messageLayout,whatsappLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        hidesystemUi();

    name =findViewById(R.id.profileName);
    number=findViewById(R.id.profileNumber );
    photo=findViewById(R.id.profileImage);
    backBtn=findViewById(R.id.backBtn);
    callLayout=findViewById(R.id.call_layout);
    messageLayout=findViewById(R.id.message_layout);
    whatsappLayout=findViewById(R.id.whatsapp_layout);

    String name2=getIntent().getStringExtra("name");
    String number2=getIntent().getStringExtra("number");
    Integer photo2=getIntent().getIntExtra("image",0);

    name.setText(name2);
    number.setText(number2);
    photo.setImageResource(photo2);

    // HERE I SET ON CLICK ON BACK BUTTON TO GO BACK IN MAIN ACTIVITY THROUGH GET ON PRESS METHOD
    backBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getOnBackPressedDispatcher();
            finish();
        }
    });

    // HERE I SET ON CLICK TO MAKE PHONE CALL TO THE PERSON WHEN I CLICKED ON THE LINEAR LAYOUT  INCLUDING CALL ICON & CALL TEXT
    callLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+number2));
            startActivity(intent);
        }
    });
    // HERE I SET ON CLICK TO REDIRECT TO THE DEFAULT MESSING APP TO MESSAGE TO PERSON WHEN I CLICKED ON THE LINEAR LAYOUT INCLUDING MESSAGE ICON & MESSAGE TEXT
    messageLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"+Uri.encode(number2)));
            startActivity(intent);
        }
    });

    //I SET ON CLICK TO REDIRECT TO WHATSAPPP CHAT TO THE PERSON WHEN I CLICKED ON THE LINEAR LAYOUT INCLUDING WHATSAPP ICON & WHATSAPP TEXT
    whatsappLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://whatsapp.com/send?phone="+number2));
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            startActivity(intent);
        }
    });
    }
    // THIS IS A USER DEFINE METHOD TO HIDE BOTTOM NAVIGATION BAR
    private void hidesystemUi(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        hidesystemUi();
    }
}