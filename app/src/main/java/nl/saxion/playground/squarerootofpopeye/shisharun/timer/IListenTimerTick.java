package nl.saxion.playground.squarerootofpopeye.shisharun.timer;

public interface IListenTimerTick {

    void onTick(long millisLeft);

    void onFinish();

    void onCancel();
}
