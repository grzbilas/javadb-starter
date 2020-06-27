package pl.sda.jdbc.zad4;

public class AnimalType {
    private int id;
    private String name;

    public AnimalType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AnimalType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
