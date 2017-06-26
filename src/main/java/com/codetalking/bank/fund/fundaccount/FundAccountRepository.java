package com.codetalking.bank.fund.fundaccount;

public interface FundAccountRepository {
    FundAccount byAccountId(String fundAccountId);

    FundAccount save(FundAccount fundAccount);
}