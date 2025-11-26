package autoservice.service;

import autoservice.entity.Car;
import autoservice.entity.CarHistory;
import autoservice.entity.Owner;
import autoservice.entity.TechnicalMaintenance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Сервисный класс для работы с автосервисом
 * Содержит бизнес-логику для управления владельцами, автомобилями и обслуживанием
 */
public class AutoService {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("autoservice-h2");

    /**
     * Создает нового владельца автомобиля
     */
    public Owner createOwner(String name, String surname, String phoneNumber) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Owner owner = new Owner(name, surname, phoneNumber);
            entityManager.persist(owner);
            entityManager.getTransaction().commit();
            return owner;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Создает новый автомобиль
     */
    public Car createCar(String brand, String model, String registrationNumber, String vin, int yearManufactured) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Car car = new Car(brand, model, registrationNumber, vin, yearManufactured);
            entityManager.persist(car);
            entityManager.getTransaction().commit();
            return car;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Назначает автомобиль владельцу
     */
    public void assignCarToOwner(Car car, Owner owner) {
        assignCarToOwner(car, owner, LocalDate.now());
    }

    /**
     * Назначает автомобиль владельцу с указанной датой начала владения
     */
    public void assignCarToOwner(Car car, Owner owner, LocalDate carHistoryStartDate) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin(); // <- начало транзакции
            CarHistory history = new CarHistory(car, owner, carHistoryStartDate);
            entityManager.persist(history);
            car.setOwner(owner);
            owner.addCar(car);
            entityManager.merge(car);
            entityManager.merge(owner);
            entityManager.getTransaction().commit(); // <- завершение транзакции
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace(); // Логирование ошибки
            throw new RuntimeException("Ошибка привязки автомобиля к владельцу", e);
        } finally {
            entityManager.close();
        }
    }

    /**
     * Меняет владельца автомобиля
     */
    public void changeCarOwner(Car car, Owner newOwner) {
        changeCarOwner(car, newOwner, LocalDate.now());
    }

    /**
     * Меняет владельца автомобиля с указанной датой смены
     */
    public void changeCarOwner(Car car, Owner newOwner, LocalDate changeDate) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Owner oldOwner = car.getOwner();

            // Завершаем предыдущее владение
            if (oldOwner != null) {
                oldOwner.removeCar(car);
                entityManager.merge(oldOwner);

                // Находим и закрываем активное владение
                TypedQuery<CarHistory> activeOwnershipQuery = entityManager.createQuery(
                        "SELECT co FROM CarHistory co WHERE co.car = :car AND co.endDate IS NULL",
                        CarHistory.class);
                activeOwnershipQuery.setParameter("car", car);
                List<CarHistory> activeOwnerships = activeOwnershipQuery.getResultList();

                for (CarHistory carHistory : activeOwnerships) {
                    carHistory.setEndDate(changeDate);
                    entityManager.merge(carHistory);
                }
            }

            // Назначаем нового владельца
            car.setOwner(newOwner);
            newOwner.addCar(car);

            // Создаем новую запись в истории владения
            CarHistory newOwnership = new CarHistory(car, newOwner, changeDate);
            entityManager.persist(newOwnership);

            entityManager.merge(car);
            entityManager.merge(newOwner);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Создает запись о техническом обслуживании
     */
    public TechnicalMaintenance createTechnicalMaintenance(Car car, LocalDate date, String description,
                                                           BigDecimal cost, int mileage, String serviceType) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            TechnicalMaintenance technicalMaintenance = new TechnicalMaintenance(date, description, cost, mileage, serviceType);
            technicalMaintenance.setCar(car);
            car.addTechnicalMaintenance(technicalMaintenance);
            entityManager.persist(technicalMaintenance);
            entityManager.merge(car);
            entityManager.getTransaction().commit();
            return technicalMaintenance;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Получает всех автомобилей, отсортированных по марке и модели
     * (хороший пример с JOIN FETCH или без)
     */
    public List<Car> getAllCars() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<Car> carQuery = entityManager.createQuery(
                    "SELECT c FROM Car c LEFT JOIN FETCH c.owner ORDER BY c.brand, c.model", Car.class);
            return carQuery.getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Получает все записи обслуживания для конкретного автомобиля
     */
    public List<TechnicalMaintenance> getTechnicalMaintenanceForCar(Car car) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<TechnicalMaintenance> maintenanceQuery = entityManager.createQuery(
                    "SELECT m FROM TechnicalMaintenance m WHERE m.car = :car ORDER BY m.technicalMaintenanceDate",
                    TechnicalMaintenance.class);
            maintenanceQuery.setParameter("car", car);
            return maintenanceQuery.getResultList();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Получает общее количество проведенных ТО
     */
    public Long getTotalTechnicalMaintenanceCount() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<Long> totalMaintenanceQuery =
                    entityManager.createQuery("SELECT COUNT(m) FROM TechnicalMaintenance m", Long.class);
            return totalMaintenanceQuery.getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Получает общую стоимость всех ТО
     */
    public BigDecimal getTotalTechnicalMaintenanceCost() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<BigDecimal> totalCostQuery =
                    entityManager.createQuery("SELECT SUM(m.cost) FROM TechnicalMaintenance m", BigDecimal.class);
            return totalCostQuery.getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Получает владельца автомобиля на момент обслуживания
     */
    public String getOwnerAtTechnicalMaintenanceTime(Car car, LocalDate technicalMaintenanceDate) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<CarHistory> carHistoryQuery = entityManager.createQuery(
                    "SELECT co FROM CarHistory co WHERE co.car = :car AND " +
                            "co.startDate <= :technicalMaintenanceDate AND " +
                            "(co.endDate IS NULL OR co.endDate >= :technicalMaintenanceDate) " +
                            "ORDER BY co.startDate DESC",
                    CarHistory.class);
            carHistoryQuery.setParameter("car", car);
            carHistoryQuery.setParameter("technicalMaintenanceDate", technicalMaintenanceDate);

            List<CarHistory> carHistories = carHistoryQuery.getResultList();

            if (carHistories.isEmpty()) {
                return "Неизвестен";
            }

            Owner owner = carHistories.get(0).getOwner();
            return owner.getName() + " " + owner.getSurname();
        } finally {
            entityManager.close();
        }
    }
}