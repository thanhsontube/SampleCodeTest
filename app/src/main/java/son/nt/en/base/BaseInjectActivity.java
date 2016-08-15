package son.nt.en.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by sonnt on 8/15/16.
 */
public abstract class BaseInjectActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectPresenter();

    }

    abstract public void injectPresenter ();

}
