package son.nt.en.elite;

import son.nt.en.base.IBasePresenter;

/**
 * Created by sonnt on 7/14/16.
 */
public interface ContractDailyElite {
    interface View {

    }

    interface Presenter extends IBasePresenter
    {

    }

    interface Repository {

        void getData();
    }
}
