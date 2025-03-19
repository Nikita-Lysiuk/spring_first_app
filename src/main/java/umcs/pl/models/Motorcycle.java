package umcs.pl.models;

import java.util.Objects;

public class Motorcycle extends Vehicle {
    private String category;

    public Motorcycle(String id, String brand, String model, int year, double price, boolean rented, String category) {
        super(id, brand, model, year, price, rented);
        this.category = category;
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + category;
    }

    @Override
    public String toString() {
        return super.toString("category='" + category + '\'');
    }

    @Override
    public Motorcycle clone() throws CloneNotSupportedException {
        return (Motorcycle) super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !super.equals(o)) return false;
        return category.equals(((Motorcycle) o).category);
    }
}
