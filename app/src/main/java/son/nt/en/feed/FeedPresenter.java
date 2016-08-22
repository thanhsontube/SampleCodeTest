package son.nt.en.feed;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import son.nt.en.elite.EliteDto;

/**
 * Created by sonnt on 8/21/16.
 */
public class FeedPresenter implements FeedContract.Presenter {

    FeedContract.IRepository mRepository;
    FeedContract.View mView;

    @Inject
    public FeedPresenter(FeedContract.IRepository repository, FeedContract.View view) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void onStart() {
        //get reading
        mRepository.getElite(mObserver);
    }

    Observer<List<EliteDto>> mObserver = new Observer<List<EliteDto>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<EliteDto> eliteDtos) {

        }
    };
}
