package io.reist.sandbox.cryptocurrency;

import dagger.Subcomponent;
import io.reist.sandbox.cryptocurrency.view.CryptoCurrencyFragment;

/**
 * Created by Sergey on 02/11/2017.
 */

@Subcomponent
public interface CryptoCurrencyComponent {

    void inject(CryptoCurrencyFragment cryptoCurrencyFragment);

}