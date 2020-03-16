package nl.saxion.playground.squarerootofpopeye.shisharun.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nl.saxion.playground.squarerootofpopeye.R;
import nl.saxion.playground.squarerootofpopeye.shisharun.entity.GameView;
import nl.saxion.playground.squarerootofpopeye.shisharun.entity.PlayerControl;
import nl.saxion.playground.squarerootofpopeye.shisharun.entity.RandomMessage;
import nl.saxion.playground.squarerootofpopeye.shisharun.timer.CustomCountdownTimer;
import nl.saxion.playground.squarerootofpopeye.shisharun.timer.IListenTimerTick;

public class PlayField extends AppCompatActivity {

    private Button ahead;
    private Button back;
    private Button left;
    private Button right;
    private ImageView aheadButtonImage;
    private ImageView downButtonImage;
    private ImageView leftButtonImage;
    private ImageView rightButtonImage;
    private TextView timerView;
    private TextView tvRandomMessage;
    private GameView gameView;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean messageShown;                                                                   // for random message
    private CustomCountdownTimer countdownTimer;
    private static final int timeToPass = 30000;                                                    // 60 seconds
    private static final int perSecondInterval = 1000;                                              // 1 seconds as interval
    private static long timeLeft;
    private IListenTimerTick listenTimerTick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);
        initComponents();
        setupMediaPlayer(R.raw.clock_tick_speed_up);

        aheadButtonImage.setImageResource(R.drawable.ahead);
        downButtonImage.setImageResource(R.drawable.down);
        leftButtonImage.setImageResource(R.drawable.left);
        rightButtonImage.setImageResource(R.drawable.right);

        listenTimerTick = new IListenTimerTick() {
            @Override
            public void onTick(long millisLeft) {
                timerView.setText(String.valueOf(millisLeft / 1000));

                if ((millisLeft / 1000) < 20 && !messageShown)                                       // if 20 seconds left
                {
                    createDramaticEffects();
                    messageShown = true;
                }
                timeLeft = millisLeft;
            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
                Intent loseScreen = new Intent(PlayField.this, OnLoseScreen.class);
                startActivity(loseScreen);
            }

            @Override
            public void onCancel() {
                mediaPlayer.reset();
            }
        };
        initCountdownTimer(timeToPass + timeLeft);
        listenPlayerInput();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        countdownTimer.cancel();
    }

    @Override
    protected void onRestart() {
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();
        super.onRestart();
        initCountdownTimer(timeLeft);
        setupMediaPlayer(R.raw.clock_tick_speed_up);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder confirmExit = new AlertDialog.Builder(PlayField.this);
        confirmExit.setTitle("out of coal?");
        confirmExit.setMessage("your friends wont be happy");
        confirmExit.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.reset();
                countdownTimer.cancel();
                finishAffinity();
                finish();
            }
        });
        confirmExit.setNegativeButton("Nay", null).show();
    }

    private void initCountdownTimer(long milliseconds) {
        countdownTimer = new CustomCountdownTimer((milliseconds), perSecondInterval, listenTimerTick);
        countdownTimer.start();
    }

    private void listenPlayerInput() {
        ahead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.updatePlayer(PlayerControl.UP);
                checkExit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.updatePlayer(PlayerControl.DOWN);
                checkExit();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.updatePlayer(PlayerControl.LEFT);
                checkExit();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.updatePlayer(PlayerControl.RIGHT);
                checkExit();
            }
        });
    }

    private void checkExit() {
        if (!gameView.atExit()) return;
        countdownTimer.cancel();
        Intent winScreen = new Intent(PlayField.this, OnWinScreen.class);
        startActivity(winScreen);
    }

    private void createDramaticEffects() {
        mediaPlayer.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(50);
        }
        tvRandomMessage.setText(RandomMessage.getInstance().getRandomMessage());
    }

    private void setupMediaPlayer(int resId) {
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.setLooping(false);
    }

    private void initComponents() {
        gameView = findViewById(R.id.mazeView);
        timerView = findViewById(R.id.tvTimer);
        tvRandomMessage = findViewById(R.id.tvRandomMessage);
        ahead = findViewById(R.id.moveAheadbutton);
        back = findViewById(R.id.moveBackButton);
        left = findViewById(R.id.moveLeftButton);
        right = findViewById(R.id.moveRightButton);
        aheadButtonImage = findViewById(R.id.aheadImageButton);
        downButtonImage = findViewById(R.id.downImageButton);
        leftButtonImage = findViewById(R.id.leftImageButton);
        rightButtonImage = findViewById(R.id.rightImageButton);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    public static long getTimeLeft() {
        return timeLeft / 1000;
    }
}