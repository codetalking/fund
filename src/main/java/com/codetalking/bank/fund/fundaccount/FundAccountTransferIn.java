package com.codetalking.bank.fund.fundaccount;

import com.codetalking.bank.fund.bankaccount.BankAccount;
import com.codetalking.bank.fund.domainevent.DomainEvent;

import java.util.Date;

public class FundAccountTransferIn implements DomainEvent {
    private final BankAccount bankAccount;
    private final String fundAccountId;
    private final Date occuredAt;

    public FundAccountTransferIn(BankAccount bankAccount, String fundAccountId, Date occuredAt) {
        this.bankAccount = bankAccount;
        this.fundAccountId = fundAccountId;
        this.occuredAt = occuredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        FundAccountTransferIn that = (FundAccountTransferIn) o;

        if (bankAccount != null ? !bankAccount.equals(that.bankAccount) : that.bankAccount != null) { return false; }
        if (fundAccountId != null ? !fundAccountId.equals(that.fundAccountId) : that.fundAccountId != null) { return false; }
        return occuredAt != null ? occuredAt.equals(that.occuredAt) : that.occuredAt == null;
    }

    @Override
    public int hashCode() {
        int result = bankAccount != null ? bankAccount.hashCode() : 0;
        result = 31 * result + (fundAccountId != null ? fundAccountId.hashCode() : 0);
        result = 31 * result + (occuredAt != null ? occuredAt.hashCode() : 0);
        return result;
    }

    public BankAccount bankAccount() {
        return bankAccount;
    }

    public String fundAccountId() {
        return fundAccountId;
    }
}
