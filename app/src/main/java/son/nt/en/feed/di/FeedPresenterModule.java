package son.nt.en.feed.di;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;
import son.nt.en.di.scoped.ActivityScoped;
import son.nt.en.feed.FeedContract;
import son.nt.en.feed.FeedRepository;

/**
 * Created by sonnt on 8/15/16.
 * This module is used to create {@link son.nt.en.feed.FeedPresenter}
 */
@Module
public class FeedPresenterModule
{
    private FeedContract.View mView;

    public FeedPresenterModule(FeedContract.View mView)
    {
        this.mView = mView;
    }

    @Provides
    @ActivityScoped
    FeedContract.View provideView ()
    {
        return mView;
    }

    @ActivityScoped
    @Provides
    DatabaseReference provideDatabaseReference()
    {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    @ActivityScoped
    FeedContract.IRepository provideRepository (DatabaseReference databaseReference)
    {
        return new FeedRepository(databaseReference);
    }
}
