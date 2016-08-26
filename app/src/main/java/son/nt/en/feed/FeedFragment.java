package son.nt.en.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.elite.EliteDto;
import son.nt.en.esl.AdapterEslDaily;
import son.nt.en.esl.EslDailyDto;
import son.nt.en.feed.adapter.AdapterFeedElite;
import son.nt.en.feed.adapter.AdapterFeedHelloChao;
import son.nt.en.feed.di.DaggerFeedComponent;
import son.nt.en.feed.di.FeedPresenterModule;
import son.nt.en.hellochao.HelloChaoSentences;

/**
 * Created by sonnt on 8/21/16.
 */
public class FeedFragment extends BaseFragment implements FeedContract.View {

    //Dagger injected fields
    @Inject
    FeedContract.Presenter mPresenter;


    //daily hc
    @BindView(R.id.feed_rcv_daily_challenge)
    RecyclerView mRecyclerViewDailyHc;
    AdapterFeedHelloChao mAdapterDailyHc;

    //daily listening
    @BindView(R.id.feed_rcv_daily_listening)
    RecyclerView mRecyclerViewListening;
    AdapterEslDaily mAdapter;

    //daily reading
    @BindView(R.id.feed_rcv_reading)
    RecyclerView mRecyclerViewElite;


    private AdapterFeedElite mAdapterFeedElite;

    //construction
    public static FeedFragment newInstance() {
        FeedFragment f = new FeedFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        //DI
        DaggerFeedComponent.builder().feedPresenterModule(new FeedPresenterModule(this)).build().inject(this);

        LinearLayoutManager mLinearLayoutManagerHc = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mLinearLayoutManagerHc.setStackFromEnd(false);
        mRecyclerViewDailyHc.setLayoutManager(mLinearLayoutManagerHc);
        mAdapterDailyHc = new AdapterFeedHelloChao(getActivity());
        mRecyclerViewDailyHc.setAdapter(mAdapterDailyHc);

        LinearLayoutManager mLinearLayoutManagerListening = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewListening.setLayoutManager(mLinearLayoutManagerListening);
        mAdapter = new AdapterEslDaily(getActivity());
        mRecyclerViewListening.setAdapter(mAdapter);

        // reading box
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRecyclerViewElite.setLayoutManager(mLinearLayoutManager);
        mAdapterFeedElite = new AdapterFeedElite(getActivity());
        mRecyclerViewElite.setAdapter(mAdapterFeedElite);

        mPresenter.onStart();

        //listener
        return view;
    }

    @Override
    public void onDestroy() {
        mPresenter.onStop();
        super.onDestroy();
    }

    @Override
    public void setEliteData(List<EliteDto> eliteDtos) {
        mAdapterFeedElite.setData(eliteDtos);
        if (mAdapterFeedElite.getItemCount() > 0) {
            mRecyclerViewElite.smoothScrollToPosition(0);
        }
    }

    @Override
    public void setDailyHelloChao(List<HelloChaoSentences> helloChaoSentences) {
        mAdapterDailyHc.setData(helloChaoSentences);
        if (mAdapterDailyHc.getItemCount() > 0) {
            mRecyclerViewDailyHc.smoothScrollToPosition(0);
        }
    }

    @Override
    public void setEslData(List<EslDailyDto> eslDailyDtos) {
        mAdapter.setData(eslDailyDtos);
        if (mAdapter.getItemCount() > 0) {
            mRecyclerViewListening.smoothScrollToPosition(0);
        }
    }
}
