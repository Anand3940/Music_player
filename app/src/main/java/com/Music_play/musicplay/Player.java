
package com.Music_play.musicplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {
    Button play;
    Button next;
    Button previous;
    TextView textView,currentDuration,TotalDuration;
    SeekBar seekBar;
    static MediaPlayer mediaPlayer;
    int postion=0;
    String sname;
    Handler handler=new Handler();
    ArrayList<File> arrayList;
    Thread updateseekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        play=(Button)findViewById(R.id.playbutton);
        textView=(TextView)findViewById(R.id.songnametextview);
        currentDuration=(TextView)findViewById(R.id.cduration);
        TotalDuration=(TextView)findViewById(R.id.tduration);
        next=(Button)findViewById(R.id.nextbutton);
        previous=(Button)findViewById(R.id.prevbutton);
        getSupportActionBar().setTitle("Song is Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        updateseekbar=new Thread()
        {
            @Override
            public void run() {
                int total=mediaPlayer.getDuration();
                int current=0;
                while(current<total)
                {
                    try {
                        sleep(400);
                        current=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(current);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(mediaPlayer!=null && mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        arrayList=(ArrayList) bundle.getParcelableArrayList("songs");
        String songname=i.getStringExtra("songname");
        textView.setText(songname);
        textView.setSelected(true);
        postion=bundle.getInt("position",0);
        Uri u=Uri.parse(arrayList.get(postion).toString());
        mediaPlayer=MediaPlayer.create(this,u);
        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorBlack),PorterDuff.Mode.MULTIPLY);
        String totaltime=createtimelabel(mediaPlayer.getDuration());
        TotalDuration.setText(totaltime);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                    seekBar.setProgress(i);
                }
                else{
                    // the event was fired from code and you shouldn't call player.seekTo()
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying())
                {
                    play.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else
                {
                    play.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                postion=((postion-1)<0?arrayList.size()-1:postion-1);
                Uri u=Uri.parse(arrayList.get(postion).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=arrayList.get(postion).getName().toString();
                textView.setText(sname);
                mediaPlayer.start();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                postion=(postion+1)%arrayList.size();
                Uri u=Uri.parse(arrayList.get(postion).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=arrayList.get(postion).getName().toString();
                textView.setText(sname);
                mediaPlayer.start();
            }
        });
    }

    private String createtimelabel(int duration) {
        String timelabel="" ;
        int min=duration/1000/60;
        int sec=duration/1000%60;
        timelabel+=min+":";
        if(sec<10)
            timelabel+="0";
        timelabel+= sec;
        return timelabel;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}