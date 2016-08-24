package son.nt.en.feed;

import java.util.List;

import rx.Observer;
import son.nt.en.base.BasePresenter;
import son.nt.en.elite.EliteDto;

/**
 * Created by sonnt on 7/14/16.
 */
public interface FeedContract {
    interface View {


        void setEliteData(List<EliteDto> eliteDtos);
    }

    interface Presenter extends BasePresenter {


    }

    interface IRepository {
        void getElite (Observer<List<EliteDto>> listObserver);
    }
}
