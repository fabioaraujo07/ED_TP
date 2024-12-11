package Classes;


import Collections.Linked.LinkedUnorderedList;

public class Division {

    private String name;
    private LinkedUnorderedList<Enemy> enemies;
    private LinkedUnorderedList<Item> items;

    public Division(String name) {
        this.enemies = new LinkedUnorderedList<>();
        this.items = new LinkedUnorderedList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnemies(LinkedUnorderedList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public LinkedUnorderedList<Enemy> getEnemies() {
        return enemies;
    }

    public LinkedUnorderedList<Item> getItems() {
        return items;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.addToRear(enemy);
    }

    public void addItem(Item item) {
        this.items.addToRear(item);
    }

    public void removeEnemy(Enemy enemy){
        this.enemies.remove(enemy);
    }

    public int calculateTotalDamage() {
        int totalDamage = 0;
        for (Enemy enemy : enemies) {
            totalDamage += enemy.getPower();
        }
        return totalDamage;
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
        StringBuilder sb = new StringBuilder();
        sb.append("Division{name='").append(name).append("', ");

        sb.append("enemies=[");
        for (Enemy enemy : enemies) {
            sb.append(enemy.getName()).append("(Power: ").append(enemy.getPower()).append("), ");
        }
        if (!enemies.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove última vírgula
        }
        sb.append("], ");


        sb.append("items=[");
        for (Item item : items) {
            sb.append(item.getItems()).append("(Points: ").append(item.getPoints()).append("), ");
        }
        if (!items.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]}");

        return sb.toString();
    }

}
