package son.nt.en.chat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.login.LoginActivity;
import son.nt.en.utils.DataTimesUtil;
import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 7/11/16.
 */
public class ChatFragment extends BaseFragment implements ChatContract.View
{

    public static final String                                          FRIENDLY_MSG_LENGTH      = "friendly_msg_length";
    public static final int                                             REQUEST_LOGIN            = 1000;
    private static final String                                         TAG                      = ChatFragment.class
                    .getSimpleName();
    // Firebase instance variables
    private DatabaseReference                                           mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder> mFirebaseAdapter;

    public static final String                                          MESSAGES_CHILD           = "messages";
    private static final int                                            REQUEST_INVITE           = 1;
    public static final int                                             DEFAULT_MSG_LENGTH_LIMIT = 10;
    public static final String                                          ANONYMOUS                = "anonymous";
    private static final String                                         MESSAGE_SENT_EVENT       = "message_sent";
    private String                                                      mUsername;
    private String                                                      mPhotoUrl;
    private SharedPreferences                                           mSharedPreferences;
    private GoogleApiClient                                             mGoogleApiClient;

    private Button                                                      mSendButton;
    private RecyclerView                                                mMessageRecyclerView;
    private LinearLayoutManager                                         mLinearLayoutManager;
    private ProgressBar                                                 mProgressBar;
    private EditText                                                    mMessageEditText;

    @BindView(R.id.CoordinatorLayoutChat)
    CoordinatorLayout                                                   mCoordinatorLayout;

    private ChatContract.Presenter                                      mPresenter;

    public static ChatFragment newInstance()
    {
        ChatFragment f = new ChatFragment();
        return f;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView        messageTextView;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPresenter = new ChatPresenter(this, FirebaseAuth.getInstance(), FirebaseDatabase.getInstance().getReference());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                    @Nullable Bundle savedInstanceState)
    {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        //        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(FriendlyMessage.class,
                        R.layout.row_message, MessageViewHolder.class, mFirebaseDatabaseReference.child(MESSAGES_CHILD))
        {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, FriendlyMessage friendlyMessage,
                            int position)
            {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
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
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                //œ user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1)
                                && lastVisiblePosition == (positionStart - 1)))
                {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText = (EditText) view.findViewById(R.id.messageEditText);
        //        mMessageEditText.setFilters(new InputFilter[]
        //        { new InputFilter.LengthFilter(mSharedPreferences.getInt(FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT)) });
        mMessageEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.toString().trim().length() > 0)
                {
                    mSendButton.setEnabled(true);
                }
                else
                {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mPresenter.sendMessage(mMessageEditText.getText().toString());
                //
                //                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername,
                //                                mPhotoUrl);
                //                mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(friendlyMessage)
                //                                .addOnFailureListener(new OnFailureListener()
                //                                {
                //                                    @Override
                //                                    public void onFailure(@NonNull Exception e)
                //                                    {
                //                                        Logger.debug(TAG, ">>>" + "onFailure:" + e.toString());
                //                                    }
                //                                });
                //                mMessageEditText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.onStart();
    }

    @Override
    public void userDoNotLogin(String s)
    {
        Snackbar.make(mSendButton, "Please login first", Snackbar.LENGTH_LONG)
                        .setAction("Login", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                startActivityForResult(new Intent(getActivity(), LoginActivity.class), REQUEST_LOGIN);

                            }
                        }).show();

    }

    @Override
    public void clearMessage(String s)
    {
        mMessageEditText.setText(s);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_LOGIN:
                {
                    Logger.debug(TAG, ">>>" + "onActivityResult REQUEST_LOGIN");
                    mPresenter.sendMessage(mMessageEditText.getText().toString());
                    break;
                }
            }
        }
    }
}
