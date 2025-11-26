package org.example.models;

public abstract class Car {
    private final String stateNumber;
    private final String color;
    private final double power;
    private double fuelReserve;

    public Car(String stateNumber, String color, double power, double fuelReserve) {
        this.stateNumber = stateNumber;
        this.color = color;
        this.power = power;
        this.fuelReserve = fuelReserve;
    }

    public String getColor() {
        return color;
    }

    public double getFuelReserve() {
        return fuelReserve;
    }

    public double getPower() {
        return power;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setFuelReserve(double fuelReserve) {
        this.fuelReserve = fuelReserve;
    }

    public abstract String getTypeCar();
}
