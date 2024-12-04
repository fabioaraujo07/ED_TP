package Classes;

import Collections.Lists.LinkedUnorderedList;

public class Division {

    private String name;
    //private boolean in_and_out;
    LinkedUnorderedList<Enemy> enemies;


    public Division(String name) {
        this.enemies = new LinkedUnorderedList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
