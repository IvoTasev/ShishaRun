package nl.saxion.playground.squarerootofpopeye.shisharun.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import nl.saxion.playground.squarerootofpopeye.R;

public class OnLoseScreen extends AppCompatActivity {
    private ImageView imageView;
    private View loseScreen;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_lose_screen);
        initComponents();

        setupPlayer();
        mediaPlayer.start();

        OnWinScreen.lastLevel = 1;

        Toast toast = Toast.makeText(getApplicationContext(), "Press on the screen to go to the main menu", Toast.LENGTH_SHORT);
        toast.setMargin(50, 650);
        toast.show();

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        int[] photos = new int[]{R.drawable.fail_first};

        Random random = new Random();
        int index = random.nextInt(photos.length);
        this.imageView.setImageDrawable(getResources().getDrawable(photos[index]));

        loseScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * ready the media player to play the background music
     */
    private void setupPlayer() {
        int[] sounds = new int[]{
                R.raw.sad,
                R.raw.arabiansound
        };
        int index = new Random().nextInt(sounds.length);
        mediaPlayer = MediaPlayer.create(this, sounds[index]);
        mediaPlayer.setLooping(false);
    }

    private void initComponents() {
        imageView = findViewById(R.id.imageLose);
        loseScreen = findViewById(R.id.viewLoseScreen);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    @Override
    public void onBackPressed() {}                                                                  // disable back button
}