package son.nt.en.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.en.R;
import son.nt.en.test.dagger2.CoffeeShop;
import son.nt.en.test.dagger2.DaggerCoffeeShop;

/**
 * Created by sonnt on 8/11/16.
 */
public class Dagger2Activity extends AppCompatActivity implements View.OnClickListener
{

    public static final String TAG = Dagger2Activity.class.getSimpleName();
    @BindView(R.id.btn1)
    Button                     btn1;
    @BindView(R.id.btn2)
    TextView                   btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2);
        ButterKnife.bind(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn1:
            {
                coffee();
                break;
            }
            case R.id.btn2:
            {
                break;
            }
        }
    }

    private void coffee ()
    {
        CoffeeShop coffeeShop = DaggerCoffeeShop.builder().build();
        coffeeShop.maker().lam1LyCafe();
    }

}
