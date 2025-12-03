/**
 Два потока печатают числа от 1 до 10, потом от 10 до 1.
 Надо сделать так, чтобы числа чередовались, т.е. получился такой вывод:
 Поток 1:1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 4....
 Поток 2: 1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3....
 Всегда должен начинать Поток 1.
 */

public class NumberPrintCorrect {
    private static final Object lock = new Object();
    private static boolean isThread1Turn = true;
    private static volatile boolean shouldStop = false;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            int direction = 1;
            int count = 1;
            while (!shouldStop) {
                synchronized (lock) {
                    // while (!isThread1Turn && !shouldStop) { зачем тут нужна переменная shouldStop?
                    while (!isThread1Turn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // if (shouldStop) break; это условие излишне

                    System.out.println("Поток 1:" + count);

                    count += direction;  // Увеличиваем или уменьшаем count на direction
                    if (count == 10) {    // Если достигли верхней границы (10)
                        direction = -1;   // Меняем направление на уменьшение
                    }

                    isThread1Turn = false;
                    lock.notify();
                }

                try {
                    Thread.sleep(1000); // Небольшая задержка для наглядности
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            int direction = 1;
            int count = 1;
            while (!shouldStop) {
                synchronized (lock) {
                    // while (!isThread1Turn && !shouldStop) { зачем тут нужна переменная shouldStop?
                    while (isThread1Turn) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                   // if (shouldStop) break; это условие излишне

                    System.out.println("Поток 2:" + count);

                    count += direction;
                    if (count == 10) {
                        direction = -1;
                    } else if (count == 0) {
                        shouldStop = true;
                    }

                    isThread1Turn = true;
                    lock.notify();
                }

                try {
                    Thread.sleep(1000); // Небольшая задержка для наглядности
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}

