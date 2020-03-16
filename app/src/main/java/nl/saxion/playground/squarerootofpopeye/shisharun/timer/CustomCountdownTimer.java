package nl.saxion.playground.squarerootofpopeye.shisharun.timer;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CustomCountdownTimer {
    private final long countdownInterval;
    private static long stopTimeInFuture;
    private IListenTimerTick tickListener;
    private ScheduledExecutorService scheduler;
    private boolean isCancelled;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());                        //Callback thread

    /**
     * Constructor
     *
     * @param millisInFuture the amount of milliseconds to countdown
     * @param countDownInterval interval in seconds
     * @param tickListener interface for delegation
     */
    public CustomCountdownTimer(long millisInFuture, long countDownInterval, IListenTimerTick tickListener) {
        stopTimeInFuture = SystemClock.elapsedRealtime() + millisInFuture;
        countdownInterval = countDownInterval;
        this.tickListener = tickListener;
    }

    /**
     * prepare the thread scheduler and starts the timer
     */
    public synchronized void start() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(new TimerRunnable(), 0, countdownInterval,
                TimeUnit.MILLISECONDS);
    }

    /**
     * cancels the timer
     */
    public synchronized void cancel()
    {
        if(!isCancelled)
            isCancelled = true;
    }
    //Interface callback handler
    private final class TimerRunnable implements Runnable {
        public void run() {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    long millisLeft = stopTimeInFuture - SystemClock.elapsedRealtime();
                    if(isCancelled)
                    {
                        tickListener.onCancel();
                        scheduler.shutdown();
                    }
                    if (millisLeft >= 1) {
                        tickListener.onTick(millisLeft);
                    } else {
                        tickListener.onFinish();
                        scheduler.shutdown();
                    }
                }
            });
        }
    }
}