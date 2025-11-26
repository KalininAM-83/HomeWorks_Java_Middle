package org.example.States;

import static java.lang.System.out;

//конкретное состояние - зеленый свет
public class GreenState implements TrafficLightState {
    @Override
    public void display() {
        out.println("🟢 Зеленый свет - МОЖНО ЕХАТЬ");
    }

    @Override
    public TrafficLightState next() {
        return new RedState();
    }
}
