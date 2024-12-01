package Test;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Import.ImportJSON;

public class ImportJSONTest {
    public static void main(String[] args) {

        ImportJSON imprt = new ImportJSON();
        Map<Division> m;
        LinkedUnorderedList<Enemy> e = new LinkedUnorderedList<>();
        LinkedUnorderedList<Item> i = new LinkedUnorderedList<>();
        LinkedUnorderedList<Division> io = new LinkedUnorderedList<>();
        String file = "src/main/resources/Missão.json";

        Building building = new Building(file);
        System.out.printf(building.toString());

        m = building.getMap();
        System.out.println(m);

        e = building.getEnemies();
        System.out.println(e);

        i = building.getItems();
        System.out.println(i);

        Goal g = building.getGoal();
        System.out.println(g);

        io = building.getInAndOut();
        System.out.println(io);
    }
}
