package son.nt.en.esl;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.otto.OttoBus;
import son.nt.en.service.GoPlayer;
import son.nt.en.service.MusicService;

/**
 * Created by sonnt on 7/15/16.
 */
public class EslDaiLyFragment extends BaseFragment implements View.OnClickListener, TextWatcher, EslDailyContract.View
{
    public static final String TAG    = EslDaiLyFragment.class.getSimpleName();
    private RecyclerView       mMessageRecyclerView;
    private ProgressBar        mProgressBar;

    @BindView(R.id.CoordinatorLayoutChat)
    CoordinatorLayout          mCoordinatorLayout;

    @BindView(R.id.txt_search)
    EditText                   txtSearch;
    @BindView(R.id.btn_clear)
    View                       btnClear;

    private AdapterEslDaily    mAdapter;

    boolean                    isBind = false;

    MusicService               mPlayService;

    EslDailyContract.Presenter mPresenter;

    @BindView(R.id.player_play)
    ImageView                  mImgPlay;
    @BindView(R.id.img_track)
    ImageView                  mImgTrack;

    @BindView(R.id.txt_title)
    TextView                   mTxtTitle;

    @BindView(R.id.txt_des)
    TextView                   mTxtDes;

    public static EslDaiLyFragment newInstace()
    {
        EslDaiLyFragment f = new EslDaiLyFragment();
        return f;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);

        MusicService.bindToMe(getContext(), serviceConnectionPlayer);

        PublishSubject<List<EslDailyDto>> publishSubject2 = PublishSubject.create();
        final FireBaseRepository repo = new FireBaseRepository(FirebaseDatabase.getInstance().getReference(), publishSubject2);
        mPresenter = new EslDailyPresenter(this, repo, publishSubject2);

        mPresenter.onStart();

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
        GridLayoutManager mLinearLayoutManager = new GridLayoutManager(getActivity(), 1);
        //        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new AdapterEslDaily(getActivity());

        mMessageRecyclerView.setAdapter(mAdapter);

        //search
        btnClear.setOnClickListener(this);
        txtSearch.addTextChangedListener(this);
        mImgPlay.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroy()
    {
        OttoBus.unRegister(this);
        getActivity().unbindService(serviceConnectionPlayer);
        mPresenter.onDestroy();

        super.onDestroy();

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

    /**
     * who calls this {@link son.nt.en.esl.AdapterEslDaily.ViewHolder#ViewHolder(View)}
     *
     * @param busSentence
     */

    @Subscribe
    public void getSelection(BusEsl busSentence)
    {
        if (mPlayService != null)
        {
            mPlayService.setDataToService(mAdapter.mValues);
            mPlayService.playAtPos(busSentence.pos);
        }
    }

    /**
     * called from {@link MusicService#play()}
     *
     * @param goPlayer
     */

    @Subscribe
    public void getControlFromService(GoPlayer goPlayer)
    {
        mTxtTitle.setText(goPlayer.title);
        mTxtDes.setText(goPlayer.des);
        mImgPlay.setImageResource(
                        goPlayer.command == GoPlayer.DO_PLAY ? R.drawable.icon_paused : R.drawable.icon_played);
        if (goPlayer.image != null)
        {
            Glide.with(this).load(goPlayer.image).fitCenter().into(mImgTrack);
        }

    }

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
        mPresenter.afterTextChanged(s.toString());
    }

    @Override
    public void resultSearch(List<EslDailyDto> list)
    {
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        mAdapter.setData(list);
        mMessageRecyclerView.scrollToPosition(0);

    }

    @Override
    public void setVisibility(boolean b)
    {
        btnClear.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
