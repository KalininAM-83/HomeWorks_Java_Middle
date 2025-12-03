/**
Написать программу, которая будет работать с набором временных меток и выполнять следующие задачи:
Чтение данных: Программа должна читать временные метки из метода-поставщика временных меток в формате LocalDateTime.
Выполнение расчетов: На основании полученных данных необходимо определить среднюю разницу временных меток.
Параллельные вычисления: Нужно реализовать механизм параллельных вычислений с использованием CompletableFuture.
Каждая пара временных мерок должна обрабатываться в отдельном потоке.
Объединение результатов: После завершения всех вычислений результаты должны быть выведены в консоль
*/

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class Timestamp {
    //создаем тестовый метод с временными метками
    private static final Supplier<List<LocalDateTime>> timestampSupplier = () -> List.of(
            LocalDateTime.of(2021, 1, 11, 12, 11),
            LocalDateTime.of(2022, 4, 5, 1, 2),
            LocalDateTime.of(2023, 8, 30, 3, 56),
            LocalDateTime.of(2024, 12, 26, 22, 43)
    );

    private static long calculateDifference(LocalDateTime first, LocalDateTime second) {
        return Duration.between(first, second).toSeconds();
    }

    public static void main(String[] args) {
        // Получаем список временных меток
        List<LocalDateTime> timestamps = timestampSupplier.get();

        // Инициализируем список futures перед использованием
        List<CompletableFuture<Long>> futures = new ArrayList<>();

        // Обрабатываем пары временных меток
        for (int i = 0; i < timestamps.size() - 1; i++) {
            LocalDateTime first = timestamps.get(i);
            LocalDateTime second = timestamps.get(i + 1);

            // Для каждой пары создаем асинхронную задачу
            CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
                long difference = calculateDifference(first, second);
                System.out.println("Поток " + Thread.currentThread().getId() +
                        ": разница между " + first + " и " + second +
                        " составляет " + difference + " секунд");
                return difference;
            });
            futures.add(future);
        }

        // Объединяем все CompletableFuture в один
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Когда все задачи завершены, вычисляем среднюю разницу
        CompletableFuture<Long> resultFuture = allFutures.thenApply(v ->
                (long) futures.stream()
                        .mapToLong(CompletableFuture::join)
                        .average()
                        .orElse(0.0));
        try {
            // Получаем и выводим результат
            long averageDifference = resultFuture.get();
            System.out.println("\nСредняя разница временных меток составляет " + averageDifference + " секунд");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
