package Game;

import Classes.Building;
import Classes.Division;
import Classes.Item;
import Classes.ToCruz;
import Collections.Linked.LinkedUnorderedList;
import Enumerations.Items;
import Exceptions.InvalidAction;
import Interfaces.Action;

public class PlayerMoveAction implements Action {

    private ToCruz player;
    private boolean move;
    private Division from;
    private Division to;
    private Building building;
    private int option;
    LinkedUnorderedList<Division> neighbors;

    public PlayerMoveAction(ToCruz player, Building building, int option) {
        this.player = player;
        this.building = building;
        this.move = false;
        this.to = null;
        this.from = null;
        this.option = option;
        this.neighbors = building.getMap().getEdges(player.getCurrentDivision());
        this.from = player.getCurrentDivision();
        this.to = null;

    }

    @Override
    public boolean execute() {
        return PlayerMove();
    }

    private boolean PlayerMove() throws InvalidAction {
        Division targetDivision = findDivisionByOption(neighbors, option);

        if (targetDivision == null) {
            throw new InvalidAction("Invalid option selected. No movement occurred.");
        }

        player.movePlayer(building.getMap(), targetDivision);
        to = targetDivision;

        LinkedUnorderedList<Item> divisionItems = player.getCurrentDivision().getItems();
        if (!divisionItems.isEmpty()) {
            for (Item item : divisionItems){
                player.addItem(item);
                if (item.getItems().equals(Items.COLETE)) {
                    player.useItem(item);
                }
            }
        }
        move = true;
        return move;
    }

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

    public Division getFrom() {
        return from;
    }

    public Division getTo() {
        return to;
    }
}
