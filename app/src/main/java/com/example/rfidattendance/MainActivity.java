package com.example.rfidattendance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference reference , reference1 ;

    private static final String FileName = "Attendance_Data.txt";
    ArrayList<userData> list = new ArrayList<>();                            // creating list for data


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);                      // set id of recycler view
        database = FirebaseDatabase.getInstance();                           // getting instance of database
        reference = database.getReference("users");                    // getting database reference
        reference1 = database.getReference("wifi/value");


        MyAdapter adapter = new MyAdapter(this,list);
        recyclerView.setAdapter(adapter);



      reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot snapshot1:snapshot.getChildren())
                {

                   userData userData1 = snapshot1.getValue(com.example.rfidattendance.userData.class);
                   list.add(userData1);


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)            // code to inflate menu bar
    {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @SuppressLint({"NonConstantResourceId", "WorldReadableFiles"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.reset:

                 reference1.setValue("reset");
                Toast.makeText(this, "Module reseted", Toast.LENGTH_LONG).show();

                break;
            case  R.id.save:

                String data = list.toString();

                FileOutputStream stream  = null;                              // storing data to local storage
                try {
                    stream = openFileOutput(FileName,MODE_APPEND);
                    stream.write(data.getBytes());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally {
                    if(null != stream)
                    {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Toast.makeText(this, "saved to file"+ getFilesDir() + FileName, Toast.LENGTH_LONG).show();
                break;

            default:
                break;

        }
        return true;
    }
}