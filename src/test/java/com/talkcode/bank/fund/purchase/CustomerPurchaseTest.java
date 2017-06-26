package com.talkcode.bank.fund.purchase;

import com.talkcode.bank.fund.bankaccount.BankAccount;
import com.talkcode.bank.fund.bankaccount.BankAccountFactory;
import com.talkcode.bank.fund.fundaccount.FundAccount;
import com.talkcode.bank.fund.fundaccount.FundAccountRepository;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerPurchaseTest {

    @Test
    public void should_purchase() {
        String fundAccountId = "6666-1234-1234";
        FundAccountRepository fundAccountRepository = new InMemoryFundAccountRepository();
        fundAccountRepository.save(new FundAccount(fundAccountId));

        MoneyTransferService moneyTransferService = mock(MoneyTransferService.class);
        when(moneyTransferService.transfer(any(BankAccount.class), any(BankAccount.class), any(BigInteger.class)))
                .thenReturn(true);

        BankAccount customerBankAccount = new BankAccount("6226-1234-1234");
        BigInteger amount = new BigInteger("200");

        PurchaseService purchaseService = new PurchaseService(fundAccountRepository, moneyTransferService);
        Voucher voucher = purchaseService.purchase(customerBankAccount, fundAccountId, amount);

        assertEquals(customerBankAccount, voucher.bankAccount());
        assertEquals(fundAccountId, voucher.fundAccountId());
        FundAccount fundAccount = fundAccountRepository.byAccountId(fundAccountId);
        assertEquals(amount, fundAccount.balance());
        verify(moneyTransferService).
                transfer(
                        customerBankAccount,
                        BankAccountFactory.companyBankAccount(),
                        amount);

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
        private       MoneyTransferService  moneyTransferService;

        public PurchaseService(FundAccountRepository fundAccountRepository,
                               MoneyTransferService moneyTransferService) {
            this.fundAccountRepository = fundAccountRepository;
            this.moneyTransferService = moneyTransferService;
        }

        public Voucher purchase(BankAccount bankAccount, String fundAccountId, BigInteger amount) {
            FundAccount fundAccount = fundAccountRepository.byAccountId(fundAccountId);
            fundAccount.transferIn(amount);
            fundAccountRepository.save(fundAccount);

            BankAccount companyBankAccount = BankAccountFactory.companyBankAccount();
            moneyTransferService.transfer(bankAccount, companyBankAccount, amount);
            return new Voucher(bankAccount, fundAccountId, amount);
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

    private class InMemoryFundAccountRepository implements FundAccountRepository {
        private List<FundAccount> records;
        private Map<String, FundAccount> primaryIndex;

        public InMemoryFundAccountRepository() {
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
