package org.example.factories;

import org.example.models.Car;

public interface CarFactory {
    Car createCar(String stateNumber, String color, double power, double fuelReserve);
}
