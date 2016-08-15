package son.nt.en.elite.di;

import dagger.Module;
import dagger.Provides;
import son.nt.en.elite.EliteDailyContract;

/**
 * Created by sonnt on 8/15/16.
 */
@Module
public class ElitePresenterModule
{
    private EliteDailyContract.View mView;

    public ElitePresenterModule(EliteDailyContract.View mView)
    {
        this.mView = mView;
    }

    @Provides
    EliteDailyContract.View provideView ()
    {
        return mView;
    }
}
