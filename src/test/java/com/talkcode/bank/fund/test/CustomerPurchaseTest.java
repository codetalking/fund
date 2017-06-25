package com.talkcode.bank.fund.test;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerPurchaseTest {

    // What to verify?
    // 1. FundAccount balance 增加
    // 2. Money Transfer Service success
    @Test
    public void should_purchase() {
        String fundAccountId = "6666-1234-1234";
        BankAccount customerBankAccount = new BankAccount("6226-1234-1234");
        BankAccount companyBankAccount = new BankAccount("8888-1234-1234");
        BigInteger amount = new BigInteger("200");
        MoneyTransferService moneyTransferService = mock(MoneyTransferService.class);
        when(moneyTransferService.transfer(customerBankAccount, companyBankAccount, amount))
                .thenReturn(true);

        PurchaseService purchaseService = new PurchaseService();
        Voucher voucher = purchaseService.purchase(customerBankAccount, fundAccountId, amount);

        assertEquals(customerBankAccount, voucher.bankAccount());
        assertEquals(fundAccountId, voucher.fundAccountId());
        FundAccountRepository fundAccountRepository = new FundAccountRepository();
        FundAccount fundAccount = fundAccountRepository.byAccountId(fundAccountId);
        assertEquals(amount, fundAccount.balance());
        verify(moneyTransferService).transfer(customerBankAccount, companyBankAccount, amount);

    }

    private class Customer {
        private final String id;
        private       String name;
        private       String phone;
        private       String address;

        Customer(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        void setAddress(String address) {
            this.address = address;
        }
    }

    private class PurchaseService {

        public Voucher purchase(BankAccount bankAccount, String fundAccount, BigInteger bigInteger) {

            return null;
        }
    }

    private class FundAccount {
        private long id;

        public long getId() {
            return id;
        }

        public BigInteger balance() {
            return null;
        }
    }

    private class BankAccount {
        private final String cardNumber;

        public BankAccount(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }

    private class Voucher {
        private long        fundAccountId;
        private BankAccount bankAccount;

        public long fundAccountId() {
            return fundAccountId;
        }

        public BankAccount bankAccount() {
            return bankAccount;
        }
    }

    private interface MoneyTransferService {
        boolean transfer(BankAccount customerBankAccount, BankAccount companyBankAccount, BigInteger amount);
    }

    private class FundAccountRepository {
        public FundAccount byAccountId(String fundAccountId) {
            return null;
        }
    }
}