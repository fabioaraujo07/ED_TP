package Game;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Exceptions.InvalidAction;

import java.util.Iterator;

public class CombatHandler {

    // Cenário 1: Ataque de inimigos
    public void scenario1(ToCruz player, Building building) {
        Division currentDivision = player.getCurrentDivision();
        LinkedUnorderedList<Enemy> currentEnemies = currentDivision.getEnemies();

        if (currentEnemies.isEmpty()) {
            throw new InvalidAction("No enemies to attack in the current division.");
        }

        // Player attacks enemies
        for (Enemy enemy : currentEnemies) {
            player.attack(enemy);
            System.out.println(player.getName() + " attacked " + enemy.getName() + ". Remaining life: " + enemy.getLifePoints());
        }

        // Remove defeated enemies
        Iterator<Enemy> iterator = currentEnemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (!enemy.isAlive()) {
                iterator.remove();
                System.out.println(enemy.getName() + " was defeated!");
            }
        }

        // Enemies counter-attack
        if (!currentEnemies.isEmpty()) {
            System.out.println(currentEnemies.size() + " enemy(s) are counter-attacking!");
            for (Enemy enemy : currentEnemies) {
                enemy.attackPlayer(player);
                System.out.println(enemy.getName() + " attacked causing " + enemy.getPower() + " damage to To Cruz.");
            }
            moveEnemy(player, building);
        }

        if (!player.isAlive()) {
            System.out.println("Player is dead. Game over.");
        }
    }

    // Cenário 2: Movimentação do jogador
    public void scenario2(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction {
        Division targetDivision = findDivisionByOption(neighbors, option);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }

        player.movePlayer(building.getMap(), targetDivision);
        moveEnemy(player, building);
    }

    // Cenário 3: Movimentação dos inimigos
    public void scenario3(ToCruz player, Building building) {
        for (Division division : building.getMap().getVertexes()) {
            LinkedUnorderedList<Enemy> enemies = division.getEnemies();
            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && !division.equals(player.getCurrentDivision())) {
                    Division randomNeighbor = getRandomDivision(division, building);
                    if (randomNeighbor != null) {
                        division.removeEnemy(enemy);
                        randomNeighbor.addEnemy(enemy);
                        System.out.println(enemy.getName() + " moved from " + division.getName() + " to " + randomNeighbor.getName());
                    }
                }
            }
        }
    }

    // Cenário 4: Uso de item pelo jogador
    public void scenario4(ToCruz player, Building building) {
        if (player.getBag().isEmpty()) {
            throw new InvalidAction("No items available in the bag.");
        }

        Item item = (Item) player.getBag().pop();
        player.useItem(player, item);
        System.out.println(player.getName() + " used " + item.getItems() + " and restored health.");
        moveEnemy(player, building);
    }

    // Cenário 5: Jogador encontra o objetivo, mas há inimigos
    public void scenario5(ToCruz player, Building building, Goal goal) {
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            System.out.println("To Cruz has not found the goal yet.");
            return;
        }

        LinkedUnorderedList<Enemy> enemies = player.getCurrentDivision().getEnemies();
        if (!enemies.isEmpty()) {
            System.out.println("Enemies in the division. Clearing the area first.");
            scenario1(player, building);
        }

        if (enemies.isEmpty()) {
            interactWithGoal(player, goal);
        }
    }

    // Cenário 6: Jogador encontra o objetivo sem inimigos
    public void scenario6(ToCruz player, Building building, Goal goal) {
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            System.out.println("To Cruz has not found the goal yet.");
            return;
        }

        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();
        if (!currentEnemies.isEmpty()) {
            System.out.println("Enemies in the division. Clearing the area first.");
            scenario1(player, building);
            return;
        }

        interactWithGoal(player, goal);
        if (player.isAlive()) {
            System.out.println("Mission Completed!");
        } else {
            System.out.println("To Cruz is dead. Mission failed.");
        }
    }

    // Interação com o objetivo
    private void interactWithGoal(ToCruz player, Goal goal) {
        System.out.println(player.getName() + " interacted with goal: " + goal.getType());
        goal.setRequired(true);
        System.out.println("Goal: " + goal.getType() + " successfully retrieved!");
    }

    private void moveEnemy(ToCruz player, Building building) {
        // Itera por todas as divisões do mapa
        for (Division division : building.getMap().getVertexes()) {
            LinkedUnorderedList<Enemy> enemies = division.getEnemies();

            // Se não houver inimigos, pula para a próxima divisão
            if (enemies.isEmpty()) {
                continue;
            }

            // Lista temporária para armazenar inimigos a serem movidos
            LinkedUnorderedList<Enemy> enemiesToMove = new LinkedUnorderedList<>();

            // Adiciona inimigos à lista temporária para movimentação
            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && !division.equals(player.getCurrentDivision())) {
                    enemiesToMove.addToRear(enemy);
                }
            }

            // Agora movemos os inimigos da lista temporária
            for (Enemy enemy : enemiesToMove) {
                Division newDivision = moveNPC(enemy, division, building);

                // Verifica se o inimigo foi movido para uma nova divisão
                if (!newDivision.equals(division)) {
                    // Remover o inimigo da divisão original e adicionar na nova
                    division.removeEnemy(enemy);
                    newDivision.addEnemy(enemy);
                    System.out.println(enemy.getName() + " moved from " + division.getName() + " to " + newDivision.getName());
                } else {
                    // Caso o inimigo não tenha se movido, ele permanece na mesma divisão
                    System.out.println(enemy.getName() + " stayed at " + newDivision.getName());
                }
            }
        }
    }

    public Division moveNPC(Enemy enemy, Division division, Building building) {
        // Determina o número de movimentos aleatórios para o inimigo
        int moves = (int) (Math.random() * 3);

        // Se o inimigo se mover, escolhe uma divisão aleatória
        if (moves > 0) {
            Division currentDivision = division;
            Division lastDivision = null;

            // Movimento do inimigo para a(s) nova(s) divisão(ões)
            for (int i = 0; i < moves; i++) {
                lastDivision = getRandomDivision(currentDivision, building);
            }

            return lastDivision; // Retorna a última divisão escolhida
        }

        // Caso contrário, o inimigo permanece na mesma divisão
        return division;
    }

    public Division getRandomDivision(Division division, Building building) {
        LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(division);

        // Se não houver divisões vizinhas, retorna a divisão atual
        if (neighbors.isEmpty()) {
            return division;
        }

        // Escolhe uma divisão aleatória entre as vizinhas
        int randomIndex = (int) (Math.random() * neighbors.size());
        Iterator<Division> iterator = neighbors.iterator();

        // Percorre até a divisão aleatória
        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }

        // Retorna a divisão aleatória escolhida
        return iterator.next();
    }


    // Escolher divisão com base na opção
    private Division findDivisionByOption(LinkedUnorderedList<Division> neighbors, int option) {
        int index = 1;
        for (Division division : neighbors) {
            if (index == option) {
                return division;
            }
            index++;
        }
        return null;
    }
}
