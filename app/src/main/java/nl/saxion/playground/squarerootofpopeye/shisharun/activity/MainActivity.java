package nl.saxion.playground.squarerootofpopeye.shisharun.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.saxion.playground.squarerootofpopeye.R;

public class MainActivity extends AppCompatActivity {

    private TextView logo;                                                                          // the text as logo
    private Button playButton;
    private MediaPlayer mediaPlayer;                                                                // for music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupPlayer(R.raw.arabicpluckedstring);
        mediaPlayer.start();

        logo = findViewById(R.id.shisha_run_logo);
        playButton = findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlayField.class);
                mediaPlayer.release();                                                              // free up resources
                startActivity(intent);                                                              // start the game
            }
        });
    }

    /**
     * ready the media player to play the background music
     *
     * @param resId the resource id for raw sound
     */
    private void setupPlayer(int resId) {
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.setLooping(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setupPlayer(R.raw.arabicpluckedstring);
        mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

