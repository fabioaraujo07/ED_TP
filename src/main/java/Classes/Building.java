package Classes;

import Collections.Lists.LinkedUnorderedList;
import Enumerations.Items;
import Import.ImportJSON;
import Exceptions.ImportException;
import Collections.Graph.Graph;

import java.util.Iterator;

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
            // Handle the exception appropriately
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

    public Division findNearestRecoveryKit(Division start) {
    Iterator<Division> iterator = map.iteratorBFS(start);
    while (iterator.hasNext()) {
        Division division = iterator.next();
        LinkedUnorderedList<Item> items = division.getItems();
        for (Item item : items) {
            if (item.getItems() == Items.KIT_VIDA) {
                return division;
            }
        }
    }
    return null;
}

    public void displayPaths(Division start) {
        Iterator<Division> pathToGoal = map.iteratorShortestPath(start, goal.getDivision());
        Division nearestRecoveryKit = findNearestRecoveryKit(start);
        Iterator<Division> pathToRecoveryKit;

        if (nearestRecoveryKit != null) {
            pathToRecoveryKit = map.iteratorShortestPath(start, nearestRecoveryKit);
        } else {
            pathToRecoveryKit = new LinkedUnorderedList<Division>().iterator();
        }

        System.out.println("\nBest path to the goal:");
        while (pathToGoal.hasNext()) {
            System.out.print(" -> " + pathToGoal.next().getName());
        }

        System.out.println("\n\nBest path to the nearest recovery kit:");
        while (pathToRecoveryKit.hasNext()) {
            System.out.print(" -> " + pathToRecoveryKit.next().getName());
        }
        System.out.println();
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
        boolean[][] matrix = map.getMatrix();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.size(); j++) {
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