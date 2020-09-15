package com.example.musicplay;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
String[] items;
ListView listView;
    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
listView=(ListView)findViewById(R.id.listview);
runtimepermission();
    }
    public void runtimepermission()
    {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    private void display() {
        final ArrayList<File> arrayList=arrayList(Environment.getExternalStorageDirectory());
        int a =arrayList.size();
        items=new String[a];
        for(int i=0;i<a;i++) {
            items[i] = arrayList.get(i).getName().toString().replace(".mp3", "").replace("m4a", "");
        }
            CustomAdapter arrayAdapter=new CustomAdapter(this,items);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    String sname= (String) listView.getItemAtPosition(i);
                    Intent intent=new Intent(getApplicationContext(),Player.class);
                    intent.putExtra("songs",arrayList);
                    intent.putExtra("songname",sname);
                    intent.putExtra("position",i);
                    startActivity(intent);
                }
            });
        }
    public ArrayList<File> arrayList(File file)
    {
        ArrayList<File> arraylist=new ArrayList<File>();
        File[] array=file.listFiles();
        for(File files:array)
        {
            if(files.isDirectory()&& !files.isHidden())
            {
                arraylist.addAll(arrayList(files));
            }
            else
            {
                if(files.getName().endsWith(".mp3")||files.getName().endsWith(".m4a"))
                {
                    arraylist.add(files);
                }
            }
        }
        return arraylist;
    }
}