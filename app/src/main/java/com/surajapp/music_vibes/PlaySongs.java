package com.surajapp.music_vibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySongs extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateBar.interrupt();
    }
    TextView textView,currentTime,totalTime,heartRate;
    ImageView play,previous,next,back,menu;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    String textContent;
    int position;
    Thread updateBar;
    private boolean ismenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_songs);
        textView = findViewById(R.id.textView);
        heartRate = findViewById(R.id.heartRate);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darkPink));

        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        seekBar = findViewById(R.id.seekBar);

        // get the Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        //heartRate
        heartRate.setSelected(true);
        // Play song
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        // song duration
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                String total = createTimerLabel(mediaPlayer.getDuration());
                totalTime.setText(total);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                //playIcon.setImageResource(R.drawable.ic_pause_black_24dp);
            }
        });
        //seekbar
        seek();

        //play,pause,next,previous -- onClickListener
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }else{
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position = position - 1;
                }else{
                    position = songs.size() - 1;
                }
                // set previous song
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        String total = createTimerLabel(mediaPlayer.getDuration());
                        totalTime.setText(total);
                        seekBar.setMax(mediaPlayer.getDuration());
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.setLooping(true);
                seek();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                // set text
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size() - 1){
                    position = position + 1;
                }else{
                    position = 0;
                }
                // set next song
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        String total = createTimerLabel(mediaPlayer.getDuration());
                        totalTime.setText(total);
                        seekBar.setMax(mediaPlayer.getDuration());
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.setLooping(true);
                seek();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                // set text
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });

        //ActionBer
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                finish();
            }
        });
    }
    public String createTimerLabel(int duration){
        String timerLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timerLabel += min + ":";
        if(sec<10)timerLabel += "0";
        timerLabel += sec;

        return timerLabel;
    }
    private void seek(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.setProgress(i);
                String cTime = createTimerLabel(i);
                currentTime.setText(cTime);
                String total = createTimerLabel(mediaPlayer.getDuration());
                if(cTime.equals(total))play.setImageResource(R.drawable.play);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateBar = new Thread() {
            public void run() {
                int currentPosition = 0;
                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateBar.start();
    }
}