package son.nt.en.di;

import android.app.Application;

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
}
