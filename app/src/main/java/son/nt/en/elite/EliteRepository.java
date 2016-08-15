package son.nt.en.elite;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

/**
 * Created by sonnt on 8/15/16.
 */
public class EliteRepository implements ContractDailyElite.Repository
{

    private DatabaseReference mDatabaseReference;

    @Inject
    public EliteRepository(DatabaseReference mDatabaseReference)
    {
        this.mDatabaseReference = mDatabaseReference;
    }

    @Override
    public void getData()
    {

    }
}
