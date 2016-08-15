package son.nt.en.esl;

import java.util.List;

import rx.Observable;
import rx.Observer;
import son.nt.en.base.BasePresenter;

/**
 * Created by sonnt on 7/14/16.
 */
public interface EslDailyContract {
    interface View {

        void resultSearch(List<EslDailyDto> mList);

        void setVisibility(boolean b);
    }

    interface Presenter extends BasePresenter
    {

        void onDestroy();

        void afterTextChanged(String s);
    }

    interface IRepository
    {

        void getData();

        void getData(Observer<List<EslDailyDto>> callback);


        Observable<List<EslDailyDto>> doSearch2(String s);

        List<EslDailyDto> doSearch3(String s);

        void doSearch4(String s, Observer<List<EslDailyDto>> callback);


    }
}
