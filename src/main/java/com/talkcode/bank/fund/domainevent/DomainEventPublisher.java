package com.talkcode.bank.fund.domainevent;

import java.util.ArrayList;
import java.util.List;

public class DomainEventPublisher {
    public static DomainEventPublisher instance() {
        return new DomainEventPublisher();
    }

    private static final ThreadLocal<List> subscribers = new ThreadLocal<List>();
    private static final ThreadLocal<Boolean> publishing = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public <T extends DomainEvent> void publish(T domainEvent) {
        if (publishing.get()) {
            return;
        }
        try {
            publishing.set(Boolean.TRUE);
            doPublish(domainEvent);
        } finally {
            publishing.set(Boolean.FALSE);
        }

    }

    @SuppressWarnings("unchecked")
    private <T> void doPublish(T domainEvent) {
        List<DomainEventSubscriber<T>> registeredSubscribers = subscribers.get();
        if (registeredSubscribers == null) {
            return;
        }

        Class<?> eventType = domainEvent.getClass();
        for(DomainEventSubscriber<T> subscriber : registeredSubscribers) {
            Class<?> subscribedTo = subscriber.subscribedToEventType();
            if (subscribedTo == eventType ||
                    subscribedTo == DomainEvent.class) {
                subscriber.handleEvent(domainEvent);
            }
        }
    }

    public <T> void subscribe(DomainEventSubscriber<T> aSubscriber) {
        if (publishing.get()) {
            return;
        }

        List<DomainEventSubscriber<T>> registeredSubscribers = subscribers.get();
        if (registeredSubscribers == null) {
            registeredSubscribers = new ArrayList<DomainEventSubscriber<T>>();
            subscribers.set(registeredSubscribers);
        }

        registeredSubscribers.add(aSubscriber);
    }
}
