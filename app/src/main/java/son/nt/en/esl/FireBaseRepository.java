package son.nt.en.esl;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import son.nt.en.FireBaseConstant;
import son.nt.en.utils.Logger;

/**
 * Created by sonnt on 7/29/16.
 */
public class FireBaseRepository implements EslDailyContract.IRepository
{

    public static final String  TAG   = FireBaseRepository.class.getSimpleName();

    DatabaseReference           mDatabaseReference;
    List<EslDailyDto>           mList = new ArrayList<>();

    Observer<List<EslDailyDto>> observer;

    public FireBaseRepository(DatabaseReference mDatabaseReference)
    {
        this.mDatabaseReference = mDatabaseReference;
    }

    @Override
    public void getData(Observer<List<EslDailyDto>> callback)
    {
        mDatabaseReference.child(FireBaseConstant.TABLE_ESL_DAILY).addValueEventListener(valueEventListener);
        this.observer = callback;
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
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            observer.onError(new Throwable(databaseError.toException()));

        }
    };

    @Override
    public void doSearch4(String keyword, Observer<List<EslDailyDto>> callback)
    {
        Logger.debug(TAG, ">>>" + "doSearch4:" + keyword + ";mList:" + mList.size());
        if (TextUtils.isEmpty(keyword))
        {
            callback.onNext(mList);
            return;
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
        callback.onNext(list);
    }

    @Override
    public List<EslDailyDto> doSearch3(String keyword) {
        if (TextUtils.isEmpty(keyword))
        {
            return mList;
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
        return list;
    }
}
