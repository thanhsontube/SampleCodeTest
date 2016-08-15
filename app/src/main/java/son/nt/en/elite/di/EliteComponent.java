package son.nt.en.elite.di;

import dagger.Component;
import son.nt.en.di.scoped.ActivityScoped;
import son.nt.en.elite.EliteDailyActivity;

/**
 * Created by sonnt on 8/15/16.
 */
@ActivityScoped
@Component (modules = ElitePresenterModule.class)
public interface EliteComponent {
    void inject (EliteDailyActivity eliteDailyActivity);
}
