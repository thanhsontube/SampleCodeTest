package son.nt.en.test.dagger2;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by sonnt on 8/11/16.
 */
public class LamCaFeJob {

    private Lazy<IMayLamNong> mayLamNongLazy;
    private IMayBom maybom;

    @Inject
    LamCaFeJob(Lazy<IMayLamNong> heaterLazy, IMayBom pump) {
        this.mayLamNongLazy = heaterLazy;
        this.maybom = pump;
    }

    public void lam1LyCafe()
    {
        mayLamNongLazy.get().on();
        maybom.layCafe();
        mayLamNongLazy.get().off();
    }
}
