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

    public void displayItemsLocations() {
        System.out.println("\nLocations of recovery kits and vests:");

        for (Division division : map.getVertexes()) {
            LinkedUnorderedList<Item> items = division.getItems();
            for (Item item : items) {
                if (item.getItems() == Items.KIT_VIDA) {
                    System.out.println("Recovery kit found in: " + division.getName());
                } else if (item.getItems() == Items.COLETE) {
                    System.out.println("Vest found in: " + division.getName());
                }
            }
        }
    }

    public void printMap(Division currentDivision) {
        System.out.println("\nBuilding Map:");

        for (Division division : map.getVertexes()) {
            if (division.equals(currentDivision)) {
                System.out.print("* " + division.getName() + " * -> ");
            } else {
                System.out.print("  " + division.getName() + " -> ");
            }

            LinkedUnorderedList<Division> neighbors = map.getEdges(division);
            for (Division neighbor : neighbors) {
                System.out.print(" -> " + neighbor.getName() + " ");
            }
            System.out.println();
        }
    }

    public PathResult simulatePath(ToCruz player, LinkedUnorderedList<Division> entries, Goal goal) {
        Graph<Division> map = this.getMap();
        LinkedUnorderedList<Division> bestPathToGoal = null;
        LinkedUnorderedList<Division> bestPathToExit = null;
        int bestLifePointsRemaining = 0;
        Division bestEntry = null;

        for (Division entry : entries) {
            LinkedUnorderedList<Division> tempPathToGoal = new LinkedUnorderedList<>();
            Iterator<Division> pathToGoal = map.iteratorShortestPath(entry, goal.getDivision());
            while (pathToGoal.hasNext()) {
                tempPathToGoal.addToRear(pathToGoal.next());
            }

            int damageToGoal = 0;
            LinkedUnorderedList<Division> clearedDivisions = new LinkedUnorderedList<>();
            for (Division division : tempPathToGoal) {
                damageToGoal += division.calculateTotalDamage();
                clearedDivisions.addToRear(division);
            }

            int lifePointsAfterGoal = player.getLifePoints() - damageToGoal;
            if (lifePointsAfterGoal <= 0) continue;

            LinkedUnorderedList<Division> tempPathToExit = new LinkedUnorderedList<>();
            Iterator<Division> pathToExit = map.iteratorShortestPath(goal.getDivision(), entry);
            while (pathToExit.hasNext()) {
                tempPathToExit.addToRear(pathToExit.next());
            }

            int damageToExit = 0;
            for (Division division : tempPathToExit) {
                if (!clearedDivisions.contains(division)) {
                    damageToExit += division.calculateTotalDamage();
                }
            }

            int lifePointsRemaining = lifePointsAfterGoal - damageToExit;

            if (lifePointsRemaining > bestLifePointsRemaining) {
                bestLifePointsRemaining = lifePointsRemaining;
                bestPathToGoal = tempPathToGoal;
                bestPathToExit = tempPathToExit;
                bestEntry = entry;
            }
        }

        boolean missionSuccess = bestLifePointsRemaining > 0;
        return new PathResult(bestPathToGoal, bestPathToExit, bestLifePointsRemaining, missionSuccess, bestEntry);
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