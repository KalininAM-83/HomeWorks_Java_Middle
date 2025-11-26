package org.example;

//для оптимизации кода меняем ВЕЗДЕ Integer на int

public class Data {
    private final int value;

    public Data(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
