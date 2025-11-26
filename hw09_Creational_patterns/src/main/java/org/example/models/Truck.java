package org.example.models;

public class Truck extends Car {
    public Truck(String stateNumber, String color, double power, double fuelReserve) {
        super(stateNumber, color, power, fuelReserve);
    }

    @Override
    public String getTypeCar() {
        return "Грузовой автомобиль";
    }
}
