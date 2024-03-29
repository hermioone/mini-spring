package org.hermione.minis.context;

import java.util.ArrayList;
import java.util.List;

public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {
    private final List<ApplicationListener> listeners = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.listeners.add(listener);
    }
}
