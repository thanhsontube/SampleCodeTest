package son.nt.en.elite.content;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.en.R;
import son.nt.en.base.BaseFragment;
import son.nt.en.elite.EliteContentDto;
import son.nt.en.elite.EliteDto;
import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 7/20/16.
 */
public class EliteContentFragment extends BaseFragment
{
    private static final String TAG = EliteContentFragment.class.getSimpleName();
    DatabaseReference mFirebaseDatabaseReference;
    EliteDto eliteDto;
    TextView mTxtContent;
    @BindView(R.id.backdrop) ImageView imgBackground;
    @BindView(R.id.authorPic) ImageView authorPic;
    @BindView(R.id.title) TextView txtTitle;
    @BindView(R.id.author) TextView txtAuthor;
    @BindView(R.id.des) TextView des;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;


    public static EliteContentFragment newInstance(Parcelable dto)
    {
        EliteContentFragment f = new EliteContentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", dto);
        f.setArguments(bundle);
        return f;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                    @Nullable Bundle savedInstanceState)

    {
        View view = inflater.inflate(R.layout.fragment_elite_content, container, false);
        ButterKnife.bind(this, view);
        eliteDto = Parcels.unwrap(getArguments().getParcelable("data"));

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(eliteDto.des);
//        setHasOptionsMenu(true);

        Glide.with(this).load(eliteDto.image).into(imgBackground);
        Glide.with(this).load(eliteDto.authPic).into(authorPic);
        txtTitle.setText(eliteDto.title);
        txtAuthor.setText(eliteDto.authName);
        des.setText(eliteDto.des);
        mProgressBar.setVisibility(View.GONE);
        mTxtContent = (TextView) view.findViewById(R.id.content);
        mTxtContent.setText(eliteDto.content);
//
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        Query query = mFirebaseDatabaseReference.child(FireBaseConstant.TABLE_ELITE_Content).orderByChild("id").equalTo(eliteDto.id).limitToLast(20);
//        query.addValueEventListener(valueEventListener);
        return view;

    }

    ValueEventListener valueEventListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
//            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            List<EliteContentDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                EliteContentDto post = postSnapshot.getValue(EliteContentDto.class);
                list.add(post);
            }
            Logger.debug(TAG, ">>>" + "list:" + list.size());
            EliteContentDto dto = list.get(0);
            mTxtContent.setText(dto.content);
            mProgressBar.setVisibility(View.GONE);

//            mAdapter.setData(list);
//            mMessageRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            mProgressBar.setVisibility(View.GONE);
            mTxtContent.setText("Sorry ! Can not load data.");
        }
    };


}
