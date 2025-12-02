package org.example;

/**
Создайте аккаунт на github.com (если еще нет)
Создайте репозиторий для домашних работ
Сделайте checkout репозитория на свой компьютер
Создайте локальный бранч hw01-gradle
Создать проект gradle
В проект добавьте последнюю версию зависимости  guava com.google.guava
Создайте модуль hw01-gradle
В модуле сделайте класс HelloGradle
В этом классе сделайте вызов какого-нибудь метода из guava
Создайте "толстый-jar"
Убедитесь, что "толстый-jar" запускается.
Сделайте pull-request в gitHub
*/

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
