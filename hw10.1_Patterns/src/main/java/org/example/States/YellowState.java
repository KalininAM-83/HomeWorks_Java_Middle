package org.example.States;

import static java.lang.System.out;

//конкретное состояние - желтый свет
public class YellowState implements TrafficLightState {
    @Override
    public void display() {
        out.println("🟡 Желтый свет - ПРИГОТОВИТЬСЯ");
    }

    @Override
    public TrafficLightState next() {
        return new GreenState();
    }
}
