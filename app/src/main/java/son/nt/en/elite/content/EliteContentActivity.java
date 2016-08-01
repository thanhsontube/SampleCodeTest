package son.nt.en.elite.content;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import son.nt.en.R;

public class EliteContentActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elite_content);
        Parcelable id = getIntent().getParcelableExtra("data");
        getSupportFragmentManager().beginTransaction().add(R.id.main_ll, EliteContentFragment.newInstance(id), null).commit();
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
