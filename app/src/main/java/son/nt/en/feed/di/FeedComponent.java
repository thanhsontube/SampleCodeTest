package son.nt.en.feed.di;

import dagger.Component;
import son.nt.en.di.scoped.ActivityScoped;
import son.nt.en.feed.FeedFragment;

/**
 * Created by sonnt on 8/22/16.
 * Inject to {@link FeedFragment#mPresenter}
 */
@ActivityScoped
@Component(modules = {FeedPresenterModule.class})
public interface FeedComponent {
    void inject(FeedFragment feedFragment);
}
