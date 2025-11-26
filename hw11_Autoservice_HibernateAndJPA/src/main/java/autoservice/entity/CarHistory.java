package autoservice.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность для отслеживания истории владения автомобилями
 *
 * @Entity - указывает, что это JPA сущность
 * @Table(name = "car_history") - маппинг на таблицу "car_ownership" в базе данных
 */

@Entity
@Table(name = "car_history")
public class CarHistory {

    /**
     * Уникальный идентификатор записи владения
     *
     * @Id - первичный ключ
     * @GeneratedValue(strategy = GenerationType.IDENTITY) - автоинкремент, база данных сама генерирует значения
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Автомобиль, для которого записывается владение
     *
     * @ManyToOne - связь "многие к одному" (много записей владения могут принадлежать одному автомобилю)
     * fetch = FetchType.LAZY - ленивая загрузка (автомобиль загружается только при обращении)
     * @JoinColumn(name = "car_id", nullable = false) - внешний ключ в таблице car_ownership, ссылается на таблицу cars, поле обязательное
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    /**
     * Владелец автомобиля в данный период
     *
     * @ManyToOne - связь "многие к одному" (много записей владения могут принадлежать одному владельцу)
     * fetch = FetchType.LAZY - ленивая загрузка (владелец загружается только при обращении)
     * @JoinColumn(name = "owner_id", nullable = false) - внешний ключ в таблице car_ownership, ссылается на таблицу owners, поле обязательное
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    /**
     * Дата начала владения автомобилем
     *
     * @Column(name = "start_date", nullable = false) - маппинг на колонку "start_date", поле обязательное
     */

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Дата окончания владения автомобилем (null означает, что владение активно)
     *
     * @Column(name = "end_date") - маппинг на колонку "end_date", поле необязательное
     */

    @Column(name = "end_date")
    private LocalDate endDate;

    public CarHistory() {
    }

    public CarHistory(Car car, Owner owner, LocalDate startDate) {
        this.car = car;
        this.owner = owner;
        this.startDate = startDate;
    }

    public CarHistory(Car car, Owner owner, LocalDate startDate, LocalDate endDate) {
        this.car = car;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Проверяет, был ли владелец активен на указанную дату
     */

    public boolean isActiveOnDate(LocalDate date) {
        return !date.isBefore(startDate) && (endDate == null || !date.isAfter(endDate));
    }

    @Override
    public String toString() {
        return "CarHistory{" +
                "car=" + car +
                ", id=" + id +
                ", owner=" + owner +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
