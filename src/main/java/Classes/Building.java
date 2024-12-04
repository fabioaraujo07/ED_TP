package Classes;

import Collections.Lists.LinkedUnorderedList;
import Import.ImportJSON;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Building {

    private ImportJSON imprt;
    private Goal goal;
    private Map<Division> map;
    private LinkedUnorderedList<Division> inAndOut;

    public Building(String file) {

        this.imprt = new ImportJSON();
        this.goal = imprt.importGoal(file);
        this.map = imprt.importBuilding(file);
        this.inAndOut = imprt.importInAndOut(file);
    }

    public Goal getGoal() {
        return this.goal;
    }

    public Map<Division> getMap() {
        return this.map;
    }

    public LinkedUnorderedList<Division> getInAndOut() {
        LinkedUnorderedList<Division> tmp = new LinkedUnorderedList<>();

        for (Division division : this.inAndOut) {
            tmp.addToFront(division);
        }
        return tmp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Building:\n");
        sb.append("  Goal: ").append(goal).append("\n");

        sb.append("  Map:\n");
        for (int i = 0; i < map.getNumVertices(); i++) {
            sb.append("    ").append(map.getVertex(i)).append("\n");
        }

        sb.append("\n  Adjacency Matrix:\n");
        boolean[][] matrix = map.getMatrix();
        for (int i = 0; i < map.getNumVertices(); i++) {
            for (int j = 0; j < map.getNumVertices(); j++) {
                sb.append(matrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }

        sb.append("  Entrances/Exits:\n");
        for (Division division : inAndOut) {
            sb.append("    ").append(division.getName()).append("\n");
        }

        return sb.toString();
    }

}
