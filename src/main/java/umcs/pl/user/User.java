package umcs.pl.user;

import umcs.pl.models.Vehicle;

public class User implements Cloneable {
    private String id;
    private String login;
    private String password;
    private Role role;
    private Vehicle rentedVehicle;

    public User(String id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;

        this.rentedVehicle = null;
    }

    public User(String id, String login, String password, Role role, Vehicle rentedVehicle) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.rentedVehicle = rentedVehicle;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        User cloned = (User) super.clone();
        cloned.id = this.id;
        cloned.login = this.login;
        cloned.password = this.password;
        cloned.role = this.role;
        cloned.rentedVehicle = this.rentedVehicle != null ? (Vehicle) this.rentedVehicle.clone() : null;
        return cloned;
    }

    public String toCsv() {
        return id + "," + login + "," + password + "," + role + "," + (rentedVehicle != null ? rentedVehicle.getId() : "");
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                (rentedVehicle != null ? ", rentedVehicle=" + rentedVehicle : "") +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Vehicle getRentedVehicle() {
        return rentedVehicle;
    }

    public void setRentedVehicle(Vehicle rentedVehicle) {
        this.rentedVehicle = rentedVehicle;
    }
}
