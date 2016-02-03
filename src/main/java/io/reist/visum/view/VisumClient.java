package io.reist.visum.view;

/**
 * Created by defuera on 02/02/2016.
 * VisumClient is an entity which can use services
 * provided by Dagger components
 */
public interface VisumClient {

    Long getComponentId();

    void setComponentId(Long componentId);

    Object getComponent();

    void inject(Object from);

}
