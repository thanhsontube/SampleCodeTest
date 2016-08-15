package son.nt.en;

import android.app.Application;
import android.content.Intent;

import son.nt.en.service.MusicService;

/**
 * Created by sonnt on 7/15/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        dagger2();
        startService(new Intent(getApplicationContext(), MusicService.class));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(new Intent(getApplicationContext(), MusicService.class));
    }

    private void dagger2()
    {

    }
}
