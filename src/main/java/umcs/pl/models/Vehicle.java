package umcs.pl.models;


import java.util.Objects;

public abstract class Vehicle implements Cloneable {
    private String id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private boolean rented;

    public Vehicle(String id, String brand, String model, int year, double price, boolean rented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
    }

    public String toCsv() {
        return id + "," + brand + "," + model + "," + year + "," + price + "," + rented;
    }

    public String toString(String tuple) {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", rented=" + rented +
                (tuple != null ? ", " + tuple : "") +
                '}';
    }

    public String getId() {
        return id;
    }

    public void rent() {
        this.rented = true;
    }

    public void returnVehicle() {
        this.rented = false;
    }

    public boolean isRented() {
        return rented;
    }

    @Override
    public Vehicle clone() throws CloneNotSupportedException {
        Vehicle cloned = (Vehicle) super.clone();
        cloned.id = this.id;
        cloned.brand = this.brand;
        cloned.model = this.model;
        cloned.year = this.year;
        cloned.price = this.price;
        cloned.rented = this.rented;
        return cloned;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, model, year, price, rented);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return year == vehicle.year &&
                Double.compare(vehicle.price, price) == 0 &&
                id.equals(vehicle.id) &&
                brand.equals(vehicle.brand) &&
                model.equals(vehicle.model);
    }
}
