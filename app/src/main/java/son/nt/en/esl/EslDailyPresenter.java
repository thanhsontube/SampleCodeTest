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
    PublishSubject<String> publishSubject ;

    public EslDailyPresenter(EslDailyContract.View mView, EslDailyContract.IRepository repo) {
        this.mView = mView;
        this.mRepository = repo;

        publishSubject = PublishSubject.create();


        Subscription subscription = publishSubject.debounce(750, TimeUnit.MILLISECONDS)
                .map(new Func1<String, List<EslDailyDto>>() {
                    @Override
                    public List<EslDailyDto> call(String s) {
                        return mRepository.doSearch3(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onStart() {
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

    @Override
    public void onDestroy() {

    }

    @Override
    public void afterTextChanged(String s) {
        mView.setVisibility(s.length() > 0);
        publishSubject.onNext(s);

    }

}
