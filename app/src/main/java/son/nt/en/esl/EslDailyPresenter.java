package son.nt.en.esl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by sonnt on 7/15/16.
 */
public class EslDailyPresenter implements EslDailyContract.Presenter
{

    private static final String       TAG = EslDailyPresenter.class.getSimpleName();
    EslDailyContract.View             mView;
    EslDailyContract.IRepository      mRepository;

    PublishSubject<String>            observableSearch;
    Subscription                      subscriptionSearch;

    public EslDailyPresenter(EslDailyContract.View mView, EslDailyContract.IRepository repo) {
        this.mView = mView;
        this.mRepository = repo;

        observableSearch = PublishSubject.create();
        subscriptionSearch = observableSearch.debounce(750, TimeUnit.MILLISECONDS)
                .map(s -> mRepository.doSearch3(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    public void onStart()
    {
        mRepository.getData(observer);
    }

    final Observer<List<EslDailyDto>> observer  = new Observer<List<EslDailyDto>>()
                                                {
                                                    @Override
                                                    public void onCompleted()
                                                    {

                                                    }

                                                    @Override
                                                    public void onError(Throwable e)
                                                    {

                                                    }

                                                    @Override
                                                    public void onNext(List<EslDailyDto> list)
                                                    {
                                                        mView.resultSearch(list);
                                                    }
                                                };



    @Override
    public void onDestroy()
    {
        subscriptionSearch.unsubscribe();
    }

    @Override
    public void afterTextChanged(String s)
    {
        mView.setVisibility(s.length() > 0);
        observableSearch.onNext(s);
    }

}
