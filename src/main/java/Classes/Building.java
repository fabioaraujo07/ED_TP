package Classes;

import Collections.Lists.LinkedUnorderedList;
import Import.ImportJSON;

import java.util.LinkedHashMap;

public class Building {

    ImportJSON imprt;
    Goal goal;
    Map<Division> map;
    LinkedUnorderedList<Enemy> enemies;
    LinkedUnorderedList<Item> items;
    LinkedUnorderedList<Division> inAndOut;

    public Building(String file) {

        this.imprt = new ImportJSON();
        this.goal = imprt.importGoal(file);
        this.map = imprt.importBuilding(file);
        this.enemies = imprt.importEnemy(file);
        this.items = imprt.importItems(file);
        this.inAndOut = imprt.importInAndOut(file);
    }

    public Goal getGoal() {
        return this.goal;
    }

    public Map<Division> getMap() {
        Map<Division> tmp = new Map<>();

        for (Division division : this.map.getVertexes()) {
            tmp.addVertex(division);
        }
        boolean[][] matrix = this.map.getMatrix();
        for (int i = 0; i < this.map.getNumVertices(); i++) {
            for (int j = 0; j < this.map.getNumVertices(); j++) {
                if (matrix[i][j]) {
                    tmp.addEdge(this.map.getVertex(i), this.map.getVertex(j));
                }
            }
        }

        return tmp;
    }

    public LinkedUnorderedList<Enemy> getEnemies() {
        LinkedUnorderedList<Enemy> tmp= new LinkedUnorderedList<>();

        for (Enemy enemy : this.enemies) {
            tmp.addToFront(enemy);
        }
        return tmp;
    }

    public LinkedUnorderedList<Item> getItems() {
        LinkedUnorderedList<Item> tmp = new LinkedUnorderedList<>();

        for (Item item : this.items) {
            tmp.addToFront(item);
        }
        return tmp;
    }

    public LinkedUnorderedList<Division> getInAndOut() {
        LinkedUnorderedList<Division> tmp = new LinkedUnorderedList<>();

        for (Division division : this.inAndOut) {
            tmp.addToFront(division);
        }
        return tmp;
    }
}
