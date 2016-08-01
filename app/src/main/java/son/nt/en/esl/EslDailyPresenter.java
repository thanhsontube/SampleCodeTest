package son.nt.en.esl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sonnt on 7/15/16.
 */
public class EslDailyPresenter implements EslDailyContract.Presenter {

    private static final String TAG = EslDailyPresenter.class.getSimpleName();
    EslDailyContract.View mView;
    EslDailyContract.IRepository mRepository;

    public EslDailyPresenter(EslDailyContract.View mView, EslDailyContract.IRepository mRepository
    ) {
        this.mView = mView;
        this.mRepository = mRepository;
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

        Observable<List<EslDailyDto>> listObservable = mRepository.doSearch2(s);

        listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(750, TimeUnit.MILLISECONDS)
                .subscribe(observer);


    }


}
