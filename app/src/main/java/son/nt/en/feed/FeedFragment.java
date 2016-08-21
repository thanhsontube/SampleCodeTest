package son.nt.en.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.esl.AdapterEslDaily;

/**
 * Created by sonnt on 8/21/16.
 */
public class FeedFragment extends BaseFragment {
    @BindView(R.id.feed_rcv_daily_listening)
    RecyclerView mRecyclerViewListening;
    AdapterEslDaily mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        //DI


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewListening.setLayoutManager(mLinearLayoutManager);
        mAdapter = new AdapterEslDaily(getActivity());

        mRecyclerViewListening.setAdapter(mAdapter);

        //listener
        return view;
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();

    }
}
