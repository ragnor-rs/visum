package io.reist.visum.view;

/**
 * Created by defuera on 02/02/2016.
 */
public interface VisumDI {

    Long getComponentId();

    void setComponentId(Long componentId);

    Object getComponent();

    void inject(Object from);

}
