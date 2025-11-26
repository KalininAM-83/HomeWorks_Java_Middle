package org.example.factories;

import org.example.models.Car;
import org.example.models.Truck;

public class TruckFactory implements CarFactory {
    @Override
    public Car createCar(String stateNumber, String color, double power, double fuelReserve) {
        return new Truck(stateNumber, color, power, fuelReserve);
    }
}
