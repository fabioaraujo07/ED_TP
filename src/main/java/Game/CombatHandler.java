package Game;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.InvalidAction;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CombatHandler {

    public CombatHandler() {
    }

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
        LinkedUnorderedList<Enemy> defeatedEnemies = new LinkedUnorderedList<>();

        for (Enemy enemy : currentEnemies) {
            if (!enemy.isAlive()) {
                defeatedEnemies.addToRear(enemy);
                System.out.println(enemy.getName() + "was defeated!");
            }
        }

        // Remove os inimigos derrotados fora do loop de iteração principal
        for (Enemy defeatedEnemy : defeatedEnemies) {
            currentEnemies.remove(defeatedEnemy);
        }

        // Enemies counter-attack
        if (!currentEnemies.isEmpty()) {
            scenario3(player, building);
        }

        if (!player.isAlive()) {
            throw new InvalidAction("To Cruz is dead!!\nMission Failed");
        }
    }

    // Cenário 2: Movimentação do jogador
    public void scenario2(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction {
        Division targetDivision = findDivisionByOption(neighbors, option);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }


        player.movePlayer(building.getMap(), targetDivision);

        LinkedUnorderedList<Item> divisionItems = player.getCurrentDivision().getItems();
        if (!divisionItems.isEmpty()) {
            for (Item item : divisionItems){
                player.addItem(item);
                if (item.getItems().equals(Items.COLETE)) {
                    player.useItem(item);
                }
                System.out.println(player.getCurrentDivision().getName() + "picked " + item.getItems() + " and added it to the bag.");
            }
        }

        moveEnemy(player, building);
    }

    // Cenário 3: Movimentação dos inimigos
    public void scenario3(ToCruz player, Building building) {
        moveEnemy(player, building);
    // Enemies counter-attack
        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();

        if (!currentEnemies.isEmpty()) {
            for (Enemy enemy : currentEnemies) {
                if (enemy.getLifePoints() == enemy.getPower()) {
                    System.out.println(enemy.getName() + " enemy(s) are counter-attacking!");
                    enemy.attackPlayer(player);
                    System.out.println(enemy.getName() + " attacked causing " + enemy.getPower() + " damage to To Cruz.");
                }
            }
        }

        if (!player.isAlive()) {
            throw new InvalidAction("To Cruz is dead!!\nMission Failed");
        }
    }

    // Cenário 4: Uso de item pelo jogador
    public void scenario4(ToCruz player, Building building) {
        if (player.getBag().isEmpty()) {
            throw new InvalidAction("No items available in the bag.");
        }

        Item item = (Item) player.getBag().peek();
        player.useItem(item);
        System.out.println(player.getName() + " used " + item.getItems() + " and restored health.");

        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();
        //Enimigo ataca se item for usado em batalha
        if (!currentEnemies.isEmpty()) {
            System.out.println(currentEnemies.size() + " enemy(s) are counter-attacking!");
            for (Enemy enemy : currentEnemies) {
                enemy.attackPlayer(player);
                System.out.println(enemy.getName() + " attacked causing " + enemy.getPower() + " damage to To Cruz.");
            }
        }
        scenario3(player, building);
    }

    // Cenário 5: Jogador encontra o objetivo, mas há inimigos Não necessario
    public void scenario5(ToCruz player, Building building, Goal goal) {
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
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
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();
        if (!currentEnemies.isEmpty()) {
            System.out.println("Enemies in the division. Clearing the area first.");
            scenario1(player, building);
            return;
        }

        interactWithGoal(player, goal);
        System.out.println("Get out of the building!");
    }

    public Division startDivision(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction {
        Division targetDivision = findDivisionByOption(neighbors, option);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }

        player.setDivision(targetDivision);
        moveEnemy(player, building);

        return targetDivision;
    }

    // Interação com o objetivo
    private void interactWithGoal(ToCruz player, Goal goal) {
        System.out.println(player.getName() + " interacted with goal: " + goal.getType());
        goal.setRequired(true);
        System.out.println("Goal: " + goal.getType() + " successfully retrieved!");
    }

    private void moveEnemy(ToCruz player, Building building) {
        LinkedUnorderedList<Enemy> enemiesMoved = new LinkedUnorderedList<>();
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

                try {
                    enemiesMoved.contains(enemy);
                    continue;
                } catch (NoSuchElementException e) {
                }

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
                enemiesMoved.addToFront(enemy);
            }
        }
    }

    public Division moveNPC(Enemy enemy, Division division, Building building) {
        // Determina o número de movimentos aleatórios para o inimigo
        int moves = (int) (Math.random() * 3);

        // Se o inimigo se mover, escolhe uma divisão aleatória
        if (moves > 0) {
            Division currentDivision = division;

            // Movimento do inimigo para a(s) nova(s) divisão(ões)
            for (int i = 0; i < moves; i++) {
                currentDivision = getRandomDivision(currentDivision, building);
            }

            return currentDivision; // Retorna a última divisão escolhida
        }

        // Caso contrário, o inimigo permanece na mesma divisão
        return division;
    }

    private Division getRandomDivision(Division division, Building building) {
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
