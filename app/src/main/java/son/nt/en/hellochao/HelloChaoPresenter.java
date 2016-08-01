package son.nt.en.hellochao;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonnt on 7/14/16.
 */
public class HelloChaoPresenter implements HelloChaoContract.Presenter
{

    HelloChaoContract.View   mView;
    DatabaseReference        mDatabaseReference;
    List<HelloChaoSentences> mList;

    public HelloChaoPresenter(HelloChaoContract.View mView, DatabaseReference mDatabaseReference)
    {
        this.mView = mView;
        this.mDatabaseReference = mDatabaseReference;
    }

    public HelloChaoPresenter()
    {
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void doSearch(String keyword)
    {
        if (mList == null || mList.isEmpty())
        {
            return;
        }

        if (TextUtils.isEmpty(keyword))
        {
            mView.resultSearch (mList);
            return;
        }
        List<HelloChaoSentences> list = new ArrayList<>();
        for (HelloChaoSentences d : mList)
        {
            if (d.getText().toLowerCase().contains(keyword.toLowerCase()))
            {
                list.add(d);

            }

        }
        mView.resultSearch (list);

    }

    @Override
    public void setData(List<HelloChaoSentences> list)
    {
        this.mList = list;

    }
}
