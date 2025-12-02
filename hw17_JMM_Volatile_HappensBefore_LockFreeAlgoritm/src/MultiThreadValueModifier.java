import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

/**
 Сделать многопоточную программу, которая уменьшает любое введенное значение на 2 500 000,
 а затем увеличивает на 3 200 000 и делает все с шагом 1.
*/

 public class MultiThreadValueModifier {
    private static final int decrease_amount = 2500000;
    private static final int increase_amount = 3200000;
    private static final int threads = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws InterruptedException {
        //пользователь вводит значение (целое число)
        Scanner scanner = new Scanner(System.in);
        out.print("Введите целое число: ");
        int initialValue = scanner.nextInt();
        scanner.close();

        //создаем атомарное значение для потокобезопасных операций
        AtomicInteger value = new AtomicInteger(initialValue);

        //уменьшение
        out.println("Уменьшаем начальное значение на " + decrease_amount);
        modifyValue(value, decrease_amount, false);
        out.println("После уменьшения значение = " + value.get());

        //увеличение
        out.println("Увеличиваем значение на " + increase_amount);
        modifyValue(value, increase_amount, true);
        out.println("После увеличения значение = " + value.get());
    }

    private static void modifyValue(AtomicInteger value, int amount, boolean isIncrease) throws InterruptedException {
        //создаем пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        //распределяем операции между потоками
        int operationsPerThread = amount / threads;
        int remainingOperations = amount % threads;

        for (int i = 0; i < threads; i++) {
            int operations = (i == threads - 1)
                    ? operationsPerThread + remainingOperations
                    : operationsPerThread;

            executor.submit(() -> {
                for (int j = 0; j < operations; j++) {
                    if (isIncrease) {
                        value.incrementAndGet(); //увеличиваем на 1
                    } else {
                        value.decrementAndGet(); //уменьшаем на 1
                    }
                }
            });
        }

        //завершаем работу потоков
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
