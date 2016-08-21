package son.nt.en.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;

/**
 * Created by sonnt on 8/21/16.
 */
public class FeedFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        //DI

        //listener
        return view;
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();

    }
}
