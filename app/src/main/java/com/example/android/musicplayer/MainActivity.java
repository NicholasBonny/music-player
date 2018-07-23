package com.example.android.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.lvPlayList);
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        for(int i = 0; i < mySongs.size(); i++){
            //toast(mySongs.get(i).getName().toString());
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".mp4","")
                    .replace(".wav","");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.song_layout,R.id.textView,items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class)
                .putExtra("pos", position).putExtra("songList",mySongs));
            }
        });

    }

    public ArrayList<File> findSongs(File root){
        ArrayList<File> arrayList =  new ArrayList<File>();

        File[] files = root.listFiles();
        for (File Downloads : files){

            if(Downloads.isDirectory()){
                arrayList.addAll(findSongs(Downloads));
            }else {
                if (Downloads.getName().endsWith("mp3") || Downloads.getName().endsWith("mp4")
                        || Downloads.getName().endsWith("wav")){
                    arrayList.add(Downloads);
                }
            }
        }
        return arrayList;
    }

    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}
