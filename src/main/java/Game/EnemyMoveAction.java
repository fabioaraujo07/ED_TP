package Game;

import Classes.Building;
import Classes.Division;
import Classes.Enemy;
import Classes.ToCruz;
import Collections.Array.UnorderedArrayList;
import Collections.Linked.LinkedUnorderedList;
import Interfaces.Action;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EnemyMoveAction implements Action {

    private ToCruz player;
    private Building building;
    private boolean move;
    private UnorderedArrayList<Division> from;
    private UnorderedArrayList<Division> to;

    public EnemyMoveAction(ToCruz player, Building building) {
        this.player = player;
        this.building = building;
        this.move = false;
        this.from = new UnorderedArrayList<>();
        this.to = new UnorderedArrayList<>();
    }

    @Override
    public boolean execute() {
        return moveEnemy();
    }

    private boolean moveEnemy() {
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

                from.addToRear(division);
                Division newDivision = moveNPC(division, building);
                to.addToRear(newDivision);
                // Verifica se o inimigo foi movido para uma nova divisão
                if (!newDivision.equals(division)) {
                    // Remover o inimigo da divisão original e adicionar na nova
                    division.removeEnemy(enemy);
                    newDivision.addEnemy(enemy);
                }
                if (newDivision.equals(player.getCurrentDivision())) {
                    enemy.attackPlayer(player);
                }
                enemiesMoved.addToFront(enemy);
            }
        }
        move = true;
        return move;
    }

    public Division moveNPC(Division division, Building building) {
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

    public UnorderedArrayList<Division> getFrom() {
        return from;
    }

    public UnorderedArrayList<Division> getTo() {
        return to;
    }

}
