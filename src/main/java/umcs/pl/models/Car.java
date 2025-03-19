package umcs.pl.models;

public class Car extends Vehicle {
    public Car(String id, String brand, String model, int year, double price, boolean rented) {
        super(id, brand, model, year, price, rented);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }

    @Override
    public Car clone() throws CloneNotSupportedException {
        return (Car) super.clone();
    }
}
