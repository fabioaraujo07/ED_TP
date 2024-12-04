package Test;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Import.ImportJSON;

public class ImportJSONTest {
    public static void main(String[] args) {

        ImportJSON imprt = new ImportJSON();
        Map<Division> m;
//        LinkedUnorderedList<Item> i = new LinkedUnorderedList<>();
        LinkedUnorderedList<Division> io = new LinkedUnorderedList<>();
        String file = "src/main/resources/Missão.json";

        Building building = new Building(file);
        System.out.printf(building.toString());

//        System.out.println();
//        System.out.println("Test Getters");
//
//        m = building.getMap();
//        System.out.println(m);
//
//        for (int i = 0; i < m.getNumVertices(); i++) {
//            Division division = m.getVertex(i);
//            System.out.println("Division: " + division.getName());
//
//            // Listar inimigos associados
//            for (Enemy enemy : division.getEnemies()) {
//                System.out.println("  Enemy: " + enemy.getName() + ", Power: " + enemy.getPower());
//            }
//        }

//        i = building.getItems();
//        System.out.println(i);
//
//        Goal g = building.getGoal();
//        System.out.println(g);
//
//        io = building.getInAndOut();
//        System.out.println(io);
    }
}
