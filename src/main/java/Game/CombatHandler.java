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
    public String scenario1(ToCruz player, Building building) {
        StringBuilder log = new StringBuilder();
        Division currentDivision = player.getCurrentDivision();
        LinkedUnorderedList<Enemy> currentEnemies = currentDivision.getEnemies();

        if (currentEnemies.isEmpty()) {
            throw new InvalidAction("No enemies to attack in the current division.");
        }

        // Player attacks enemies
        for (Enemy enemy : currentEnemies) {
            player.attack(enemy);
            log.append(player.getName()).append(" attacked ").append(enemy.getName()).append(". Remaining life: ").append(enemy.getLifePoints()).append("\n");
        }

        // Remove defeated enemies
        LinkedUnorderedList<Enemy> defeatedEnemies = new LinkedUnorderedList<>();

        for (Enemy enemy : currentEnemies) {
            if (!enemy.isAlive()) {
                defeatedEnemies.addToRear(enemy);
                log.append(enemy.getName()).append(" was defeated!\n");
            }
        }

        // Remove os inimigos derrotados fora do loop de iteração principal
        for (Enemy defeatedEnemy : defeatedEnemies) {
            currentEnemies.remove(defeatedEnemy);
        }

        // Enemies counter-attack
        log.append(scenario3(player, building));

        if (!player.isAlive()) {
            throw new InvalidAction("To Cruz is dead!!\nMission Failed");
        }

        return log.toString();
    }

    // Cenário 2: Movimentação do jogador
    public String scenario2(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction {
        StringBuilder log = new StringBuilder();
        Division targetDivision = findDivisionByOption(neighbors, option);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }

        player.movePlayer(building.getMap(), targetDivision);
        log.append(player.getName()).append(" moved to ").append(player.getCurrentDivision().getName()).append("\n");

        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();

        if (!currentEnemies.isEmpty()) {
            for (Enemy enemy : currentEnemies) {
                enemy.attackPlayer(player);
                log.append(enemy.getName()).append(" attacked ").append(player.getName()).append("\n");
            }
        }

        LinkedUnorderedList<Item> divisionItems = player.getCurrentDivision().getItems();
        if (!divisionItems.isEmpty()) {
            for (Item item : divisionItems){
                player.addItem(item);
                if (item.getItems().equals(Items.COLETE)) {
                    player.useItem(item);
                }
                log.append(player.getCurrentDivision().getName()).append(" picked ").append(item.getItems()).append(" and added it to the bag.\n");
            }
        }
        log.append(moveEnemy(player, building));
        return log.toString();
    }

    // Cenário 3: Movimentação dos inimigos
    public String scenario3(ToCruz player, Building building) {
        return moveEnemy(player, building);
    }

    // Cenário 4: Uso de item pelo jogador
    public String scenario4(ToCruz player, Building building) {
        if (player.getBag().isEmpty()) {
            throw new InvalidAction("No items available in the bag.");
        }

        Item item = (Item) player.getBag().peek();
        player.useItem(item);
        String log = player.getName() + " used " + item.getItems() + " and restored health.\n";

        log += scenario3(player, building);
        return log;
    }

    // Cenário 5: Jogador encontra o objetivo, mas há inimigos
    public String scenario5(ToCruz player, Building building, Goal goal) {
        StringBuilder log = new StringBuilder();
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();
        if (!currentEnemies.isEmpty()) {
            for(Enemy enemy : currentEnemies) {
                if (enemy.isAlive()) {
                    enemy.attackPlayer(player);
                    log.append("Encountered enemy ").append(enemy.getName()).append("! Took ").append(enemy.getPower()).append(" damage. Remaining life: ").append(player.getLifePoints()).append("\n");
                }
            }
            log.append(scenario1(player, building));
            return log.toString();
        }

        if (currentEnemies.isEmpty()) {
            log.append(interactWithGoal(player, goal));
        }
        return log.toString();
    }

    // Cenário 6: Jogador encontra o objetivo sem inimigos
    public String scenario6(ToCruz player, Building building, Goal goal) {
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        return interactWithGoal(player, goal) + "Get out of the building!\n";
    }

    public Division startDivision(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction {
        Division targetDivision = findDivisionByOption(neighbors, option);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }

        player.setDivision(targetDivision);
        moveEnemy(player, building);

        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();

        if (!currentEnemies.isEmpty()) {
            for (Enemy enemy : currentEnemies) {
                enemy.attackPlayer(player);
            }
        }

        return targetDivision;
    }

    // Interação com o objetivo
    private String interactWithGoal(ToCruz player, Goal goal) {
        goal.setRequired(true);
        return player.getName() + " interacted with goal: " + goal.getType() + "\nGoal: " + goal.getType() + " successfully retrieved!\n";
    }

    private String moveEnemy(ToCruz player, Building building) {
        StringBuilder log = new StringBuilder();
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
                    log.append(enemy.getName()).append(" moved from ").append(division.getName()).append(" to ").append(newDivision.getName()).append("\n");

                } else {
                    // Caso o inimigo não tenha se movido, ele permanece na mesma divisão
                    log.append(enemy.getName()).append(" stayed at ").append(newDivision.getName()).append("\n");
                }
                if (newDivision.equals(player.getCurrentDivision())) {
                    log.append(enemy.getName()).append(" encountered To Cruz and is attacking!\n");
                    enemy.attackPlayer(player);
                    log.append(enemy.getName()).append(" attacked causing ").append(enemy.getPower()).append(" damage to To Cruz.\n");
                }
                enemiesMoved.addToFront(enemy);
            }
        }
        return log.toString();
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