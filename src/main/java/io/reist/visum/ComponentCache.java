package io.reist.visum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import io.reist.visum.view.BaseView;

/**
 * ComponentCache let you to create and destroy components manually
 * thereby providing custom scopes for yor view.
 * In other words this is scope manager for your views.
 */
public abstract class ComponentCache {

    private final AtomicLong idSequence = new AtomicLong();

    private final Map<Long, Object> componentMap = new HashMap<>();

    /**
     * Retrieves component from cache for given view or
     * creates a new component
     * @param view - BaseView with provides an identifier to retrieve component from cache
     */
    public Object getComponentFor(BaseView view) {

        Long componentId = view.getComponentId();

        if (componentId == null) {
            componentId = idSequence.incrementAndGet();
            view.setComponentId(componentId);
        }

        Object component = componentMap.get(componentId);

        if (component == null) {
            component = buildComponentFor(view.getClass());
            componentMap.put(componentId, component);
        }

        return component;

    }

    /**
     * Util method for creating new view component, every Component should be registered here
     */
    protected abstract Object buildComponentFor(Class<? extends BaseView> viewClass);

    /**
     * Destroys component for the given view.
     * If there's no component for the view nothing will happen.
     */
    public void invalidateComponentFor(BaseView view) {
        Long componentId = view.getComponentId();
        if (componentId == null) {
            return;
        }
        componentMap.remove(componentId);
    }

}
