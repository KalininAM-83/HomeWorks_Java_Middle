package org.example.States;

import static java.lang.System.out;


//конкретное состояние - красный свет
public class RedState implements TrafficLightState {
    @Override
    public void display() {
        out.println("🔴 Красный свет - СТОП");
    }

    @Override
    public TrafficLightState next() {
        return new YellowState();
    }
}
