package app.mmguardian.com.location_tracking.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class CountDownTimer {

    private TimeUnit timeUnit;
    private Long startValue;
    private Disposable disposable;
    private boolean running=false;

    public CountDownTimer(Long startValue,TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        this.startValue = startValue;
    }

    public abstract void onTick(long tickValue);
    public abstract void onFinish();
    public abstract void onError(Throwable e);

    public boolean isRunning(){
        return running;
    }

    public void start(){
        io.reactivex.Observable.zip(
                io.reactivex.Observable.range(0, startValue.intValue()), io.reactivex.Observable.interval(1, timeUnit), (integer, aLong) -> {
                    Long l = startValue-integer;
                    return l;
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        running = true;
                        onTick(aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        running=false;
                        onFinish();
                    }
                });
    }

    public void cancel(){
        running = false;
        if(disposable!=null) disposable.dispose();
    }
}
