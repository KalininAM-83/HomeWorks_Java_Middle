package org.example;

import com.google.common.base.Strings;

public class HelloGradle {
    /**
     * Проверяет, является ли строка null или пустой (используя Guava)
     * @param input строка для проверки
     * @return true, если строка null или пустая
     */
    public static boolean isNullOrEmpty(String input) {
        return Strings.isNullOrEmpty(input);
    }

    public static void main(String[] args) {
        String test1 = null;
        String test2 = "";
        String test3 = "Hello Guava!";

        System.out.println("Test 1 (null): " + isNullOrEmpty(test1));
        System.out.println("Test 2 (empty string): " + isNullOrEmpty(test2));
        System.out.println("Test 3 (not empty string): " + isNullOrEmpty(test3));
    }
}
