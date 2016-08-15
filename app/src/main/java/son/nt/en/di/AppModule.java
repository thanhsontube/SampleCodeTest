package son.nt.en.di;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sonnt on 8/15/16.
 */
@Module
public class AppModule
{
    Application mApplication;

    public AppModule(Application mApplication)
    {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideMyApplication()
    {
        return mApplication;
    }

    @Provides
    @Singleton
    DatabaseReference provideDatabaseReference()
    {
        return FirebaseDatabase.getInstance().getReference();
    }
}
