package org.example.models;

import org.example.services.ServicePoint;

import java.util.List;

public class OptionalCheckpoint extends Checkpoint {
    private final double penaltyHours;

    public OptionalCheckpoint(String name, Coordinates coordinates, double penaltyHours, List<ServicePoint> servicePoints) {
        super(name, coordinates, servicePoints);
        this.penaltyHours = penaltyHours;
    }

    @Override
    public boolean isMandatory() {
        return false;
    }

    public double getPenaltyHours() {
        return penaltyHours;
    }
}
