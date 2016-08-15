package son.nt.en.elite.di;

import dagger.Component;
import son.nt.en.di.AppComponent;
import son.nt.en.di.scoped.ActivityScoped;
import son.nt.en.elite.ContractDailyElite;
import son.nt.en.elite.EliteDailyActivity;

/**
 * Created by sonnt on 8/15/16.
 * This Component support to create {@link son.nt.en.elite.EliteDailyPresenter#EliteDailyPresenter(ContractDailyElite.View, ContractDailyElite.Repository)}
 */
@ActivityScoped
@Component (dependencies = AppComponent.class, modules = ElitePresenterModule.class)
public interface EliteComponent {
    void inject (EliteDailyActivity eliteDailyActivity);
}
