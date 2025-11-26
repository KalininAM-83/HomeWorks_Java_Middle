package org.example.factories;

import org.example.models.Car;
import org.example.models.LightCar;

public class LightCarFactory implements CarFactory {
    @Override
    public Car createCar(String stateNumber, String color, double power, double fuelReserve) {
        return new LightCar(stateNumber, color, power, fuelReserve);
    }
}
