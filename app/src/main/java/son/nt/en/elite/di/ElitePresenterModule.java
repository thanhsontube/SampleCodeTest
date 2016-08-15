package son.nt.en.elite.di;

import com.google.firebase.database.DatabaseReference;

import dagger.Module;
import dagger.Provides;
import son.nt.en.elite.ContractDailyElite;
import son.nt.en.elite.EliteRepository;

/**
 * Created by sonnt on 8/15/16.
 * This module is used to create {@link son.nt.en.elite.EliteDailyPresenter}
 */
@Module
public class ElitePresenterModule
{
    private ContractDailyElite.View mView;

    public ElitePresenterModule(ContractDailyElite.View mView)
    {
        this.mView = mView;
    }

    @Provides
    ContractDailyElite.View provideView ()
    {
        return mView;
    }

    @Provides
    ContractDailyElite.Repository provideRepository (DatabaseReference databaseReference)
    {
        return new EliteRepository(databaseReference);
    }
}
