package son.nt.en.test.dagger2;

import dagger.Component;

/**
 * Created by sonnt on 8/11/16.
 */
@Component (modules = CungCapCoffeeModule.class)
public interface CoffeeShop {
    LamCaFeJob maker();
}
