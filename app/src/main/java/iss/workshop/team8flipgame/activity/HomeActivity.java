package iss.workshop.team8flipgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import iss.workshop.team8flipgame.R;
import iss.workshop.team8flipgame.service.BGMusicService;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener , ServiceConnection {
    BGMusicService bgMusicService;
    public Boolean IS_MUTED = false ; //Setting of BG Music
    RadioGroup difficulties;
    int cardCount;
    String difficulty;
    SharedPreferences game_service;
    SharedPreferences.Editor game_service_editor;
    ImageButton toggle;
    Button play;
    Button leader;
    Button credits;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming);
        game_service = getSharedPreferences("game_service",MODE_PRIVATE);
        game_service_editor = game_service.edit();

        //top bar
        toggle = findViewById(R.id.soundToggle);
        if (toggle != null) { toggle.setOnClickListener(this); }

        //play button
        play = findViewById(R.id.play);
        if (play != null) { play.setOnClickListener(this); }
        play.setEnabled(false);

        //difficulty radio
        difficulties = findViewById(R.id.difficultyRD);
        difficulties.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.easy_modeRD:
                        cardCount=6;
                        difficulty="Easy";
                        game_service_editor.putString("difficulty",difficulty);
                        game_service_editor.putInt("cardcount",cardCount);
                        game_service_editor.commit();
                        play.setEnabled(true);
                        break;
                    case R.id.normal_modeRD:
                        cardCount=10;
                        difficulty="Normal";
                        game_service_editor.putString("difficulty",difficulty);
                        game_service_editor.putInt("cardcount",cardCount);
                        game_service_editor.commit();
                        play.setEnabled(true);
                        break;
                    case R.id.hard_modeRD:
                        cardCount=14;
                        difficulty="Hard";
                        game_service_editor.putString("difficulty",difficulty);
                        game_service_editor.putInt("cardcount",cardCount);
                        game_service_editor.commit();
                        play.setEnabled(true);
                        break;
                }

            }
        });

        //leaderboard button
        leader = findViewById(R.id.leaderBoard);
        if (leader != null) { leader.setOnClickListener(this); }

        //credits button
        credits = findViewById(R.id.credits);
        if (credits != null) { credits.setOnClickListener(this); }




        //Bianca Music Service
        if (!IS_MUTED){
            Intent music = new Intent(this, BGMusicService.class);
            bindService(music, this, BIND_AUTO_CREATE);
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.play) {
            Intent intent = new Intent(this, ImagePickingActivity.class);
            intent.putExtra("IS_MUTED",IS_MUTED); //pass music setting
            startActivity(intent);

        }
        else if (id == R.id.leaderBoard) {
            Intent intent = new Intent(this, LeaderBoardActivity.class);
            intent.putExtra("IS_MUTED",IS_MUTED); //pass music setting
            startActivity(intent);
        }
        else if (id == R.id.soundToggle) {
            if (IS_MUTED) {
                Log.i("MusicLog", "BGMusicService -> UNMUTED");
                IS_MUTED = false;
                Intent music = new Intent(this, BGMusicService.class);
                bindService(music, this, BIND_AUTO_CREATE);
            }
            else {
                bgMusicService.mute();
                unbindService(this);
                IS_MUTED = true;
                Log.i("MusicLog", "BGMusicService -> MUTED");
            }
        }

        else if (id == R.id.credits) {
            Intent intent = new Intent(this,CreditsActivity.class);
            startActivity(intent);
        }
    }

    //Bianca Lifecycle
    @Override
    public void onPause(){
        super.onPause();
        // pause music
        if(bgMusicService!=null) bgMusicService.pause();
    }
    @Override
    public void onResume(){
        super.onResume();
        // restore
        if(bgMusicService!=null) bgMusicService.resume();
        else if(!IS_MUTED) {
            Intent music = new Intent(this, BGMusicService.class);
            bindService(music, this, BIND_AUTO_CREATE);
        }

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(bgMusicService!=null)
            unbindService(this);// unbindService
        // end everything
    }

    //Bianca Music Service
    //@Override
    public void onServiceConnected(ComponentName name, IBinder binder){
        BGMusicService.LocalBinder musicBinder = (BGMusicService.LocalBinder) binder;
        if(binder != null) {
            bgMusicService = musicBinder.getService();
            bgMusicService.playMusic("MENU");
            Log.i("MusicLog", "BGMusicService Connected, location: HOME.");
        }
    }
    @Override
    public void onServiceDisconnected(ComponentName name){
        Log.i("MusicLog", "BGMusicService DIS-Connected.");

    }


}