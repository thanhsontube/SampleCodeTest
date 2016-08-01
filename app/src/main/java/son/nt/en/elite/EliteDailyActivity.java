package son.nt.en.elite;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import son.nt.en.FireBaseConstant;
import son.nt.en.R;
import son.nt.en.base.BaseActivity;
import son.nt.en.elite.content.EliteContentActivity;
import son.nt.en.otto.OttoBus;
import son.nt.en.utils.Logger;

public class EliteDailyActivity extends BaseActivity
{
    public static final String TAG = EliteDailyActivity.class.getSimpleName();
    private RecyclerView       mMessageRecyclerView;
    private GridLayoutManager  mLinearLayoutManager;
    private ProgressBar        mProgressBar;

    @BindView(R.id.CoordinatorLayoutChat)
    CoordinatorLayout          mCoordinatorLayout;

    DatabaseReference          mFirebaseDatabaseReference;

    private AdapterEliteDaily  mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);
        setContentView(R.layout.activity_elite_daily);

        getSupportActionBar().setTitle("Reading");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new GridLayoutManager(this, 1);
        //        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new AdapterEliteDaily(this);

        //        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child(FireBaseConstant.TABLE_ELITE_DAILY).addValueEventListener(valueEventListener);

        mMessageRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        OttoBus.unRegister(this);
        super.onDestroy();
    }

    ValueEventListener valueEventListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            List<EliteDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                EliteDto post = postSnapshot.getValue(EliteDto.class);
                list.add(post);
            }
            Logger.debug(TAG, ">>>" + "list:" + list.size());

            mAdapter.setData(list);
            mMessageRecyclerView.scrollToPosition(0);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        }
    };

    @Subscribe
    public void getClick (BusElite busElite)
    {
        EliteDto eliteDto = mAdapter.mValues.get(busElite.pos);
        Intent intent = new Intent(this, EliteContentActivity.class);
        intent.putExtra("data", Parcels.wrap(eliteDto));
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
