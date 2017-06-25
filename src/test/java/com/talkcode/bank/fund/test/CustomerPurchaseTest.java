package com.talkcode.bank.fund.test;

import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        FundAccountRepository fundAccountRepository = new FundAccountRepository();
        fundAccountRepository.save(new FundAccount(fundAccountId));
        PurchaseService purchaseService = new PurchaseService(fundAccountRepository);
        Voucher voucher = purchaseService.purchase(customerBankAccount, fundAccountId, amount);

        assertEquals(customerBankAccount, voucher.bankAccount());
        assertEquals(fundAccountId, voucher.fundAccountId());
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

        private final FundAccountRepository fundAccountRepository;

        public PurchaseService(FundAccountRepository fundAccountRepository) {
            this.fundAccountRepository = fundAccountRepository;
        }

        public Voucher purchase(BankAccount bankAccount, String fundAccountId, BigInteger amount) {
            FundAccount fundAccount = fundAccountRepository.byAccountId(fundAccountId);
            fundAccount.transferIn(amount);
            fundAccountRepository.save(fundAccount);

            return new Voucher(bankAccount, fundAccountId, amount);
        }
    }

    private class FundAccount {
        private String id;
        private BigInteger balance;

        FundAccount(String id) {
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

    private class BankAccount {
        private final String cardNumber;

        public BankAccount(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }

    private class Voucher {
        private       String      fundAccountId;
        private final BigInteger  amount;
        private       BankAccount bankAccount;

        public Voucher(BankAccount bankAccount, String fundAccountId, BigInteger amount) {
            this.bankAccount = bankAccount;
            this.fundAccountId = fundAccountId;
            this.amount = amount;
        }

        public String fundAccountId() {
            return fundAccountId;
        }

        public BankAccount bankAccount() {
            return bankAccount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }

            Voucher voucher = (Voucher) o;

            if (fundAccountId != null ? !fundAccountId.equals(voucher.fundAccountId) : voucher.fundAccountId != null) { return false; }
            if (amount != null ? !amount.equals(voucher.amount) : voucher.amount != null) { return false; }
            return bankAccount != null ? bankAccount.equals(voucher.bankAccount) : voucher.bankAccount == null;
        }

        @Override
        public int hashCode() {
            int result = fundAccountId != null ? fundAccountId.hashCode() : 0;
            result = 31 * result + (amount != null ? amount.hashCode() : 0);
            result = 31 * result + (bankAccount != null ? bankAccount.hashCode() : 0);
            return result;
        }
    }

    private interface MoneyTransferService {
        boolean transfer(BankAccount customerBankAccount, BankAccount companyBankAccount, BigInteger amount);
    }

    private class FundAccountRepository {
        private List<FundAccount> records;
        private Map<String, FundAccount> primaryIndex;

        public FundAccountRepository() {
            records = new ArrayList<FundAccount>();
            primaryIndex = new HashMap<String, FundAccount>();
        }

        public FundAccount byAccountId(String fundAccountId) {
            return primaryIndex.get(fundAccountId);
        }

        public FundAccount save(FundAccount fundAccount) {
            FundAccount old = primaryIndex.get(fundAccount.getId());
            records.remove(old);
            primaryIndex.put(fundAccount.getId(), fundAccount);
            records.add(fundAccount);
            return fundAccount;
        }
    }
}