package org.example;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        ATM atm = new ATM();

        // Загружаем банкноты в банкомат
        try {
            Map<Integer, Integer> banknotes = new HashMap<>();
            banknotes.put(1000, -1);
            banknotes.put(5000, null);
            banknotes.put(2000, 20);
            banknotes.put(500, 10);

            atm.depositCash(banknotes);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Общий баланс: " + atm.getBalance());
        System.out.println("Детализация по банкнотам: " + atm.getDetailedBalance());

        try {
            System.out.println("Снимаем 150000: " + atm.withdrawCash(150000));
            System.out.println("Остаток после снятия: " + atm.getBalance() + " " + atm.getDetailedBalance());

            // Пытаемся снять сумму, которую нельзя выдать
            atm.withdrawCash(1250000);
        } catch (ATM.InsufficientFundsException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}