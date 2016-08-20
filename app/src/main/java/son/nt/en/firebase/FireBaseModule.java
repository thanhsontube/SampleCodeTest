package son.nt.en.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dagger.Module;
import dagger.Provides;
import son.nt.en.di.scoped.ActivityScoped;

/**
 * Created by sonnt on 8/20/16.
 */
@Module
public class FireBaseModule {

    FirebaseAuth mFirebaseAuth;

//    public FireBaseModule(FirebaseAuth firebaseAuth) {
//        mFirebaseAuth = firebaseAuth;
//    }

    @ActivityScoped
    @Provides
    FirebaseAuth provideFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }

    @ActivityScoped
    @Provides
    FirebaseUser provideFirebaseUser(FirebaseAuth firebaseAuth)
    {
        return firebaseAuth.getCurrentUser();
    }
}
