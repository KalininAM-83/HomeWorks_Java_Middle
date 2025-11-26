package org.example.services;

import org.example.models.Car;
import org.example.models.Checkpoint;

import static java.lang.System.out;

public class RepairCar implements ServicePoint {
    @Override
    public void serviceCar(Car car, Checkpoint checkpoint) {
        out.println("  Автомобиль в ремонте " + checkpoint.getName() +
                " (" + checkpoint.getCoordinates() + ")");
    }
}
