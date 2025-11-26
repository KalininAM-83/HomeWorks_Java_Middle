package org.example.models;

import java.util.List;

public class RaceTrack {
    private final List<Checkpoint> checkpoints;
    private final List<Car> cars;

    public RaceTrack(List<Car> cars, List<Checkpoint> checkpoints) {
        this.cars = cars;
        this.checkpoints = checkpoints;
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }
}
