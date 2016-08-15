package son.nt.en.di;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sonnt on 8/15/16.
 */
@Singleton
@Component (modules = AppModule.class)
public interface AppComponent {
    DatabaseReference getDatabaseReference();
}
