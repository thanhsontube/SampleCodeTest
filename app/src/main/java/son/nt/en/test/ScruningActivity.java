package son.nt.en.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import son.nt.en.R;
import son.nt.en.utils.Logger;

public class ScruningActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = ScruningActivity.class.getSimpleName();
    @BindView(R.id.btn1)
    Button btn1; @BindView(R.id.btn2)
    TextView btn2;
    Observer<String> mObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scruning);
        ButterKnife.bind(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        mObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.debug(TAG, ">>>" + "mObserver onCompleted");

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.debug(TAG, ">>>" + "mObserver onNext:" + s);

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: {
//                sample1();
                t1();
                publishObject();
                range();

                break;
            }
            case R.id.btn2:
            {
                break;
            }
        }
    }

    private void sample1() {
        Observable.OnSubscribe<String> onSubscribeAction = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World");
                subscriber.onCompleted();


            }
        };
        Observable<String> stringObservable = Observable.create(onSubscribeAction);

        Subscriber<String> stringSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Logger.debug(TAG, ">>>" + "onCompleted");

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.debug(TAG, ">>>" + "onNext:" + s);

            }
        };

        Observer<String> stringObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.debug(TAG, ">>>" + "stringObserver onCompleted");

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.debug(TAG, ">>>" + "stringObserver onNext:" + s);

            }
        };

        stringObservable.observeOn(AndroidSchedulers.mainThread());
        stringObservable.subscribe(stringSubscriber);
        stringObservable.subscribe(stringObserver);

    }

    private void sample3() {
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Logger.debug(TAG, ">>>" + "action1 sample3:" + s);
            }
        };

        Observable<String> stringObservable = Observable.just("sonnt");
        stringObservable.subscribe(action1);
        stringObservable.observeOn(AndroidSchedulers.mainThread());
    }

    private void testLambdas() {
        List<String> list = Arrays.asList("Hello", "Streams", "Not");
        Observable.from(list).
                filter(s -> s.contains("e")).
                map(s -> s.toUpperCase()).
                reduce(new StringBuilder(), StringBuilder::append).
                subscribe(System.out::print, e -> {
                        },
                        () -> System.out.println("!"));

        Observable<String> observable = Observable.from(list);
        observable.filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.contains("e");
            }
        })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.toUpperCase();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                    }
                });

        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });

//        observable.subscribe(new Observer<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//        });
    }

    private void t1()
    {
        Observable<Integer> observableString = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> observer) {

                for (int i = 0; i < 5; i++) {
                    observer.onNext(i);
                }

                observer.onCompleted();
            }
        });


        Observable.OnSubscribe<Integer> onSubscribe2 = subscriber -> {
            for (int i = 0; i < 5; i ++)
            {
                subscriber.onNext(i);
            }
            subscriber.onCompleted();
        };

        Subscription subscriptionPrint = observableString.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Observable completed");
                Logger.debug(TAG, ">>>" + "Observable completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Oh no! Something wrong happened!");
            }

            @Override
            public void onNext(Integer item) {
                Logger.debug(TAG, ">>>" +"Item is " + item );
            }
        });
    }

    private void publishObject ()
    {
        Logger.debug(TAG, ">>>publishObject" );
        PublishSubject<String> stringPublishSubject = PublishSubject.create();
        Subscription subscription = stringPublishSubject.subscribe(mObserver);
        stringPublishSubject.onNext("I love u");
    }

    private void range ()
    {
        Observable.range(10, 3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Integer number) {
                        Logger.debug(TAG, ">>>" + "RAnge:" + number);
                    }
                });

        Subscription subscription = Observable.interval(3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Logger.debug(TAG, ">>>" + "interval:" + aLong);
                    }
                });
    }
}
