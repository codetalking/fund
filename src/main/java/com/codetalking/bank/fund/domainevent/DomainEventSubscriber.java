package com.codetalking.bank.fund.domainevent;

public interface DomainEventSubscriber<T> {
    Class<?> subscribedToEventType();

    void handleEvent(T domainEvent);
}
