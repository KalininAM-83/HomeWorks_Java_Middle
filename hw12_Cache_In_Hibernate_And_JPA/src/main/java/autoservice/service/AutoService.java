package autoservice.service;

import autoservice.entity.Car;
import autoservice.entity.CarHistory;
import autoservice.entity.Owner;
import autoservice.entity.TechnicalMaintenance;

import javax.persistence.*;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервисный класс для работы с автосервисом
 * Содержит бизнес-логику для управления владельцами, автомобилями и обслуживанием
 */
public class AutoService {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("autoservice-h2");

    private final Long databaseQueryCounter = new Long(0);

    //cache с мягкими ссылками SoftReference
    private final Map<Long, SoftReference<Car>> carCache = new ConcurrentHashMap<>(); // <- кэш для автомобилей
    private final Map<String, SoftReference<Owner>> ownerCache = new ConcurrentHashMap<>(); // <- кэш для владельцев


    //метод для очистки кэша от собранных GC объектов
    private void cleanCache() {
        carCache.values().removeIf(softReference -> softReference.get() == null);
        ownerCache.values().removeIf(softReference -> softReference.get() == null);
    }

    //метод для получения автмобиля из кэша
    public Car getCarById(Long id) {
        cleanCache();

        SoftReference<Car> cachedCar = carCache.get(id);
        if (cachedCar != null && cachedCar.get() != null) {
            return cachedCar.get();
        }

        EntityManager em = emf.createEntityManager();
        try {
            Car car = em.find(Car.class, id);
            if (car != null) {
                carCache.put(id, new SoftReference<>(car));
            }
            return car;
        } finally {
            em.close();
        }
    }

    //метод для получения владельца из кэша по номеру телефона
    public Owner getOwnerByPhoneNumber(String phoneNumber) {
        cleanCache();

        SoftReference<Owner> cachedOwner = ownerCache.get(phoneNumber);
        if (cachedOwner != null && cachedOwner.get() != null) {
            return cachedOwner.get();
        }

        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Owner> ownerQuery = em.createQuery(
                    "SELECT o FROM Owner o WHERE o.phoneNumber = :phoneNumber", Owner.class);
            ownerQuery.setParameter("phoneNumber", phoneNumber);
            Owner owner = ownerQuery.getSingleResult();
            if (owner != null) {
                ownerCache.put(phoneNumber, new SoftReference<>(owner));
            }
            return owner;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public boolean isOwnerCached(String phoneNumber) {
        return ownerCache.containsKey(phoneNumber);
    }

    public int getCarCacheSize() {
        return carCache.size();
    }

    public int getOwnerCacheSize() {
        return ownerCache.size();
    }

    public long getDatabaseQueryCount() {
        return databaseQueryCounter; // Счетчик нужно вести в классе
    }

    public boolean isCarCached(Long id) {
        return carCache.containsKey(id);
    }

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

            ownerCache.put(phoneNumber, new SoftReference<>(owner));
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

            // Добавляем в кэш
            carCache.put(car.getId(), new SoftReference<>(car));
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

            carCache.put(car.getId(), new SoftReference<>(car));
            ownerCache.put(owner.getPhoneNumber(), new SoftReference<>(owner));
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
    public void createTechnicalMaintenance(Car car, LocalDate date, String description,
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
        } finally {
            entityManager.close();
        }
    }

    /**
     * Получает всех автомобилей, отсортированных по марке и модели
     * (хороший пример с JOIN FETCH или без)
     */
    public List<Car> getAllCars() {
        cleanCache();

        //проверяем кэш
        List<Car> cachedCars = new ArrayList<>();
        for (SoftReference<Car> softReference : carCache.values()) {
            Car car = softReference.get();
            if (car != null) {
                cachedCars.add(car);
            }
        }
        if (!cachedCars.isEmpty()) {
            return cachedCars;
        }

        //если в кэше нет, запрашиваем из БД
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<Car> carQuery = entityManager.createQuery(
                    "SELECT c FROM Car c LEFT JOIN FETCH c.owner", Car.class);
            List<Car> cars = carQuery.getResultList();

            //заполняем кэш
            for (Car car : cars) {
                carCache.put(car.getId(), new SoftReference<>(car));
            }
            return cars;
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

            Owner owner = carHistories.getFirst().getOwner();
            return owner.getName() + " " + owner.getSurname();
        } finally {
            entityManager.close();
        }
    }
}