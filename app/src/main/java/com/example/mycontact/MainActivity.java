package com.example.mycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    contactAdapter adapter;
    ArrayList<contactModel> data;
    contactDB contactDB;
    CardView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        hidesystemUi();

        recyclerView=findViewById(R.id.contactRecyclView);
        searchView=findViewById(R.id.searchView);
        addButton=findViewById(R.id.addContactBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //HERE I CALL readAllData() METHOD TO RETRIEVE DATA FROM SQLITE DATABASE & ADD TO ARRAY LIST

        fetch();
        sortContact();
        adapter=new contactAdapter(data,MainActivity.this);
        recyclerView.setAdapter(adapter);


        // THIS ADD BUTTON USE TO SHOW ADD NEW CONTACT ALERT DIALOG AND INVOKE addRecord() METHOD
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout=findViewById(R.id.addContactDailog);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_contact_layoout,null);

                EditText etName = view.findViewById(R.id.editTextName);
                EditText etNumber = view.findViewById(R.id.editTextNumber);
                TextView addNewContactBtn = view.findViewById(R.id.addNewContactBtn);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();

                //WHEN WE CLICK THIS addNewContactBtn addRecord() METHOD CALL
                addNewContactBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRecord(etName.getText().toString(),etNumber.getText().toString());
                        fetch();
                        alertDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Contact Added", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.show();
            }
        });


        //THIS IS A SEARCH VIEW TO SEARCH ITEM FROM RECYCLER VIEW
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    // THIS IS A METHOD TO ADD DATA INCLUDING CONTACT NAME & NUMBER TO THE SQLITE DATABASE
    public void addRecord(String name,String number){

        String result = new contactDB(MainActivity.this).addrecord(name,number);
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

    }


    // USER DEFINE METHOD TO SORT CONTACT ALPHABETICAL ORDER
    private void sortContact(){
        Collections.sort(data, new Comparator<contactModel>() {
            @Override
            public int compare(contactModel o1, contactModel o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
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
   public void fetch(){
       data=new contactDB(MainActivity.this).readAllData();
   }

}