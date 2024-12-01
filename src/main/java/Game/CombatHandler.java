package Game;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Collections.Lists.UnorderedArrayList;

public class CombatHandler {

    public void Scenario1(ToCruz player, Building building){

        LinkedUnorderedList<Enemy> currentEnemies = getEnemiesInDivision(player, building.getEnemies());

        if (currentEnemies.isEmpty()){
            System.out.println("No enemies found");
            return;
        }

        System.out.println("Enemies found in" + player.getCurrentDivision());

        for (Enemy enemy: currentEnemies){
            player.attack(enemy);
            System.out.println(player.getName() + "attacked" + enemy.getName() + "remaining life: " + enemy.getLifePoints());
        }

        for (Enemy enemy : currentEnemies){
            if (!enemy.isAlive()){
                currentEnemies.remove(enemy);
            }else {
                System.out.println(enemy.getName() + "was defeated!");
            }
        }

        if(!currentEnemies.isEmpty()){
            System.out.println("Left enemie(s) attacking");
            for (Enemy enemy: currentEnemies){
                enemy.attackPlayer(player);
                System.out.println(enemy.getName() + "attacked causing" + enemy.getPower() + "damage to To Cruz");
            }

            for (Enemy enemy: building.getEnemies()){
                if (!enemy.getDivision().equals(player.getCurrentDivision()) && enemy.isAlive()){
                    Division randomNeighbor = getRandomNeighbor(building, enemy.getDivision()) ;
                    if (randomNeighbor != null){
                        enemy.setDivision(randomNeighbor);
                    }
                }
            }
        }

        if (!player.isAlive()){
            System.out.println("Player is dead");
        }

    }

    private LinkedUnorderedList<Enemy> getEnemiesInDivision(ToCruz player, LinkedUnorderedList<Enemy> allEnemies){
        LinkedUnorderedList<Enemy> enemiesInDivision = new LinkedUnorderedList<>();
        for (Enemy enemy: allEnemies){
            if (enemy.getDivision().equals(player.getCurrentDivision()) && enemy.isAlive()){
                enemiesInDivision.addToRear(enemy);
            }
        }
        return enemiesInDivision;
    }

    private Division getRandomNeighbor(Building building, Division currentDivision){
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

}
