package org.example;

import org.example.builders.RaceTrackBuilder;
import org.example.factories.*;
import org.example.models.*;
import org.example.services.CarParking;
import org.example.services.RepairCar;
import org.example.services.ServicePoint;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        //Создаем фабрики
        CheckpointFactory checkpointFactory = new CheckpointFactoryImpl();
        CarFactory truckFactory = new TruckFactory();
        CarFactory lightCarFactory = new LightCarFactory();

        List<ServicePoint> defaultServices = List.of(
                new CarParking(),
                new RepairCar()
        );

        //создаем контрольные пункты
        Checkpoint cp1 = checkpointFactory.createCheckpoint("Контрольный пункт 1: ", new Coordinates(55.75222,
                37.61556), null, defaultServices);
        Checkpoint cp2 = checkpointFactory.createCheckpoint("Контрольный пункт 2: ", new Coordinates(51.5074,
                30.5233), 1.0, defaultServices);
        Checkpoint cp3 = checkpointFactory.createCheckpoint("Контрольный пункт 3: ", new Coordinates(53.34643,
                58.681), 2.0, defaultServices);
        Checkpoint cp4 = checkpointFactory.createCheckpoint("Контрольный пункт 4: ", new Coordinates(55.75222,
                37.61556), null, defaultServices);

        //создаем автомобили
        Car truck = truckFactory.createCar("А352РМ", "Белый", 400.0, 200.0);
        Car truck2 = truckFactory.createCar("О879НП", "Желтый", 380.0, 200.0);
        Car lightCar = lightCarFactory.createCar("И253ИИ", "Черный", 300.0, 80.0);
        Car lightCar2 = lightCarFactory.createCar("А253ИИ", "Синий", 320.0, 83.0);

        //строим трассу
        RaceTrack track = new RaceTrackBuilder()
                .addCheckpoint(cp1)
                .addCheckpoint(cp2)
                .addCheckpoint(cp3)
                .addCheckpoint(cp4)
                .addCar(truck)
                .addCar(truck2)
                .addCar(lightCar)
                .addCar(lightCar2)
                .build();

        //выводим информацию на табло
        out.println("Гоночная трасса имеет " + track.getCheckpoints().size() + " контрольных пункта(ов):");
        track.getCheckpoints().forEach(checkpoint -> {
            out.println("- " + checkpoint.getName() +
                    checkpoint.getCoordinates().getLatitude() + ", " +
                    checkpoint.getCoordinates().getLongitude() +
                    (checkpoint.isMandatory() ? " (обязательный)" : " (необязательный, штраф: " +
                            ((OptionalCheckpoint) checkpoint).getPenaltyHours() + " час)"));
        });

        out.println("\nВ гонке участвуют следующие автомобили: ");
        track.getCars().forEach(car -> {
            out.println("- " + car.getTypeCar() + " гос.номер: " + car.getStateNumber() + ", цвет: " + car.getColor() +
                    ", мощность: " + car.getPower() + ", запас топлива: " + car.getFuelReserve() + " литров");
        });

        // Эмулируем прохождение КП автомобилями
        out.println("\n=== Прохождение трассы автомобилями ===");

// Для каждого автомобиля
        track.getCars().forEach(car -> {
            out.println("\nАвтомобиль " + car.getStateNumber() + " (" + car.getTypeCar() + "):");

            // Проходим все контрольные пункты
            track.getCheckpoints().forEach(checkpoint -> {
                out.println("  Проходит " + checkpoint.getName() +
                        " (" + checkpoint.getCoordinates().getLatitude() + ", " +
                        checkpoint.getCoordinates().getLongitude() + ")");

                // Используем сервисные точки
                checkpoint.getServicePoints().forEach(service -> {
                    if (service instanceof CarParking) {
                        out.println("    - Заехал на парковку");
                        service.serviceCar(car, checkpoint);
                    } else if (service instanceof RepairCar) {
                        out.println("    - Отправлен на ремонт");
                        service.serviceCar(car, checkpoint);
                    }
                });
            });
        });
    }
}

