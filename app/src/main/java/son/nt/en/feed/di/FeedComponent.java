package son.nt.en.feed.di;

import dagger.Component;
import son.nt.en.di.scoped.ActivityScoped;
import son.nt.en.feed.FeedFragment;
import son.nt.en.firebase.FireBaseModule;

/**
 * Created by sonnt on 8/22/16.
 * Inject to {@link FeedFragment#mPresenter}
 */
@ActivityScoped
@Component(modules = {FeedPresenterModule.class, FireBaseModule.class})
public interface FeedComponent {
    void inject(FeedFragment feedFragment);
}
