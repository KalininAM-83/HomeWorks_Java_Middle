package org.example.models;

import org.example.services.ServicePoint;

import java.util.List;

public abstract class Checkpoint {
    private final String name;
    private final Coordinates coordinates;
    private final List<ServicePoint> servicePoints;

    public Checkpoint(String name, Coordinates coordinates, List<ServicePoint> servicePoints) {
        this.name = name;
        this.coordinates = coordinates;
        this.servicePoints = servicePoints;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public abstract boolean isMandatory();

    public List<ServicePoint> getServicePoints() {
        return servicePoints;
    }
}