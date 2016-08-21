package son.nt.en.google_client_api;

import dagger.Component;
import son.nt.en.HomeActivity;
import son.nt.en.di.scoped.ActivityScoped;
import son.nt.en.firebase.FireBaseModule;
import son.nt.en.login.LoginActivity;

/**
 * Created by sonnt on 8/20/16.
 */
@ActivityScoped
@Component(modules = {GoogleApiClientModule.class, FireBaseModule.class})
public interface GoogleApiComponent {
    void inject(LoginActivity loginActivity);
    void inject(HomeActivity loginActivity);

}
