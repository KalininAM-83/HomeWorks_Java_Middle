package org.example.factories;

import org.example.models.Checkpoint;
import org.example.models.Coordinates;
import org.example.models.MandatoryCheckpoint;
import org.example.models.OptionalCheckpoint;
import org.example.services.ServicePoint;

import java.util.List;

public class CheckpointFactoryImpl implements CheckpointFactory {
    @Override
    public Checkpoint createCheckpoint(String name, Coordinates coordinates, Double penaltyHours, List<ServicePoint> servicePoints) {
        if (penaltyHours == null) {
            return new MandatoryCheckpoint(name, coordinates, servicePoints);
        } else {
            return new OptionalCheckpoint(name, coordinates, penaltyHours, servicePoints);
        }
    }
}
