package com.example.pavel.handymerchant;

/**
 * Created by pavel on 10.02.18.
 */

public enum Account {

    INSTANCE;

    private long balance;

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void buy(long price) {
        balance = balance - price;
    }

    public void sell(long price) {
        balance = balance + price;
    }
}
