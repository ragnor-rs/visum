package io.reist.vui.view;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by reist on 07.04.17.
 */

public interface CompositeView extends UiElementBinder {

    void init(Context context, Bundle savedInstanceState);

}
