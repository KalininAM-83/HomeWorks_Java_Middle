package autoservice.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сущность технического обслуживания автомобиля
 *
 * @Entity - указывает, что это JPA сущность
 * @Table(name = "technical_maintenances") - маппинг на таблицу "technical_maintenances" в базе данных
 */
@Entity
@Table(name = "technical_maintenances")
public class TechnicalMaintenance {

    /**
     * Уникальный идентификатор записи обслуживания
     *
     * @Id - первичный ключ
     * @GeneratedValue(strategy = GenerationType.IDENTITY) - автоинкремент, база данных сама генерирует значения
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата проведения технического обслуживания
     *
     * @Column(name = "technical_maintenance_date", nullable = false) - маппинг на колонку "maintenance_date", поле обязательное
     */
    @Column(name = "technical_maintenance_date", nullable = false)
    private LocalDate technicalMaintenanceDate;

    /**
     * Описание проведенных работ (например, "Замена масла и фильтров", "Диагностика тормозной системы")
     *
     * @Column(name = "description", columnDefinition = "TEXT") - маппинг на колонку "description" типа TEXT для длинных описаний
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Стоимость проведенного обслуживания в рублях
     *
     * @Column(name = "cost", precision = 10, scale = 2) - маппинг на колонку "cost" с точностью 10 цифр, 2 после запятой
     */
    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    /**
     * Пробег автомобиля на момент обслуживания (в километрах)
     *
     * @Column(name = "mileage") - маппинг на колонку "mileage", поле необязательное
     */
    @Column(name = "mileage")
    private int mileage;

    /**
     * Тип технического обслуживания (например, "ТО-1", "ТО-2", "ТО-3", "Диагностика")
     *
     * @Column(name = "service_type") - маппинг на колонку "service_type", поле необязательное
     */
    @Column(name = "service_type")
    private String serviceType;

    /**
     * Автомобиль, для которого проводилось обслуживание
     *
     * @ManyToOne - связь "многие к одному" (много обслуживаний могут принадлежать одному автомобилю)
     * fetch = FetchType.LAZY - ленивая загрузка (автомобиль загружается только при обращении)
     * @JoinColumn(name = "car_id", nullable = false) - внешний ключ в таблице TechnicalMaintenance, ссылается на таблицу cars, поле обязательное
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    public TechnicalMaintenance() {
    }

    public TechnicalMaintenance(LocalDate technicalMaintenanceDate, String description, BigDecimal cost,
                                int mileage, String serviceType) {
        this.technicalMaintenanceDate = technicalMaintenanceDate;
        this.description = description;
        this.cost = cost;
        this.mileage = mileage;
        this.serviceType = serviceType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTechnicalMaintenanceDate() {
        return technicalMaintenanceDate;
    }

    public void setTechnicalMaintenanceDate(LocalDate technicalMaintenanceDate) {
        this.technicalMaintenanceDate = technicalMaintenanceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "TechnicalMaintenance{" +
                "id=" + id +
                ", technicalMaintenanceDate=" + technicalMaintenanceDate +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", mileage=" + mileage +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }
}
