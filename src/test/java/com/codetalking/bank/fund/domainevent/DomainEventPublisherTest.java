package com.codetalking.bank.fund.domainevent;

import com.codetalking.bank.fund.bankaccount.BankAccount;
import com.codetalking.bank.fund.fundaccount.FundAccountTransferIn;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class DomainEventPublisherTest {

    @Test
    public void should_publish_domain_event_to_subscriber() {
        FundAccountTransferInDomainEventSubscriber aSubscriber = new FundAccountTransferInDomainEventSubscriber();
        DomainEventPublisher.instance().subscribe(aSubscriber);

        FundAccountTransferIn fundAccountTransferIn =
                new FundAccountTransferIn(
                        new BankAccount("bankAccount"),
                        "6666-1234-1234-1234",
                        new Date());
        DomainEventPublisher.instance().publish(fundAccountTransferIn);

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
