package Game;

import Classes.Enemy;
import Classes.ToCruz;
import Collections.Linked.LinkedUnorderedList;
import Exceptions.InvalidAction;
import Interfaces.Action;

public class PlayerAtackAction implements Action {

    private ToCruz player;
    private LinkedUnorderedList<Enemy> enemies;
    private boolean atack;

    public PlayerAtackAction(ToCruz player) {
        this.player = player;
        this.enemies = player.getCurrentDivision().getEnemies();
        this.atack = false;
    }

    @Override
    public boolean execute() {
        return PlayerAtack();
    }

    private boolean PlayerAtack() {

        LinkedUnorderedList<Enemy> defeatedEnemies = new LinkedUnorderedList<>();

        if (enemies.isEmpty()) {
            throw new InvalidAction("No enemies to attack in the current division.");
        }

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                player.attack(enemy);
            }

            if (!enemy.isAlive()) {
                defeatedEnemies.addToRear(enemy);
            }
        }

        if (defeatedEnemies.size() == enemies.size()) {
            for (Enemy defeatedEnemy : defeatedEnemies) {
                enemies.remove(defeatedEnemy);
            }
        }
        atack = true;
        return atack;
    }

    public LinkedUnorderedList<Enemy> getEnemies() {
        return enemies;
    }
}
