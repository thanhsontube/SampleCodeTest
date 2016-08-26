package son.nt.en.feed;

import java.util.List;

import rx.Observer;
import rx.Subscriber;
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
    public static final String TAG = "FeedPresenter";

    FeedContract.IRepository mRepository;
    FeedContract.View mView;

//    CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    CompositeSubs mCompositeSubs;

    /**
     * Module {@link son.nt.en.feed.di.FeedPresenterModule} will provide dependencies required
     * to Dagger {@link son.nt.en.feed.di.FeedComponent} to create an instance of this class.
     *
     * @param repository refer to {@link FeedRepository}
     * @param view       refer to {@link FeedFragment}
     */
    public FeedPresenter(FeedContract.IRepository repository, FeedContract.View view) {
        mRepository = repository;
        mView = view;
        mCompositeSubs = new CompositeSubs();
    }

    @Override
    public void onStart() {
        Subscription subscription = mRepository.getDailyHelloChao()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);

        //ESL
        mRepository.getESL (mSubscriberEsl);

        //gets Elite data from Firebase.
        mRepository.getElite(mObserver);
        mCompositeSubs.add(subscription);

//        mCompositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
//        mCompositeSubscription.unsubscribe();
        mCompositeSubs.removeAll();
    }

    Subscriber<List<EslDailyDto>> mSubscriberEsl = new Subscriber<List<EslDailyDto>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<EslDailyDto> eslDailyDtos) {
            mView.setEslData (eslDailyDtos);

        }
    };

    Subscriber<List<HelloChaoSentences>> mSubscriber = new Subscriber<List<HelloChaoSentences>>() {
        @Override
        public void onCompleted() {
//            Logger.debug(TAG, ">>>" + "onCompleted");

        }

        @Override
        public void onError(Throwable e) {
//            Logger.debug(TAG, ">>>" + "onError:" + e);

        }

        @Override
        public void onNext(List<HelloChaoSentences> helloChaoSentences) {
//            Logger.debug(TAG, ">>>" + "onNext:" + helloChaoSentences.size());
            mView.setDailyHelloChao(helloChaoSentences);
        }
    };

    Observer<List<EliteDto>> mObserver = new Observer<List<EliteDto>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<EliteDto> eliteDtos) {
//            Logger.debug(TAG, ">>>" + "onNext:" + eliteDtos.size());
            mView.setEliteData(eliteDtos);

        }
    };
}
