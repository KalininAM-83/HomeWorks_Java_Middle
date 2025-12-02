package org.example;
/**
 Определение нужного размера хипа

 Есть готовое приложение (модуль homework)

 Запустите его с размером хипа 256 Мб и посмотрите в логе время выполнения.
 Пример вывода: spend msec:18284, sec:18

 Нужно проанализировать производительность в зависимости от объема памяти и также попробовать оптимизировать код.
 Также необходимо оптимизировать код, не меняя логику и определить самый эффективный хип для него.
 Итог:
-Xms512m (вместо 256m)
-Xmx512m (вместо 256m)
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseParallelGC лучший по времени выполнения кода из остальных тестовых (UseG1GC / UseSerialGC / UseZGC)
*/

import java.time.LocalDateTime;

public class GcDemo {
    public static void main(String[] args) {
        long counter = 100_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        for(var idx = 0; idx < counter; idx++) {
            var data = new Data(idx);
            summator.calc(data);

            if(idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx: " + idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue());
        System.out.println(summator.getPrevPrevValue());
        System.out.println(summator.getSumLastThreeValues());
        System.out.println(summator.getSomeValue());
        System.out.println(summator.getSum());
        System.out.println("speed msec: " + delta + ", sec:" + (delta / 1000));
    }
}
