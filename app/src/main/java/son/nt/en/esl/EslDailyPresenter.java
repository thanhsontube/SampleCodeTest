package son.nt.en.esl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by sonnt on 7/15/16.
 */
public class EslDailyPresenter implements EslDailyContract.Presenter {

    private static final String TAG = EslDailyPresenter.class.getSimpleName();
    EslDailyContract.View mView;
    EslDailyContract.IRepository mRepository;
    PublishSubject<String> publishSubject;
    PublishSubject<List<EslDailyDto>> publishSubject2;
    Subscription subscription;
    Subscription subscription2;

    public EslDailyPresenter(EslDailyContract.View mView, EslDailyContract.IRepository repo, PublishSubject<List<EslDailyDto>> publishSubject2) {
        this.mView = mView;
        this.mRepository = repo;
        this.publishSubject2 = publishSubject2;

        publishSubject = PublishSubject.create();


//        subscription = publishSubject.debounce(750, TimeUnit.MILLISECONDS)
//                .map(filter -> mRepository.doSearch3(filter))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);

        subscription = publishSubject.debounce(750, TimeUnit.MILLISECONDS)
//                .doOnNext(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        mRepository.doSearch4(s, observer2);
//                    }
//                })
                .map(new Func1<String, Void>() {
                    @Override
                    public Void call(String s) {
                        mRepository.doSearch4(s, observer2);
                        return null;
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();


        subscription2 = publishSubject2
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);


    }

    @Override
    public void onStart() {
//        mRepository.getData();
        mRepository.getData(observer);
    }


    final Observer<List<EslDailyDto>> observer = new Observer<List<EslDailyDto>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<EslDailyDto> list) {
            mView.resultSearch(list);
        }
    };

    final Observer<List<EslDailyDto>> observer2 = new Observer<List<EslDailyDto>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<EslDailyDto> list) {
            mView.resultSearch(list);
        }
    };


    @Override
    public void onDestroy() {
        subscription.unsubscribe();
        subscription2.unsubscribe();
    }

    @Override
    public void afterTextChanged(String s) {
        mView.setVisibility(s.length() > 0);
//        mRepository.doSearch3(observer);
        publishSubject.onNext(s);

    }

}
