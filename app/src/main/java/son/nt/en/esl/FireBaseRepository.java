package son.nt.en.esl;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import son.nt.en.FireBaseConstant;
import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 7/29/16.
 */
public class FireBaseRepository implements EslDailyContract.IRepository
{

    public static final String TAG = FireBaseRepository.class.getSimpleName();

    DatabaseReference                 mDatabaseReference;
    List<EslDailyDto> mList = new ArrayList<>();

    Observer<List<EslDailyDto>> observer;

    public FireBaseRepository(DatabaseReference mDatabaseReference
                   )
    {
        this.mDatabaseReference = mDatabaseReference;
    }



    @Override
    public void getData(Observer<List<EslDailyDto>> observer) {
        mDatabaseReference.child(FireBaseConstant.TABLE_ESL_DAILY).addValueEventListener(valueEventListener);
        this.observer = observer;
    }



    @Override
    public Observable<List<EslDailyDto>> doSearch2(String keyword) {
        Logger.debug(TAG, ">>>" + "doSearch2:" + keyword);

        if (TextUtils.isEmpty(keyword))
        {
            return Observable.just(mList);
        }

        //search
        List<EslDailyDto> list = new ArrayList<>();
        for (EslDailyDto d : mList)
        {
            if (d.getHomeTitle().toLowerCase().contains(keyword.toLowerCase())
                    || d.getHomeDescription().toLowerCase().contains(keyword.toLowerCase()))
            {
                list.add(d);

            }

        }
        return Observable.just(list);
    }


    ValueEventListener valueEventListener = new ValueEventListener()
    {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            mList.clear();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                EslDailyDto post = postSnapshot.getValue(EslDailyDto.class);
                mList.add(post);

            }

            /**
             * get result at {@link EslDailyPresenter#observer}
             */

            observer.onNext(mList);
//            listPublishSubject.onNext(list);

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            observer.onError(new Throwable(databaseError.toException()));

        }
    };
}
