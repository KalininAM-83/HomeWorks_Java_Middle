package autoservice.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сущность автомобиля
 *
 * @Entity - указывает, что это JPA сущность
 * @Table(name = "cars") - маппинг на таблицу "cars" в базе данных
 */

@Entity
@Table(name = "cars")
public class Car {

    /**
     * Уникальный идентификатор автомобиля
     *
     * @Id - первичный ключ
     * @GeneratedValue (strategy = GenerationType.IDENTITY) - автоинкремент, база данных сама генерирует значения
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Марка автомобиля (например, Toyota, BMW, Mercedes)
     *
     * @Column (name = " brand ", nullable = false) - маппинг на колонку "brand", поле обязательное
     */

    @Column(name = "brand", nullable = false)
    private String brand;

    /**
     * Модель автомобиля (например, Camry, X5, C-Class)
     *
     * @Column (name = " model ", nullable = false) - маппинг на колонку "model", поле обязательное
     */

    @Column(name = "model", nullable = false)
    private String model;

    /**
     * Государственный номер автомобиля
     *
     * @Column(name = "registration_number", unique = true) - маппинг на колонку "license_plate", значение должно быть уникальным
     */

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    /**
     * VIN номер автомобиля (Vehicle Identification Number) - уникальный идентификатор
     *
     * @Column (name = " vin ", unique = true) - маппинг на колонку "vin", значение должно быть уникальным
     */

    @Column(name = "vin", unique = true)
    private String VIN;

    /**
     * Год выпуска автомобиля
     *
     * @Column (name = " year_manufactured ") - маппинг на колонку "year_manufactured", поле необязательное
     */

    @Column(name = "year_manufactured")
    private int yearManufactured;

    /**
     * Текущий владелец автомобиля
     *
     * @ManyToOne - связь "многие к одному" (много автомобилей могут принадлежать одному владельцу)
     * fetch = FetchType.LAZY - ленивая загрузка (владелец загружается только при обращении)
     * @JoinColumn (name = " owner_id ") - внешний ключ в таблице cars, ссылается на таблицу owners
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    /**
     * Список технических обслуживаний автомобиля
     *
     * @OneToMany - связь "один ко многим" (один автомобиль может иметь много обслуживаний)
     * mappedBy = "car" - связь управляется полем "car" в сущности Maintenance
     * cascade = CascadeType.ALL → все операции над Car будут автоматически применяться к Maintenance. Включает:
     * •	PERSIST → сохранение Car сохранит и его Maintenance.
     * •	MERGE → обновление Car обновит и связанные Maintenance.
     * •	REMOVE → удаление Car удалит и все связанные Maintenance.
     * fetch = FetchType.LAZY - ленивая загрузка (обслуживания загружаются только при обращении)
     */

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechnicalMaintenance> technicalMaintenances = new ArrayList<>();


    public Car() {
    }

    public Car(String brand, String model, String registrationNumber, String VIN, int yearManufactured) {
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.VIN = VIN;
        this.yearManufactured = yearManufactured;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public int getYearManufactured() {
        return yearManufactured;
    }

    public void setYearManufactured(int yearManufactured) {
        this.yearManufactured = yearManufactured;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && Objects.equals(brand, car.brand) && Objects.equals(model, car.model) && Objects.equals(registrationNumber, car.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, registrationNumber);
    }

    @Override
    public String toString() {
        return brand + " " + model +
                ", VIN '" + VIN + '\'' +
                ", гос. номер '" + registrationNumber;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public List<TechnicalMaintenance> getTechnicalMaintenances() {
        return technicalMaintenances;
    }

    public void setTechnicalMaintenances(List<TechnicalMaintenance> technicalMaintenances) {
        this.technicalMaintenances = technicalMaintenances;
    }

    public void addTechnicalMaintenance(TechnicalMaintenance technicalMaintenance) {
        technicalMaintenances.add(technicalMaintenance);
        technicalMaintenance.setCar(this);
    }

    public void removeTechnicalMaintenance(TechnicalMaintenance technicalMaintenance) {
        technicalMaintenances.remove(technicalMaintenance);
        technicalMaintenance.setCar(null);
    }
}
