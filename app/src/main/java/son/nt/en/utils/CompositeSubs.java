package son.nt.en.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sonnt on 8/26/16.
 */
public class CompositeSubs {
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public CompositeSubs() {
        //Do nothing
    }

    public void add(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void removeAll() {
        if (!mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
