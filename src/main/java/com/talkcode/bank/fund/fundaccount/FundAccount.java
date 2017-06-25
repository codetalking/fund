package com.talkcode.bank.fund.fundaccount;

import java.math.BigInteger;

public class FundAccount {
    private String     id;
    private BigInteger balance;

    public FundAccount(String id) {
        this.id = id;
        balance = new BigInteger("0");
    }

    public String getId() {
        return id;
    }

    public BigInteger balance() {
        return balance;
    }

    public BigInteger transferIn(BigInteger amount) {
        balance = balance.add(amount);
        return balance;
    }
}