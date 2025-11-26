package org.example.services;

import org.example.models.Car;
import org.example.models.Checkpoint;

public interface ServicePoint {
    void serviceCar(Car car, Checkpoint checkpoint);
}
