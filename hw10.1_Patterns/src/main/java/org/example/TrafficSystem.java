package org.example;

import org.example.Factory.CarTrafficLightFactory;
import org.example.Factory.PedestrianTrafficLightFactory;
import org.example.Factory.TrafficLightFactory;
import org.example.Factory.TrainTrafficLightFactory;
import org.example.TrafficLights.TrafficLight;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static java.lang.System.out;

public class TrafficSystem {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        //создаем фабрики
        TrafficLightFactory carFactory = new CarTrafficLightFactory();
        TrafficLightFactory trainFactory = new TrainTrafficLightFactory();
        TrafficLightFactory pedestrianFactory = new PedestrianTrafficLightFactory();

        //создаем светофоры
        TrafficLight carTrafficLight = carFactory.createTrafficLight();
        TrafficLight trainTrafficLight = trainFactory.createTrafficLight();
        TrafficLight pedestrianTrafficLight = pedestrianFactory.createTrafficLight();

        // Демонстрация работы светофоров с использованием State
        out.println("\n=== Демонстрация паттерна State ===");

        out.println("\nАвтомобильный светофор:");
        carTrafficLight.performCycle();

        out.println("\nПешеходный светофор:");
        pedestrianTrafficLight.displayCurrentState();
        pedestrianTrafficLight.switchToNextState();
        pedestrianTrafficLight.displayCurrentState();

        out.println("\nЖелезнодорожный светофор:");
        trainTrafficLight.displayCurrentState();
        trainTrafficLight.switchToNextState();
        trainTrafficLight.displayCurrentState();
    }
}
