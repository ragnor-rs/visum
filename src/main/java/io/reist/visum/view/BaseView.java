package io.reist.visum.view;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Reist on 10/15/15.
 */
public interface BaseView {

    Bundle extras();

    Context context();

    Long getComponentId();

    void setComponentId(Long componentId);

    Object getComponent();

}
