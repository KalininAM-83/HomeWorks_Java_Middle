package org.example.factories;

import org.example.models.Checkpoint;
import org.example.models.Coordinates;
import org.example.services.ServicePoint;

import java.util.List;

public interface CheckpointFactory {
    Checkpoint createCheckpoint(String name, Coordinates coordinates, Double penaltyHours, List<ServicePoint> servicePoints);
}
