package org.example.Factory;

import org.example.TrafficLights.TrafficLight;
import org.example.TrafficLights.TrainTrafficLight;

public class TrainTrafficLightFactory implements TrafficLightFactory {
    @Override
    public TrafficLight createTrafficLight() {
        return new TrainTrafficLight();
    }
}
