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
    private boolean usedItem;
    private Division from;
    private Division to;
    private Building building;
    private int option;
    LinkedUnorderedList<Division> neighbors;

    /**
     * Constructs a PlayerMoveAction with the specified player, building, and option.
     *
     * @param player the player performing the action
     * @param building the building in which the action takes place
     * @param option the option selected for the move
     */
    public PlayerMoveAction(ToCruz player, Building building, int option) {
        this.player = player;
        this.building = building;
        this.usedItem = false;
        this.option = option;
        this.neighbors = building.getMap().getEdges(player.getCurrentDivision());
        this.from = player.getCurrentDivision();
        this.to = null;

    }

    /**
     * Executes the player move action.
     *
     * @return true if the action was successful, false otherwise
     */
    @Override
    public boolean execute() {
        return PlayerMove();
    }

    /**
     * Moves the player to the target division based on the selected option.
     *
     * @return true if the move was successful, false otherwise
     * @throws InvalidAction if the selected option is invalid
     */
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
                    usedItem = true;
                }
                divisionItems.remove(item);
            }
        }
        return true;
    }

    /**
     * Finds the division corresponding to the selected option.
     *
     * @param neighbors the list of neighboring divisions
     * @param option the selected option
     * @return the division corresponding to the option, or null if not found
     */
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

    /**
     * Returns the division from which the player moved.
     *
     * @return the division from which the player moved
     */
    public Division getFrom() {
        return from;
    }

    /**
     * Returns the division to which the player moved.
     *
     * @return the division to which the player moved
     */
    public Division getTo() {
        return to;
    }

    /**
     * Returns whether an item was used during the move.
     *
     * @return true if an item was used, false otherwise
     */
    public boolean isUsedItem() {
        return usedItem;
    }
}
