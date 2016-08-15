package son.nt.en.test.dagger2;

import javax.inject.Inject;

/**
 * Created by sonnt on 8/11/16.
 */
public class MayBomCafe implements IMayBom {

    IMayLamNong headers;

    @Inject
    public MayBomCafe(IMayLamNong headers) {
        this.headers = headers;
    }

    @Override
    public void layCafe() {

    }
}
