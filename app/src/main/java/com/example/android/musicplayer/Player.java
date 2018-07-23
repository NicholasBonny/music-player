package com.example.android.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mediaPlayer;
    ArrayList<File> mySongs;
    int position;
    Uri uri;
    Thread updateSeakbar;

    SeekBar seekBar;
    Button btnPlay, btnNxt, btnPrev, btnFF, btnFB;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btnFB = (Button) findViewById(R.id.btnfb);
        btnFF = (Button) findViewById(R.id.btnff);
        btnNxt = (Button) findViewById(R.id.btnNxt);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnPlay = (Button) findViewById(R.id.playbtn);

        btnFB.setOnClickListener(this);
        btnFF.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnNxt.setOnClickListener(this);

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        updateSeakbar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration){
                    try{
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songList");
        position = bundle.getInt("pos", 0);

        uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeakbar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.playbtn:
                if(mediaPlayer.isPlaying()){
                    btnPlay.setText(">");
                    mediaPlayer.pause();
                }else {
                    btnPlay.setText("||");
                    mediaPlayer.start();
                }
                break;

            case R.id.btnff:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                break;

            case R.id.btnfb:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;

            case R.id.btnNxt:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position+1)%mySongs.size();
                uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;

            case R.id.btnPrev:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position-1 < 0)? mySongs.size()-1: position-1;
                /**
                 *  if(position-1 < 0 ){
                 position = mySongs.size()-1;
                 }else{
                 position = position - 1;
                 }
                 */
                uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;
        }
    }
}
