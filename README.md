# Let's build a fund system

## Domain

1. Each customer has one fund fundAccount.
2. An fund fundAccount keeps it's balance.
3. A customer can purchase fund with a bankAccount fundAccount, and the money will be transferred from the bankAccount fundAccount to fund company's fundAccount, and the  

```PlantUml

@startuml

class Account <<(E, #00ff00) Entity>>{
 + customerId()
 + balance()
 + transferIn()
 + transferOut()
 + List<BalanceChange> balanceChanges()
}

class Customer <<(E, #00ff00) Entity>> {
 + name()
 + phone()
 + address()
}

class PurchaseTransaction {
 + customerId()
}

class BalanceChange {
 + amount()
}
Account ..> "1" Customer :belongs to
"PurchaseTransaction" ..> Customer
"PurchaseTransaction" "fund" -- Account
"PurchaseTransaction" "card" -- Account

Account -- "*" BalanceChange



@enduml


```
