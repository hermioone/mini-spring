package org.hermione.minis.context;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}