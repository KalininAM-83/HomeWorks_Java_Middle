package autoservice.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сущность владельца автомобиля
 *
 * @Entity - указывает, что это JPA сущность
 * @Table(name = "owners") - маппинг на таблицу "owners" в базе данных
 */

@Entity
@Table(name = "owners")
public class Owner {

    /**
     * Уникальный идентификатор владельца
     *
     * @Id - первичный ключ
     * @GeneratedValue(strategy = GenerationType.IDENTITY) - автоинкремент, база данных сама генерирует значения
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /**
     * Список автомобилей, принадлежащих владельцу
     *
     * @OneToMany - связь "один ко многим" (один владелец может иметь много автомобилей)
     * mappedBy = "owner" - связь управляется полем "owner" в сущности Car
     * cascade = CascadeType.ALL - все операции (сохранение, обновление, удаление) каскадируются на связанные автомобили
     * fetch = FetchType.LAZY - ленивая загрузка (автомобили загружаются только при обращении)
     */

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();

    public Owner() {
    }

    public Owner(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return id == owner.id && Objects.equals(name, owner.name) && Objects.equals(surname, owner.surname) && Objects.equals(phoneNumber, owner.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, phoneNumber);
    }

    @Override
    public String toString() {
        return STR."\{name} \{surname} Телефон:\{phoneNumber}";
    }

    public void addCar(Car car) {
        cars.add(car);
        car.setOwner(this);
    }

    public void removeCar(Car car) {
        cars.remove(car);
        car.setOwner(null);
    }
}
