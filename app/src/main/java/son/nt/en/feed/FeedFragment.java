package son.nt.en.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.elite.AdapterEliteDaily;
import son.nt.en.esl.AdapterEslDaily;
import son.nt.en.feed.di.DaggerFeedComponent;
import son.nt.en.feed.di.FeedPresenterModule;

/**
 * Created by sonnt on 8/21/16.
 */
public class FeedFragment extends BaseFragment implements FeedContract.View {
    @BindView(R.id.feed_rcv_daily_listening)
    RecyclerView mRecyclerViewListening;
    AdapterEslDaily mAdapter;

    //reading
    @BindView(R.id.feed_rcv_reading)
    RecyclerView mRecyclerViewElite;
    AdapterEliteDaily mAdapterEliteDaily;

    @Inject
    FeedPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        //DI
        DaggerFeedComponent.builder().feedPresenterModule(new FeedPresenterModule(this)).build();


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewListening.setLayoutManager(mLinearLayoutManager);
        mAdapter = new AdapterEslDaily(getActivity());

        mRecyclerViewListening.setAdapter(mAdapter);

        mPresenter.onStart();

        //listener
        return view;
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();

    }
}
