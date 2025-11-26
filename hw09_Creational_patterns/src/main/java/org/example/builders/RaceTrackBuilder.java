package org.example.builders;

import org.example.models.Car;
import org.example.models.Checkpoint;
import org.example.models.RaceTrack;

import java.util.ArrayList;
import java.util.List;

public class RaceTrackBuilder {
    private List<Checkpoint> checkpoints = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();

    public RaceTrackBuilder addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
        return this;
    }

    public RaceTrackBuilder addCar(Car car) {
        cars.add(car);
        return this;
    }

    public RaceTrack build() {
        return new RaceTrack(cars, checkpoints);
    }
}
