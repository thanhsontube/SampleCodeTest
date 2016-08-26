package son.nt.en.esl;

import java.util.List;

import rx.Observer;
import son.nt.en.base.IBasePresenter;

/**
 * Created by sonnt on 7/14/16.
 */
public interface EslDailyContract
{
    interface View
    {

        void resultSearch(List<EslDailyDto> mList);

        void setVisibility(boolean b);
    }

    interface Presenter extends IBasePresenter
    {

        void onDestroy();

        void afterTextChanged(String s);
    }

    interface IRepository
    {
        void getData(Observer<List<EslDailyDto>> callback);

        void doSearch4(String s, Observer<List<EslDailyDto>> callback);

        List<EslDailyDto> doSearch3(String filter);
    }
}
