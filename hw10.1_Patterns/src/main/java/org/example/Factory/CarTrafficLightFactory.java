package org.example.Factory;

import org.example.TrafficLights.CarTrafficLight;
import org.example.TrafficLights.TrafficLight;

public class CarTrafficLightFactory implements TrafficLightFactory {
    public TrafficLight createTrafficLight() {
        return new CarTrafficLight();
    }
}
