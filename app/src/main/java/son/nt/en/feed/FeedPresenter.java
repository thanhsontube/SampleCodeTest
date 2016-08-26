package son.nt.en.feed;

import java.util.ArrayList;
import java.util.List;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.en.elite.EliteDto;
import son.nt.en.esl.EslDailyDto;
import son.nt.en.hellochao.HelloChaoSentences;
import son.nt.en.utils.CompositeSubs;

/**
 * Created by sonnt on 8/21/16.
 */
public class FeedPresenter implements FeedContract.Presenter {
    public static final String TAG = FeedPresenter.class.getSimpleName();

    FeedContract.IRepository mRepository;
    FeedContract.View mView;

    CompositeSubs mCompositeSubs;

    /**
     * Module {@link son.nt.en.feed.di.FeedPresenterModule} will provide dependencies required
     * to Dagger {@link son.nt.en.feed.di.FeedComponent} to create an instance of this class.
     *
     * @param repository refer to {@link FeedRepository}
     * @param view       refer to {@link FeedFragment}
     */
    public FeedPresenter(FeedContract.IRepository repository, FeedContract.View view, CompositeSubs compositeSubs) {
        mRepository = repository;
        mView = view;
        mCompositeSubs = compositeSubs;
    }

    @Override
    public void onStart() {

        Subscription subscription = mRepository.getDailyHelloChao()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mListSingleSubscriberHc);
        mCompositeSubs.add(subscription);

        //ESL
        mRepository.getESL(mSingleSubscriberEsl);

        //Elite
        mRepository.getElite(mSingleSubscriberElite);

    }

    @Override
    public void onStop() {
        mCompositeSubs.removeAll();
    }

    //HC
    SingleSubscriber<List<HelloChaoSentences>> mListSingleSubscriberHc = new SingleSubscriber<List<HelloChaoSentences>>() {
        @Override
        public void onSuccess(List<HelloChaoSentences> value) {
            mView.setDailyHelloChao(value);
        }

        @Override
        public void onError(Throwable error) {
            mView.setDailyHelloChao(new ArrayList<>());
        }
    };

    //Esl
    SingleSubscriber<List<EslDailyDto>> mSingleSubscriberEsl = new SingleSubscriber<List<EslDailyDto>>() {
        @Override
        public void onSuccess(List<EslDailyDto> value) {
            mView.setEslData(value);
        }

        @Override
        public void onError(Throwable error) {
            mView.setEslData(new ArrayList<>());

        }
    };

    //Elite
    SingleSubscriber<List<EliteDto>> mSingleSubscriberElite = new SingleSubscriber<List<EliteDto>>() {
        @Override
        public void onSuccess(List<EliteDto> value) {
            mView.setEliteData(value);
        }

        @Override
        public void onError(Throwable error) {
            mView.setEliteData(new ArrayList<>());

        }
    };


}
