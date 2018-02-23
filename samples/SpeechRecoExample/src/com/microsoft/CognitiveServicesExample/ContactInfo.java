package com.microsoft.CognitiveServicesExample;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactInfo extends AppCompatActivity {

    EditText et1, et2;
    Button b;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        b=(Button)findViewById(R.id.button);
        db=openOrCreateDatabase("mydb",MODE_PRIVATE,null);
        if(db!=null)
        {

            db.execSQL("create table if not exists emergency_contacts(NAME varchar(50), NUMBER varchar(50))");

        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.name[MainActivity.k]=et1.getText()+"";
                //MainActivity.number[MainActivity.k++]=et2.getText()+"";
                String name=et1.getText()+"";
                String number=et2.getText()+"";
                if(name.length()<=0 || number.length()<=0)
                {
                    Toast.makeText(ContactInfo.this,"Fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String s="insert into emergency_contacts values('"+name+"','"+number+"');";
                    db.execSQL(s);
                }
                Intent i=new Intent(ContactInfo.this,EmergencyContacts.class);
                startActivity(i);
                finish();
            }
        });

    }
}
