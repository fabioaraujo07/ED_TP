package Classes;

public class Division {

    private String name;
    //private boolean in_and_out;

    private int id;

    private static int nextId = 0;

    public Division(String name) {
        this.id = nextId++;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Division)) return false;
        Division division = (Division) o;
        return name.equalsIgnoreCase(division.name);
    }

    @Override
    public String toString() {
        return "Division{" +
                "name='" + name + '\'' +
                '}';
    }
}
