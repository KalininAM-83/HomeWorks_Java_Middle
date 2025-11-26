package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс org.example.ATM")
public class ATMTest {
    private ATM atm;

    @BeforeEach
    void setUp() {
        atm = new ATM();
    }

    @DisplayName("Принимает банкноты разных номиналов одной операцией")
    @Test
    void testDepositCashValid() {
        Map<Integer, Integer> banknotes = new HashMap<>();
        banknotes.put(1000, 10);
        banknotes.put(5000, 11);
        banknotes.put(2000, 20);
        banknotes.put(500, 50);

        assertDoesNotThrow(() -> atm.depositCash(banknotes));

        Map<Integer, Integer> balance = atm.getDetailedBalance();
        assertEquals(10, balance.get(1000));
        assertEquals(11, balance.get(5000));
        assertEquals(20, balance.get(2000));
        assertEquals(50, balance.get(500));
    }

    @DisplayName("Вносим нулевое количество банкнот")
    @Test
    void testDepositCashZeroBanknotes() {
        Map<Integer, Integer> banknotes = new HashMap<>();
        banknotes.put(1000, 0);
        banknotes.put(5000, 0);
        banknotes.put(2000, 0);
        banknotes.put(500, 0);

        assertThrows(IllegalArgumentException.class, () -> atm.depositCash(banknotes));
    }
    @DisplayName("Выдаем сумму 15000 меньшим количеством банкнот")
    @Test
    void testWithdrawCashSuccess() throws ATM.InsufficientFundsException {
        Map<Integer, Integer> banknotes = new HashMap<>();
        banknotes.put(1000, 10);
        banknotes.put(5000, 5);
        atm.depositCash(banknotes);

        Map<Integer, Integer> result = atm.withdrawCash(15000);

        assertEquals(3, result.get(5000));
        assertEquals(20000, atm.getBalance());
    }

    @DisplayName("Невозможно выдать запрашиваемую сумму")
    @Test
    void testWithdrawCashInsufficientFunds() {
        Map<Integer, Integer> banknotes = new HashMap<>();
        banknotes.put(1000, 10);
        atm.depositCash(banknotes);

        ATM.InsufficientFundsException exception = assertThrows(
                ATM.InsufficientFundsException.class,
                () -> atm.withdrawCash(15000)
        );
        assertTrue(exception.getMessage().contains("Невозможно выдать запрошенную сумму"));
    }

    @DisplayName("Проверяем баланс после внесения банкнот")
    @Test
    void testGetBalance() {
        Map<Integer, Integer> banknotes = new HashMap<>();
        banknotes.put(1000, 1);
        banknotes.put(500, 1);

        atm.depositCash(banknotes);

        assertEquals(1500, atm.getBalance());
    }

    @DisplayName("Проверка детализированного баланса по банкнотам")
    @Test
    void testGetDetailedBalance() {
        Map<Integer, Integer> banknotes = new HashMap<>();
        banknotes.put(1000, 10);
        banknotes.put(500, 20);
        banknotes.put(2000, 15);
        banknotes.put(5000, 3);

        atm.depositCash(banknotes);

        Map<Integer, Integer> balance = atm.getDetailedBalance();
        assertEquals(10, balance.get(1000));
        assertEquals(20, balance.get(500));
        assertEquals(15, balance.get(2000));
        assertEquals(3, balance.get(5000));
    }
}
