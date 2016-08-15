package son.nt.en.elite;

import javax.inject.Inject;

import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 7/15/16.
 */
public class EliteDailyPresenter implements ContractDailyElite.Presenter
{
    private static final String           TAG = EliteDailyPresenter.class.getSimpleName();
    private ContractDailyElite.View       mView;
    private ContractDailyElite.Repository mRepository;

    @Inject
    public EliteDailyPresenter(ContractDailyElite.View mView, ContractDailyElite.Repository repository)
    {
        this.mView = mView;
        this.mRepository = repository;
    }

    @Override
    public void onStart()
    {
        Logger.debug(TAG, ">>>" + "onStart");

    }
}
