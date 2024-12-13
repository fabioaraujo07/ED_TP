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
    private UnorderedArrayList<Division> from;
    private UnorderedArrayList<Division> to;

    public EnemyMoveAction(ToCruz player, Building building) {
        this.player = player;
        this.building = building;
        this.from = new UnorderedArrayList<>();
        this.to = new UnorderedArrayList<>();
    }

    @Override
    public boolean execute() {
        return moveEnemy();
    }

    private boolean moveEnemy() {
        LinkedUnorderedList<Enemy> enemiesMoved = new LinkedUnorderedList<>();

        for (Division division : building.getMap().getVertexes()) {
            LinkedUnorderedList<Enemy> enemies = division.getEnemies();
            if (enemies.isEmpty()) {
                continue;
            }

            LinkedUnorderedList<Enemy> enemiesToMove = new LinkedUnorderedList<>();
            for (Enemy enemy : enemies) {
                if (enemy.isAlive() && !division.equals(player.getCurrentDivision())) {
                    enemiesToMove.addToRear(enemy);
                }
            }

            for (Enemy enemy : enemiesToMove) {
                try {
                    enemiesMoved.contains(enemy);
                    continue;
                } catch (NoSuchElementException e) {
                }

                Division newDivision = moveNPC(division, building);
                from.addToRear(division);
                to.addToRear(newDivision);
                if (!newDivision.equals(division)) {
                    division.removeEnemy(enemy);
                    newDivision.addEnemy(enemy);
                }
                if (newDivision.equals(player.getCurrentDivision())) {
                    enemy.attackPlayer(player);
                }
                enemiesMoved.addToFront(enemy);
            }
        }
        return true;
    }

    public Division moveNPC(Division division, Building building) {
        int moves = (int) (Math.random() * 3);

        if (moves > 0) {

            Division currentDivision = division;
            for (int i = 0; i < moves; i++) {
                currentDivision = getRandomDivision(currentDivision, building);
            }
            return currentDivision;
        }

        return division;
    }

    private Division getRandomDivision(Division division, Building building) {
        LinkedUnorderedList<Division> neighbors = building.getMap().getEdges(division);

        if (neighbors.isEmpty()) {
            return division;
        }

        int randomIndex = (int) (Math.random() * neighbors.size());
        Iterator<Division> iterator = neighbors.iterator();
        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    public UnorderedArrayList<Division> getFrom() {
        return from;
    }

    public UnorderedArrayList<Division> getTo() {
        return to;
    }
}
