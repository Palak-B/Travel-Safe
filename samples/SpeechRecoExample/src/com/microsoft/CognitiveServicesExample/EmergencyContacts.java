package com.microsoft.CognitiveServicesExample;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class EmergencyContacts extends AppCompatActivity {

    ListView lv;
    Button b,b1;
    static String name[]=new String[5];
    static String number[]=new String[5];
    static int k=0;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        lv=(ListView)findViewById(R.id.lv);
        b=(Button)findViewById(R.id.more);
        b1=(Button)findViewById(R.id.proceed);
        //db=openOrCreateDatabase("mydb",MODE_PRIVATE,null);
        //this.deleteDatabase("mydb");
        db=openOrCreateDatabase("mydb",MODE_PRIVATE,null);
        Cursor c;
        if(db!=null)
        {
            try{
                db.execSQL("create table if not exists emergency_contacts(NAME varchar(50), NUMBER varchar(50))");
            }
            catch(Exception e)
            {
                Toast.makeText(this,"Error="+e,Toast.LENGTH_SHORT).show();
            }
        }

        if(db!=null)
        {
            c=db.rawQuery("select * from emergency_contacts",null);
        }
        else
            c=null;

        k=0;
        while(c.moveToNext())
        {
            if(k<5) {
                name[k] = c.getString(0);
                number[k++] = c.getString(1);
            }
            else {
                //Toast.makeText(EmergencyContacts.this, "5 contacts only!", Toast.LENGTH_SHORT).show();
                b.setVisibility(View.GONE);
                b1.setVisibility(View.VISIBLE);
            }
        }
        for(int i=k;i<5;i++)
            name[i]="Enter contact information "+(i+1);
        CustomAdapter adapter=new CustomAdapter(this,number,name);
        lv.setAdapter(adapter);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EmergencyContacts.this, ContactInfo.class);
                startActivity(i);
                //finish();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EmergencyContacts.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(EmergencyContacts.this, ""+name[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
