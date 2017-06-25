package com.talkcode.bank.fund.bankaccount;

public class BankAccount {
    private final String cardNumber;

    public BankAccount(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        BankAccount that = (BankAccount) o;

        return cardNumber != null ? cardNumber.equals(that.cardNumber) : that.cardNumber == null;
    }

    @Override
    public int hashCode() {
        return cardNumber != null ? cardNumber.hashCode() : 0;
    }
}
