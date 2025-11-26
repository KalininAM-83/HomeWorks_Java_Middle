package org.example.models;

import org.example.services.ServicePoint;

import java.util.List;

public class MandatoryCheckpoint extends Checkpoint {

    public MandatoryCheckpoint(String name, Coordinates coordinates, List<ServicePoint> servicePoints) {
        super(name, coordinates, servicePoints);
    }

    @Override
    public boolean isMandatory() {
        return true;
    }
}
