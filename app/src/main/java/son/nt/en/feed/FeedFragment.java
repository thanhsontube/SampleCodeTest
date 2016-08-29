package son.nt.en.feed;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.squareup.otto.Subscribe;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.chat.FriendlyMessage;
import son.nt.en.elite.EliteDto;
import son.nt.en.esl.AdapterEslDaily;
import son.nt.en.esl.EslDailyDto;
import son.nt.en.feed.adapter.AdapterFeedElite;
import son.nt.en.feed.adapter.AdapterFeedHelloChao;
import son.nt.en.feed.di.DaggerFeedComponent;
import son.nt.en.feed.di.FeedPresenterModule;
import son.nt.en.hellochao.BusSentence;
import son.nt.en.hellochao.HelloChaoSentences;
import son.nt.en.otto.OttoBus;
import son.nt.en.service.GoPlayer;
import son.nt.en.service.MusicService;
import son.nt.en.utils.DataTimesUtil;

/**
 * Created by sonnt on 8/21/16.
 */
public class FeedFragment extends BaseFragment implements FeedContract.View {

    public static final String                                          MESSAGES_CHILD           = "messages";
    //Dagger injected fields
    @Inject
    FeedContract.Presenter mPresenter;

    @Inject
    DatabaseReference mFirebaseDatabaseReference;


    //daily hc
    @BindView(R.id.feed_rcv_daily_challenge)
    RecyclerView mRecyclerViewDailyHc;
    AdapterFeedHelloChao mAdapterDailyHc;

    //daily listening ESL
    @BindView(R.id.feed_rcv_daily_listening)
    RecyclerView mRecyclerViewListening;
    AdapterEslDaily mAdapterEsl;

    //daily reading Elite
    @BindView(R.id.feed_rcv_reading)
    RecyclerView mRecyclerViewElite;
    private AdapterFeedElite mAdapterElite;

    //conversation
    @BindView(R.id.feed_rcv_conversation)
    RecyclerView mRecyclerViewConversation;
    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder> mFirebaseAdapter;
    //media
//    @BindView(R.id.player_play)
//    ImageView mImgPlay;
//
//    @BindView(R.id.img_track)
//    ImageView mImgTrack;
//
//    @BindView(R.id.txt_title)
//    TextView mTxtTitle;
//
//    @BindView(R.id.txt_des)
//    TextView mTxtDes;

    //service
    MusicService mPlayService;

    //construction
    public static FeedFragment newInstance() {
        FeedFragment f = new FeedFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);
        createService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);

        //DI
        DaggerFeedComponent.builder().feedPresenterModule(new FeedPresenterModule(this)).build().inject(this);

        //hc
        LinearLayoutManager mLinearLayoutManagerHc = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mLinearLayoutManagerHc.setStackFromEnd(true);
        mRecyclerViewDailyHc.setLayoutManager(mLinearLayoutManagerHc);
        mAdapterDailyHc = new AdapterFeedHelloChao(getActivity());
        mRecyclerViewDailyHc.setAdapter(mAdapterDailyHc);

        //ESL
        LinearLayoutManager mLinearLayoutManagerEsl = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mLinearLayoutManagerEsl.setStackFromEnd(true);
        mRecyclerViewListening.setLayoutManager(mLinearLayoutManagerEsl);
        mAdapterEsl = new AdapterEslDaily(getActivity());
        mRecyclerViewListening.setAdapter(mAdapterEsl);

        // Elite
        LinearLayoutManager mLinearLayoutManagerElite = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRecyclerViewElite.setLayoutManager(mLinearLayoutManagerElite);
        mLinearLayoutManagerElite.setStackFromEnd(true);
        mAdapterElite = new AdapterFeedElite(getActivity());
        mRecyclerViewElite.setAdapter(mAdapterElite);

        //Conversation
        LinearLayoutManager mLinearLayoutManagerC = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        mLinearLayoutManagerC.setStackFromEnd(true);

//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        final DatabaseReference child = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        Query query = mFirebaseDatabaseReference.child(MESSAGES_CHILD).limitToLast(3);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(FriendlyMessage.class,
                R.layout.row_message, MessageViewHolder.class, query)
        {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, FriendlyMessage friendlyMessage,
                                              int position)
            {
                viewHolder.messageTextView.setText(friendlyMessage.getText());
                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                viewHolder.createdTimeTextView.setText(friendlyMessage.getCreatedTime() == null ? "Once upon a time"
                        : DataTimesUtil.getTimeAgo(friendlyMessage.getCreatedTime()));

                if (friendlyMessage.getPhotoUrl() == null)
                {
                    viewHolder.messengerImageView.setImageDrawable(
                            ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle_black_36dp));
                }
                else
                {
                    Glide.with(getContext()).load(friendlyMessage.getPhotoUrl()).into(viewHolder.messengerImageView);
                }
                Glide.with(getContext()).load("http://Img.softnyx.net/1/gb/about/rank_1.gif")
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.levelImageView);

            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManagerC.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                //œ user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1)
                        && lastVisiblePosition == (positionStart - 1)))
                {
                    mRecyclerViewConversation.scrollToPosition(positionStart);
                }
            }
        });

        mRecyclerViewConversation.setLayoutManager(mLinearLayoutManagerC);
        mRecyclerViewConversation.setAdapter(mFirebaseAdapter);


        mPresenter.onStart();

        //listener
        return view;
    }

    @Override
    public void onDestroy() {
        mPresenter.onStop();
        OttoBus.unRegister(this);
        super.onDestroy();
    }


    @Override
    public void setDailyHelloChao(List<HelloChaoSentences> helloChaoSentences) {
        mAdapterDailyHc.setData(helloChaoSentences);
    }

    @Override
    public void setEslData(List<EslDailyDto> eslDailyDtos) {
        mAdapterEsl.setData(eslDailyDtos);

    }

    @Override
    public void setEliteData(List<EliteDto> eliteDtos) {
        mAdapterElite.setData(eliteDtos);
    }

    private void createService() {
        MusicService.bindToMe(getContext(), serviceConnectionPlayer);
    }

    ServiceConnection serviceConnectionPlayer = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder localBinder = (MusicService.LocalBinder) service;
            mPlayService = localBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayService = null;
        }
    };

    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageTextView;
        public TextView        messengerTextView;
        public TextView        createdTimeTextView;
        public CircleImageView messengerImageView;
        public CircleImageView levelImageView;

        public MessageViewHolder(View v)
        {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            createdTimeTextView = (TextView) itemView.findViewById(R.id.createdTimeTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            levelImageView = (CircleImageView) itemView.findViewById(R.id.levelImageView);
        }
    }

    /**
     * {@link son.nt.en.feed.adapter.AdapterFeedHelloChao.ViewHolder#ViewHolder(View)}
     * @param busSentence
     */
    @Subscribe
    public void getSelection(BusSentence busSentence) {
        if (mPlayService != null) {
            mPlayService.setDataToService(mAdapterDailyHc.mValues);
            mPlayService.playAtPos(busSentence.pos);
        }

    }

    /**
     * called from {@link MusicService#play()}
     * @param goPlayer
     */

    @Subscribe
    public void getFromService(GoPlayer goPlayer)
    {
//        mTxtTitle.setText(goPlayer.title);
//        mTxtDes.setText(goPlayer.des);
//        mImgPlay.setImageResource(goPlayer.command == GoPlayer.DO_PLAY ? R.drawable.icon_paused : R.drawable.icon_played);
//        if (goPlayer.image != null)
//        {
//            Glide.with(this).load(goPlayer.image).fitCenter().into(mImgTrack);
//        }

    }

}
