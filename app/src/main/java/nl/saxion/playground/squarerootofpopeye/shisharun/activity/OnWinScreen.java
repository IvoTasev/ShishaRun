package nl.saxion.playground.squarerootofpopeye.shisharun.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import nl.saxion.playground.squarerootofpopeye.R;

public class OnWinScreen extends AppCompatActivity {

    static int lastLevel = 1;
    private TextView secView;
    private ImageView imageView;
    private TextView tvLevel;
    private View winScreen;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_win_screen);
        initComponents();
        setupMediaPlayer();
        mediaPlayer.start();

        tvLevel.setText("" + lastLevel);
        lastLevel ++;

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Toast nextLevel = Toast.makeText(getApplicationContext(), "Press on the screen to go to the next level", Toast.LENGTH_LONG);
        nextLevel.setMargin(0, 100);
        nextLevel.show();

        int[] photos = new int[]{ R.drawable.friends_first,
                R.drawable.friends_second,
                R.drawable.friends_third,
                R.drawable.friends_fourth,
                R.drawable.friends_fifth,
                R.drawable.friends_six,
                R.drawable.friends_seven,
                R.drawable.friends_eight,
                R.drawable.friends_ninth,
                R.drawable.friends_ten,};

        Random random = new Random();
        int index = random.nextInt(photos.length);
        this.imageView.setImageDrawable(getResources().getDrawable(photos[index]));

        secView.setText(PlayField.getTimeLeft() + "");

        winScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), PlayField.class);
                mediaPlayer.release();
                startActivity(intent);
            }
        });
    }

    private void setupMediaPlayer() {
        int[] sounds = new int[]{
                R.raw.skibka_happiness,
                R.raw.electronic_minute,
                R.raw.mission_success
        };
        int index = new Random().nextInt(sounds.length);
        mediaPlayer = MediaPlayer.create(this, sounds[index]);
    }

    @Override
    public void onBackPressed() {}                                                                  // disable back button

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    private void initComponents()
    {
        imageView = findViewById(R.id.imageWin);
        tvLevel = findViewById(R.id.win2Text);
        winScreen = findViewById(R.id.viewWinScreen);
        secView = findViewById(R.id.win6Text);
    }
}
