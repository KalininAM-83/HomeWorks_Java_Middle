package org.example.States;

// Создаем интерфейс состояния
public interface TrafficLightState {
    void display();

    TrafficLightState next();
}
