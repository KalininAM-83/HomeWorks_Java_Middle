package org.example.TrafficLights;

import org.example.States.RedState;

// Базовый класс для всех светофоров
public abstract class TrafficLight {
    protected TrafficLightContext stateContext;

    public TrafficLight() {
        this.stateContext = new TrafficLightContext(new RedState());
    }

    public void performCycle() {
        stateContext.performFullCycle();
    }

    public void displayCurrentState() {
        stateContext.displayCurrentState();
    }

    public void switchToNextState() {
        stateContext.switchToNextState();
    }
}