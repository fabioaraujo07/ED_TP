package Game;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Collections.Lists.UnorderedArrayList;
import Exceptions.InvalidAction;

public class CombatHandler {

    public void scenario1(ToCruz player, Building building) {

        LinkedUnorderedList<Enemy> currentEnemies = getEnemiesInDivision(player, building.getEnemies());

        if (currentEnemies.isEmpty()){
            throw new InvalidAction("No enemies to atack");
        }

        for (Enemy enemy: currentEnemies){
            player.attack(enemy);
            System.out.println(player.getName() + "attacked" + enemy.getName() + "remaining life: " + enemy.getLifePoints());
        }

        for (Enemy enemy : currentEnemies){
            if (!enemy.isAlive()){
                currentEnemies.remove(enemy);
                System.out.println(enemy.getName() + "was defeated!");
            }
        }

        if(!currentEnemies.isEmpty()){
            System.out.println(currentEnemies.size() + " enemie(s) attacking");
            for (Enemy enemy: currentEnemies){
                enemy.attackPlayer(player);
                System.out.println(enemy.getName() + "attacked causing" + enemy.getPower() + "damage to To Cruz");
            }
            moveEnemy(player, building);
        }

        if (!player.isAlive()){
            System.out.println("Player is dead");
        }

    }

    public LinkedUnorderedList<Enemy> getEnemiesInDivision(ToCruz player, LinkedUnorderedList<Enemy> allEnemies) {
        LinkedUnorderedList<Enemy> enemiesInDivision = new LinkedUnorderedList<>();
        for (Enemy enemy: allEnemies){
            if (enemy.getDivision().equals(player.getCurrentDivision()) && enemy.isAlive()){
                enemiesInDivision.addToRear(enemy);
            }
        }
        return enemiesInDivision;
    }

    private Division getRandomNeighbor(Building building, Division currentDivision) {
        LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(currentDivision);
        if (neighbors.isEmpty()){
            return null; // colocar exceção
        }
        int randomIndex = (int) (Math.random() * neighbors.size());
        int currentIndex = 0;

        for (Division division: neighbors){
            if (currentIndex == randomIndex){
                return division;
            }
            currentIndex++;
        }
        return null;
    }

    public void scenario2(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction{

        Division targetDivision = findDivisionByOption(neighbors, option);
        if (targetDivision != null) {
            player.movePlayer(building.getMap(), targetDivision);
        } else {
            throw new InvalidAction("Invalid option selected");
        }
        moveEnemy(player, building);
    }

    private static Division findDivisionByOption(LinkedUnorderedList<Division> neighbors, int option) {
        int i = 1;
        for (Division division : neighbors) {
            if (i == option) {
                return division;
            }
            else {
                i++;
            }
        }
        return null;
    }

    private void moveEnemy(ToCruz player, Building building) {
        for (Enemy enemy: building.getEnemies()){
            if (enemy.isAlive() && !enemy.getDivision().equals(player.getCurrentDivision())){
                Division randomNeighbor = getRandomNeighbor(building, enemy.getDivision());
                if (randomNeighbor != null){
                    System.out.println(enemy.getName() + " moved from " + enemy.getDivision().getName() + " to " + randomNeighbor.getName());
                    enemy.setDivision(randomNeighbor);
                }
            }
        }

        scenario3(player, building);
    }

    public void scenario3(ToCruz player, Building building){

        for (Enemy enemy : building.getEnemies()){
            if (enemy.isAlive() && !enemy.getDivision().equals(player.getCurrentDivision())){
                Division randomNeighbor = getRandomNeighbor(building, enemy.getDivision());
                if (randomNeighbor != null){
                    enemy.setDivision(randomNeighbor);
                }
            }
        }

        LinkedUnorderedList<Enemy> currentEnemies = getEnemiesInDivision(player, building.getEnemies());

        if (!currentEnemies.isEmpty()){
            System.out.println("Enemies enter in" + player.getCurrentDivision());
            for (Enemy enemy: currentEnemies){
                enemy.attackPlayer(player);
                System.out.println(enemy.getName() + "attack causing" + enemy.getPower() + "damage to To Cruz");
            }

            if (!player.isAlive()){
                System.out.println("Player is dead");
                return;
            }

            System.out.println(player.getName() + "Counter-attack");
            for (Enemy enemy : currentEnemies){
                player.attack(enemy);
                System.out.println(player.getName() + " attack " + enemy.getName() + ", enemy life: " + enemy.getLifePoints());
            }

            for (Enemy enemy: currentEnemies){
                if (!enemy.isAlive()){
                    currentEnemies.remove(enemy);
                    System.out.println(enemy.getName() + "is dead");
                }
            }
        }
    }

    public void scenario4(ToCruz player, Building building) {

        if (!player.getBag().isEmpty()) {

            Item item = (Item) player.getBag().pop();
            player.useItem(player, item);
            System.out.println(player.getName() + "used a life kit and restored health");
            moveEnemy(player, building);
        }
        else {
            throw new InvalidAction("No life kits in the bag");
        }
    }

    public void scenario5(ToCruz player, Building building, Goal goal) {

        if (!player.getCurrentDivision().equals(goal.getDivision())){
            System.out.println("Tó Cruz did not found the goal yet");
        }

        System.out.println("To Cruz did found the goal");

        LinkedUnorderedList<Enemy> enemies = getEnemiesInDivision(player, building.getEnemies());

        if (enemies.isEmpty()){
            System.out.println("No enemies entered, To Cruz interact with goal");
            interactWithGoal(player, goal);
            return;
        }

        System.out.println("Enemies in the division");
        scenario1(player, building);

        if (enemies.isEmpty()){
            System.out.println("No enemies entered, To Cruz interact with goal");
            interactWithGoal(player, goal);
            return;
        }

    }

    public void scenario6(ToCruz player, Building building, Goal goal) {
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            System.out.println("Tó Cruz did not found the goal yet");
        }

        LinkedUnorderedList<Enemy> currentEnemies = getEnemiesInDivision(player, building.getEnemies());

        if (!currentEnemies.isEmpty()) {
            System.out.println("Enemies in the division");
            scenario1(player, building);
            return;
        }

        System.out.println("No enemies in the division, To Cruz interact with goal");
        interactWithGoal(player, goal);

        if (player.isAlive()) {
            System.out.println("Mission Completed");
        } else {
            System.out.println("Tó Cruz is dead");
        }

    }

    private void interactWithGoal(ToCruz player, Goal goal) {
        System.out.println(player.getName() + " interact with goal" + goal.getType());
        goal.setRequired(true);
        System.out.println("Alvo: " + goal.getType() + " required with success");
    }

}
