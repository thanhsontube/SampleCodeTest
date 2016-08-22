package son.nt.en.feed;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import son.nt.en.FireBaseConstant;
import son.nt.en.elite.EliteDto;

/**
 * Created by sonnt on 8/22/16.
 */
public class FeedRepository implements FeedContract.IRepository {


    Observer<List<EliteDto>> listObserver;

    DatabaseReference mDatabaseReference;

    @Inject
    public FeedRepository( DatabaseReference databaseReference) {
        mDatabaseReference = databaseReference;
    }
    @Override
    public void getElite(Observer<List<EliteDto>> listObserver) {
        this.listObserver = listObserver;

        Query query = mDatabaseReference.child(FireBaseConstant.TABLE_ELITE_DAILY).limitToFirst(5);
        query.addListenerForSingleValueEvent(mValueEventListener);
    }

    ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<EliteDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                EliteDto post = postSnapshot.getValue(EliteDto.class);
                list.add(post);
            }

            listObserver.onNext(list);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            List<EliteDto> list = new ArrayList<>();
            listObserver.onNext(list);
        }
    };
}
