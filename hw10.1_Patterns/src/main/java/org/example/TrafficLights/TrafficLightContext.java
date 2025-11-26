package org.example.TrafficLights;

import org.example.States.TrafficLightState;

public class TrafficLightContext {
    private TrafficLightState currentState;

    public TrafficLightContext(TrafficLightState initialState) {
        this.currentState = initialState;
    }

    public void displayCurrentState() {
        currentState.display();
    }

    public void switchToNextState() {
        currentState = currentState.next();
    }

    // Показываем полный цикл (красный-зеленый-желтый)
    public void performFullCycle() {
        for (int i = 0; i < 3; i++) {
            displayCurrentState();
            switchToNextState();
        }
    }
}
