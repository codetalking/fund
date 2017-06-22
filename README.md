# Let's build a fund system

## Domain

1. Each customer has one fund account.
2. An fund account keeps it's balance.
3. A customer can purchase fund with a bankcard account, and the money will be transferred from the bankcard account to fund company's account, and the  

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
