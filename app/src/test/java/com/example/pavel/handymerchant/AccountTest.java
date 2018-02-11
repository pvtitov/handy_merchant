package com.example.pavel.handymerchant;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by pavel on 10.02.18.
 */

public class AccountTest {

    @Test
    public void spend_money_when_buy(){
        long balanceBeforeChange = Account.INSTANCE.getBalance();
        Account.INSTANCE.buy(1);
        assertTrue(Account.INSTANCE.getBalance() == (balanceBeforeChange - 1));
    }

    @Test
    public void earn_money_when_sell(){
        long balanceBeforeChange = Account.INSTANCE.getBalance();
        Account.INSTANCE.sell(1);
        assertTrue(Account.INSTANCE.getBalance() == (balanceBeforeChange + 1));
    }
}
