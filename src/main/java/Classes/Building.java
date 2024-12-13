package Classes;

import Collections.Graph.Graph;
import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Import.ImportJSON;
import Exceptions.ImportException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Building {

    private ImportJSON imprt;
    private Goal goal;
    private Map<Division> map;
    private LinkedUnorderedList<Division> inAndOut;

    public Building(String file) {
        try {
            this.imprt = new ImportJSON(file);
            this.goal = imprt.importGoal();
            this.map = imprt.importBuilding();
            this.inAndOut = imprt.getInAndOut();
        } catch (ImportException e) {
            e.printStackTrace();
        }
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

    public String getFilename() {
        return imprt.getFilename();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Building:\n");
        sb.append("  Goal: ").append(goal).append("\n");

        sb.append("  Map:\n");
        for (int i = 0; i < map.size(); i++) {
            sb.append("    ").append(map.getVertex(i)).append("\n");
        }

        sb.append("\n  Adjacency Matrix:\n");
        double[][] matrix = map.getMatrix();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.size(); j++) {
                sb.append(matrix[i][j] > 0 ? "1 " : "0 ");
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