package com.talkcode.bank.fund.domainevent;

import com.talkcode.bank.fund.fundaccount.FundAccountTransferIn;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DomainEventPublisherTest {

    @Test
    public void should_publish_domain_event_to_subscriber() {
        FundAccountTransferInDomainEventSubscriber aSubscriber = new FundAccountTransferInDomainEventSubscriber();
        DomainEventPublisher.instance().subscribe(aSubscriber);

        DomainEventPublisher.instance().publish(new FundAccountTransferIn());

        assertNotNull(aSubscriber.getDomainEvent());
    }

    private static class FundAccountTransferInDomainEventSubscriber implements DomainEventSubscriber<FundAccountTransferIn> {
        private FundAccountTransferIn domainEvent;

        public Class<?> subscribedToEventType() {
            return FundAccountTransferIn.class;
        }

        public void handleEvent(FundAccountTransferIn domainEvent) {
            this.domainEvent = domainEvent;
        }

        public FundAccountTransferIn getDomainEvent() {
            return domainEvent;
        }
    }
}
