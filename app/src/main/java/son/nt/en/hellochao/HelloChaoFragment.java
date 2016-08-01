package son.nt.en.hellochao;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.en.FireBaseConstant;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.otto.OttoBus;
import son.nt.en.service.GoPlayer;
import son.nt.en.service.MusicService;
import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 7/11/16.
 */
public class HelloChaoFragment extends BaseFragment implements HelloChaoContract.View, View.OnClickListener, TextWatcher
{

    public static final String  TAG    = HelloChaoFragment.class.getSimpleName();
    private RecyclerView        mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar         mProgressBar;

    @BindView(R.id.CoordinatorLayoutChat)
    CoordinatorLayout           mCoordinatorLayout;

    @BindView(R.id.txt_search)
    EditText                    txtSearch;
    @BindView(R.id.btn_clear)
    View                        btnClear;


    @BindView(R.id.player_play)
    ImageView mImgPlay;
    @BindView(R.id.img_track)
    ImageView mImgTrack;

    @BindView(R.id.txt_title)
    TextView mTxtTitle;

    @BindView(R.id.txt_des)
    TextView mTxtDes;

    DatabaseReference           mFirebaseDatabaseReference;

    private AdapterHelloChao    mAdapter;

    boolean                     isBind = false;

    MusicService mPlayService;

    SearchHandler               mSearchHandler;

    HelloChaoContract.Presenter mPresenter;

    public static HelloChaoFragment newInstace()
    {
        HelloChaoFragment f = new HelloChaoFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);
        MusicService.bindToMe(getContext(), serviceConnectionPlayer);
        mPresenter = new HelloChaoPresenter(this, FirebaseDatabase.getInstance().getReference());
    }

    @Override
    public void onDestroy()
    {
        OttoBus.unRegister(this);
        getActivity().unbindService(serviceConnectionPlayer);
        if (mSearchHandler != null)
        {
            mSearchHandler.removeMessage();
            mSearchHandler = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                    @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_hello_chao, container, false);
        ButterKnife.bind(this, view);
        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);

        //search
        btnClear.setOnClickListener(this);
        txtSearch.addTextChangedListener(this);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new AdapterHelloChao(getActivity());

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child(FireBaseConstant.TABLE_HELLO_CHAO).orderByChild("text")
                        .addValueEventListener(valueEventListener);

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mAdapter);

        //onclick
        mImgPlay.setOnClickListener(this);

        return view;

    }

    ServiceConnection serviceConnectionPlayer = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicService.LocalBinder localBinder = (MusicService.LocalBinder) service;
            mPlayService = localBinder.getService();
            isBind = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            isBind = false;
            mPlayService = null;
        }
    };

    @Subscribe
    public void getSelection(BusSentence busSentence)
    {
        if (mPlayService != null)
        {
            mPlayService.setDataToService(mAdapter.mValues);
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
        mTxtTitle.setText(goPlayer.title);
        mTxtDes.setText(goPlayer.des);
        mImgPlay.setImageResource(goPlayer.command == GoPlayer.DO_PLAY ? R.drawable.icon_paused : R.drawable.icon_played);
        if (goPlayer.image != null)
        {
            Glide.with(this).load(goPlayer.image).fitCenter().into(mImgTrack);
        }

    }

    ValueEventListener valueEventListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Logger.debug(TAG, ">>>" + " valueEventListener onDataChange");
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            List<HelloChaoSentences> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                HelloChaoSentences post = postSnapshot.getValue(HelloChaoSentences.class);
                list.add(post);
            }

            mAdapter.setData(list);
            mPresenter.setData(list);
            mMessageRecyclerView.scrollToPosition(0);
            if (mPlayService != null)
            {
                mPlayService.setDataToService(list);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        }
    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_clear:
            {
                txtSearch.setText("");
                break;
            }
            case R.id.player_play:
            {
                if (mPlayService != null)
                {
                    mPlayService.play();
                }
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        if (s.length() > 0)
        {
            btnClear.setVisibility(View.VISIBLE);
        }
        else
        {
            btnClear.setVisibility(View.GONE);
        }
        if (mSearchHandler == null)
        {
            mSearchHandler = new SearchHandler(this);
        }
        mSearchHandler.removeMessage();
        mSearchHandler.doSearch();

    }

    private void doSearch()
    {
        String keyword = txtSearch.getText().toString();
        Logger.debug(TAG, ">>>" + "doSearch:" + keyword);
        mPresenter.doSearch(keyword);

    }

    @Override
    public void resultSearch(List<HelloChaoSentences> list)
    {
        mAdapter.setData(list);

    }

    private static class SearchHandler extends Handler
    {
        public static final int          WHAT_SEARCH                    = 1;

        WeakReference<HelloChaoFragment> helloChaoFragmentWeakReference = new WeakReference<HelloChaoFragment>(null);

        public SearchHandler(HelloChaoFragment helloChaoFragment)
        {
            helloChaoFragmentWeakReference = new WeakReference<HelloChaoFragment>(helloChaoFragment);

        }

        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case WHAT_SEARCH:
                {
                    HelloChaoFragment f = helloChaoFragmentWeakReference.get();
                    if (f != null)
                    {
                        f.doSearch();
                    }
                    break;
                }

            }
        }

        public void removeMessage()
        {
            removeMessages(WHAT_SEARCH);
        }

        public void doSearch()
        {
            sendEmptyMessageDelayed(WHAT_SEARCH, 750);
        }
    }

}
