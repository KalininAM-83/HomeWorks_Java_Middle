package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ATM {
    private final Map<Integer, Integer> cashCassettes; // Номинал -> количество

    public ATM() {
        // Используем TreeMap для автоматической сортировки номиналов по убыванию для удобства выдачи денег
        this.cashCassettes = new TreeMap<>((a, b) -> b - a);
    }

    /**
     * Принимает банкноты разных номиналов одной операцией
     *
     * @param banknotes Map с номиналами и количеством банкнот
     */

    public void depositCash(Map<Integer, Integer> banknotes) {//принимаем деньги на счет клиента
        if (banknotes == null || banknotes.isEmpty()) {
            throw new IllegalArgumentException("Пустой список банкнот!");
        }
        for (var entry : banknotes.entrySet()) {
            int nominal = entry.getKey();
            Integer count = entry.getValue();

            if (count == null || count <= 0) {
                throw new IllegalArgumentException("Кол-во банкнот не может быть null или меньше нуля для номинала " + nominal);
            }
            if (nominal <= 0) {
                throw new IllegalArgumentException("Номинал банкноты должен быть положительным");
            }
            cashCassettes.merge(nominal, count, Integer::sum);
        }
    }

    /**
     * Выдаем запрошенную сумму минимальным количеством банкнот
     *
     * @param sum запрашиваемая сумма
     * @return Map с номиналами и количеством банкнот для выдачи
     * @throws InsufficientFundsException если сумму нельзя выдать
     */

    public Map<Integer, Integer> withdrawCash(int sum) throws InsufficientFundsException {
        if (sum <= 0) {
            throw new IllegalArgumentException("Запрашиваемая сумма должна быть положительной");
        }

        Map<Integer, Integer> result = new HashMap<>();
        int remainingSum = sum;

        // Проходим по всем номиналам от большего к меньшему
        for (var entry : cashCassettes.entrySet()) { // используем var вместо Map.Entry<Integer, Integer>
            int nominal = entry.getKey();
            int available = entry.getValue();

            if (nominal > remainingSum || available == 0) {
                continue;
            }

            int needed = remainingSum / nominal; // расчет необходимого количества каждого номинала
            int taken = Math.min(needed, available);

            if (taken > 0) { // Записываем номинал и количество в результат
                result.put(nominal, taken);
                remainingSum -= taken * nominal; // Уменьшаем оставшуюся сумму
            }

            if (remainingSum == 0) { // если всю сумму выдали - выходим из цикла
                break;
            }
        }

        if (remainingSum != 0) {
            throw new InsufficientFundsException("Невозможно выдать запрошенную сумму доступными банкнотами");
        }

        // Уменьшаем количество банкнот в кассетах
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            int nominal = entry.getKey();
            int taken = entry.getValue();
            cashCassettes.put(nominal, cashCassettes.get(nominal) - taken);
        }

        return result;
    }

    /**
     * вычисляем общий баланс (сумму всех денег) в банкомате
     *
     * @return общая сумма денег
     */

    public int getBalance() {
        return cashCassettes.entrySet().stream()
                .mapToInt(entry -> entry.getKey() * entry.getValue())
                .sum();
    }

    /**
     * вычисляем детализированный остаток по номиналам
     *
     * @return Map с номиналами и количеством банкнот
     */

    public Map<Integer, Integer> getDetailedBalance() {
        return new HashMap<>(cashCassettes);
    }

    public static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }
}
