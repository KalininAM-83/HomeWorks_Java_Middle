package org.example.Factory;

import org.example.TrafficLights.PedestrianTrafficLight;
import org.example.TrafficLights.TrafficLight;

public class PedestrianTrafficLightFactory implements TrafficLightFactory {
    @Override
    public TrafficLight createTrafficLight() {
        return new PedestrianTrafficLight();
    }
}
