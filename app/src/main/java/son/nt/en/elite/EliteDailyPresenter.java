package son.nt.en.elite;

import javax.inject.Inject;

import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 7/15/16.
 */
public class EliteDailyPresenter implements EliteDailyContract.Presenter {
    private static final String TAG = EliteDailyPresenter.class.getSimpleName();
    private EliteDailyContract.View mView;

    @Inject
    public EliteDailyPresenter(EliteDailyContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void onStart() {
        Logger.debug(TAG, ">>>" + "onStart");

    }
}
