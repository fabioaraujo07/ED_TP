package Game;

import Classes.*;

import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Interfaces.Action;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CombatHandler {

    public CombatHandler() {
    }

    // Cenário 1: Ataque de inimigos
    public LinkedUnorderedList<Action> scenario1(ToCruz player, Building building) throws InvalidAction {

        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Action playerAction = new PlayerAtackAction(player);
        Action enemyAction = new EnemyAtackAction(player);

        try {
            playerAction.execute();
            actions.addToRear(playerAction);
            if (enemyAction.execute()) {
                actions.addToRear(enemyAction);
                enemyAction = new EnemyMoveAction(player, building);
                enemyAction.execute();
                actions.addToRear(enemyAction);
            }

        } catch (InvalidAction e) {
        }

        if (!player.isAlive()) {
            throw new InvalidAction("To Cruz is dead!!\nMission Failed");
        }
        return actions;

    }

    // Cenário 2: Movimentação do jogador
    public LinkedUnorderedList<Action> scenario2(ToCruz player, Building building, int option) throws InvalidAction {

        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Action playerAction = new PlayerMoveAction(player, building, option);
        Action enemyAction = new EnemyMoveAction(player, building);
        LinkedUnorderedList<Enemy> currentEnemies = player.getCurrentDivision().getEnemies();

        try {
            playerAction.execute();
            actions.addToRear(playerAction);
            enemyAction.execute();
            actions.addToRear(enemyAction);
        } catch (InvalidAction e) {
        }

        if (!player.isAlive()) {
            throw new InvalidAction("To Cruz is dead!!\nMission Failed");
        }
        return actions;

    }

    // Cenário 3: Movimentação dos inimigos
    public LinkedUnorderedList<Action> scenario3(ToCruz player, Building building) {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Action enemyAction = new EnemyMoveAction(player, building);

        try {
            enemyAction.execute();
            actions.addToRear(enemyAction);
        } catch (InvalidAction e) {
        }
        return actions;
    }

    // Cenário 4: Uso de item pelo jogador
public LinkedUnorderedList<Action> scenario4(ToCruz player, Building building) throws InvalidAction {
    LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
    ItemAction itemAction = new ItemAction(player);

    if (!itemAction.execute()) {
        throw new InvalidAction("No items available in the bag.");
    }

    actions.addToRear(itemAction);
    LinkedUnorderedList<Action> scenario3Actions = scenario3(player, building);
    for (Action action : scenario3Actions) {
        actions.addToRear(action);
    }

    return actions;
}

    // Cenário 5: Jogador encontra o objetivo, mas há inimigos
    public LinkedUnorderedList<Action> scenario5(ToCruz player, Building building, Goal goal) throws InvalidAction {
        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();

        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        LinkedUnorderedList<Action> scenario1Actions = scenario1(player, building);
        for (Action action : scenario1Actions) {
            actions.addToRear(action);
        }

        actions.addToRear(new GoalInteractionAction(player, goal));
        return actions;
    }

    // Cenário 6: Jogador encontra o objetivo sem inimigos
    public LinkedUnorderedList<Action> scenario6(ToCruz player, Building building, Goal goal) {

        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        GoalInteractionAction playerAction = new GoalInteractionAction(player, goal);
        if (!player.getCurrentDivision().equals(goal.getDivision())) {
            throw new InvalidAction("To Cruz has not found the goal yet.");
        }

        playerAction.execute();
        actions.addToRear(playerAction);
        return actions;
    }

    public LinkedUnorderedList<Action> startDivision(ToCruz player, Building building, LinkedUnorderedList<Division> neighbors, int option) throws InvalidAction {

        LinkedUnorderedList<Action> actions = new LinkedUnorderedList<>();
        Division targetDivision = findDivisionByOption(neighbors, option);
        EnemyMoveAction enemyAction = new EnemyMoveAction(player, building);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }

        player.setDivision(targetDivision);
        enemyAction.execute();
        actions.addToRear(enemyAction);

        return actions;
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