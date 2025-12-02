package autoservice.demo;

/**
 Закончите реализацию MyCache из вебинара. Используйте WeakHashMap для хранения значений.

 Добавьте кэширование в DBService из задания про Hibernate ORM или "Самодельный ORM". Для простоты скопируйте нужные классы в это ДЗ.

 Убедитесь, что ваш кэш действительно работает быстрее СУБД и сбрасывается при недостатке памяти.
 Код предыдущего задания менять не надо. Просто скопируйте все нужные классы.
 */

import autoservice.entity.Car;
import autoservice.entity.Owner;
import autoservice.entity.TechnicalMaintenance;
import autoservice.service.AutoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.lang.System.out;

public class AutoServiceDemo {
    private static AutoService autoService;

    public static void main(String[] args) {
        autoService = new AutoService();

        try {
            out.println("\n==Автосервис==");

            //создаем владельцев
            out.println("\n--Создаем владельцев автомобилей:--");
            Owner owner1 = createOwnerWithLogging("Алексей", "Иванов", "+79991112233");
            Owner owner2 = createOwnerWithLogging("Анастасия", "Петрова", "+79993332211");
            Owner owner3 = createOwnerWithLogging("Андрей", "Сидоров", "+79994445566");

            //создаем автомобили
            out.println("\n--Создаем автомобили:--");
            Car car1 = createCarWithLogging("БМВ", "Х5", "E999EE777", "12345678901234567", 2015);
            Car car2 = createCarWithLogging("Mercedes", "Vito", "M888MM198", "WWW235741GD089D08", 2020);
            Car car3 = createCarWithLogging("Nissan", "Murano", "B232EE23", "QVD02587942HN0123", 2022);

            //назначаем владельцев автомобилям до первого ТО
            out.println("\n--Назначаем владельцев автомобилям:--");
            assignCarToOwnerWithLogging(car1, owner1, LocalDate.of(2015, 1, 11));
            assignCarToOwnerWithLogging(car2, owner2, LocalDate.of(2020, 11, 1));
            assignCarToOwnerWithLogging(car3, owner3, LocalDate.of(2022, 9, 21));

            //первые заезды на ТО
            out.println("\n--Первые заезды на ТО:--");
            createTechnicalMaintenanceWithLogging(car1, LocalDate.of(2016, 2, 3),
                    "Замена масла и масляного фильтра, проверка ходовой", new BigDecimal("5000.00"), 10000, "ТО-1");
            createTechnicalMaintenanceWithLogging(car2, LocalDate.of(2021, 5, 13),
                    "Замена масла и масляного фильтра, воздушного и салонного фильтра", new BigDecimal("10000.00"), 15000, "ТО-1");
            createTechnicalMaintenanceWithLogging(car3, LocalDate.of(2024, 8, 30),
                    "Замена КПП по гарантии", new BigDecimal("0.00"), 20000, "ТО");

            //смена владельцев
            out.println("\nСмена владельца автомобиля - Алексей Иванов продаёт автомобиль Андрею Сидорову:");
            changeCarOwnerWithLogging(car1, owner3, LocalDate.of(2016, 3, 12));

            out.println("\nСмена владельца автомобиля - Анастасия Петрова продаёт автомобиль Алексею Иванову:");
            changeCarOwnerWithLogging(car2, owner1, LocalDate.of(2021, 6, 18));

            //заезды на обслуживание с новыми владельцами
            out.println("\nЗаезды на ТО с новыми владельцами:");
            createTechnicalMaintenanceWithLogging(car1, LocalDate.of(2017, 4, 1),
                    "Замена масла и масляного фильтра, воздушных фильтров", new BigDecimal(8000), 20000, "ТО-2");
            createTechnicalMaintenanceWithLogging(car2, LocalDate.of(2022, 8, 11),
                    "Замена масла и масляного фильтра, воздушных фильтров", new BigDecimal(8000), 30000, "ТО-2");

            //дополнительные заезды на ТО
            out.println("\nДополнительные заезды на ТО:");
            createTechnicalMaintenanceWithLogging(car1, LocalDate.of(2018, 5, 1),
                    "Полное ТО-3", new BigDecimal(30000), 35000, "ТО-3");
            createTechnicalMaintenanceWithLogging(car2, LocalDate.of(2023, 1, 18),
                    "Осмотр ходовой", new BigDecimal(5000), 35000, "ходовая");
            createTechnicalMaintenanceWithLogging(car3, LocalDate.of(2025, 10, 15),
                    "замена тормозной жидкости, антифриза", new BigDecimal(20000), 32000, "жидкости");

            //выводим итоговый отчет
            out.println("\n" + "=".repeat(130));
            out.println("Итоговый отчет:");
            out.println("=".repeat(130));

            printTechnicalMaintenanceReport();
            printCacheStatistics();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static Owner createOwnerWithLogging(String name, String surname, String phoneNumber) {
        Owner owner = autoService.createOwner(name, surname, phoneNumber);
        out.println("Создан владелец: " + owner);
        return owner;
    }

    private static void getOwnerWithCacheLogging(String phoneNumber) {
        Owner owner = autoService.getOwnerByPhoneNumber(phoneNumber);
        if (owner != null) {
            System.out.println((autoService.isOwnerCached(phoneNumber)
                    ? "Получен из кэша: "
                    : "Загружен из базы: ") + owner);
        }
    }

    private static void getCarWithCacheLogging(Long Id) {
        Car car = autoService.getCarById(Id);
        if (car != null) {
            System.out.println((autoService.isCarCached(Id)
                    ? "Получен из кэша: "
                    : "Загружен из базы: ") + car);
        }
    }

    private static Car createCarWithLogging(String brand, String model, String registrationNumber, String vin, int yearManufactured) {
        Car car = autoService.createCar(brand, model, registrationNumber, vin, yearManufactured);
        out.println("Создан автомобиль: " + car);
        return car;
    }

    private static void assignCarToOwnerWithLogging(Car car, Owner owner, LocalDate carHistoryDate) {
        try {
            autoService.assignCarToOwner(car, owner, carHistoryDate);
            System.out.println("Автомобиль " + car.getBrand() + " " + car.getModel() + " " + car.getVIN() + " назначен владельцу "
                    + owner.getName() + " " + owner.getSurname());
        } catch (RuntimeException e) {
            System.err.println("Ошибка при назначении автомобиля " + car.getBrand() + " " + car.getModel() + " " + " владельцу " + owner.getName()
                    + owner.getSurname());
            e.printStackTrace();
        }
    }

    private static void assignCarToOwnerWithLogging(Car car, Owner owner) {
        autoService.assignCarToOwner(car, owner);
        out.println("Автомобиль " + car.getBrand() + " " + car.getModel() + " назначен владельцу " + owner.getName() +
                " " + owner.getSurname());
    }

    private static void createTechnicalMaintenanceWithLogging(Car car, LocalDate date, String description, BigDecimal cost, int km, String serviceType) {
        autoService.createTechnicalMaintenance(car, date, description, cost, km, serviceType);
        out.println("Обслуживание: " + car.getBrand() + " " + car.getModel() + " " + car.getRegistrationNumber() + " - " + description + " - " + cost + " руб.");
    }

    private static void changeCarOwnerWithLogging(Car car, Owner newOwner, LocalDate changeDate) {
        Owner oldOwner = car.getOwner();
        autoService.changeCarOwner(car, newOwner, changeDate);
        out.println("Автомобиль " + car.getBrand() + " " + car.getModel() + " " + car.getVIN() + " продан " +
                (oldOwner != null ? oldOwner.getName() + " " + oldOwner.getSurname() : "неизвестному владельцу")
                + " владельцу " + newOwner.getName() + " " + newOwner.getSurname() + " с " + changeDate);
    }

    private static void printTechnicalMaintenanceReport() {
        List<Car> cars = autoService.getAllCars();

        for (Car car : cars) {
            out.println("\n" + "-".repeat(130));
            out.println("\nАвтомобиль: " + car.getBrand() + " " + car.getModel() + " (" + car.getRegistrationNumber() + ")");
            out.println("\nТекущий владелец: " + (car.getOwner() != null ? car.getOwner().getName() + " " + car.getOwner().getSurname() : "неизвестен"));
            out.println("-".repeat(130));

            List<TechnicalMaintenance> technicalMaintenances = autoService.getTechnicalMaintenanceForCar(car);

            if (technicalMaintenances.isEmpty()) {
                out.println("\nНет записей о техническом обслуживании");
            } else {
                out.printf("%-12s %-80s %-15s %-8s %-15s%n", "Дата", "Описание", "Стоимость", "Пробег", "Тип ТО");
                out.println("-".repeat(130));

                for (TechnicalMaintenance technicalMaintenance : technicalMaintenances) {
                    //получаем владельца на момент обслуживания
                    String ownerAtTechnicalMaintenanceTime = autoService.getOwnerAtTechnicalMaintenanceTime(car, technicalMaintenance.getTechnicalMaintenanceDate());
                    out.printf("%n%-12s %-80s %-15s %-8s %-15s%n",
                            technicalMaintenance.getTechnicalMaintenanceDate(),
                            technicalMaintenance.getDescription().length() > 80 ?
                                    technicalMaintenance.getDescription().substring(0, 77) + "..." :
                                    technicalMaintenance.getDescription(),
                            technicalMaintenance.getCost() + " руб.",
                            technicalMaintenance.getMileage(),
                            technicalMaintenance.getServiceType());
                    out.println("\n   Владелец на момент проведения ТО: " + ownerAtTechnicalMaintenanceTime);
                }
            }
        }

        // Демонстрация кэширования
        out.println("\n===Демонстрация кэширования:===");
        out.println("\nПервый запрос владельца +79991112233: ");
        getOwnerWithCacheLogging("+79991112233");

        out.println("Второй запрос владельца +79991112233: ");
        getOwnerWithCacheLogging("+79991112233");

        out.println("Запрос несуществующего владельца +79001112233: ");
        getOwnerWithCacheLogging("+79001112233");

        out.println("Первый запрос автомобиля с id=1: ");
        getCarWithCacheLogging(1L);

        out.println("Второй запрос автомобиля с id=1: ");
        getCarWithCacheLogging(1L);

        out.println("Запрос несуществующего автомобиля с id=100: ");
        getCarWithCacheLogging(100L);

        //общая статистика
        out.println("\n" + "=".repeat(130));
        out.println("Общая статистика:");

        Long totalTechnicalMaintenance = autoService.getTotalTechnicalMaintenanceCount();
        BigDecimal totalCost = autoService.getTotalTechnicalMaintenanceCost();

        out.println("\nВсего проведено технических обслуживаний: " + totalTechnicalMaintenance);
        out.println("Общая стоимость технических обслуживаний: " + totalCost + " руб.");
        out.println("=".repeat(130));
    }

    private static void printCacheStatistics() {
        out.println("\nСтатистика кэша:");
        out.println("Размер кэша автомобилей: " + autoService.getCarCacheSize());
        out.println("Размер кэша владельцев: " + autoService.getOwnerCacheSize());
        out.println("Запросов к БД: " + autoService.getDatabaseQueryCount());
    }
}
