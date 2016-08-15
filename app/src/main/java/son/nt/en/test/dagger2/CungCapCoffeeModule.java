package son.nt.en.test.dagger2;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sonnt on 8/11/16.
 */
@Module
public class CungCapCoffeeModule {

    @Provides
    public IMayLamNong cungcapMapLamNong()
    {
        return new MayLamBongDienTu();
    }

    @Provides
    public IMayBom cungcapMayBomCafe(MayBomCafe mayBomCafe)
    {
        return mayBomCafe;
    }
}
