package org.example.models;

public class LightCar extends Car {
    public LightCar(String stateNumber, String color, double power, double fuelReserve) {
        super(stateNumber, color, power, fuelReserve);
    }

    @Override
    public String getTypeCar() {
        return "Легковой автомобиль";
    }
}
