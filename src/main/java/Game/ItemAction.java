package Game;

import Classes.Item;
import Classes.ToCruz;
import Interfaces.Action;

/**
 * Represents an action where the player uses an item.
 */
public class ItemAction implements Action {

    private ToCruz player;
    private Item item;

    /**
     * Constructs an ItemAction with the specified player.
     *
     * @param player the player character
     */
    public ItemAction(ToCruz player) {
        this.player = player;
        this.item = null;
    }

    /**
     * Executes the item action.
     *
     * @return true if the item was used, false otherwise
     */
    @Override
    public boolean execute() {
        return useItem();
    }

    /**
     * Uses an item from the player's bag.
     *
     * @return true if an item was used, false otherwise
     */
    private boolean useItem() {
        try {
            item = (Item) player.getBag().peek();
        } catch (Exception e) {
            // Handle exception if needed
        }
        if (item != null) {
            try {
                player.useItem(item);
            } catch (Exception e) {
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the item associated with this action.
     *
     * @return the item associated with this action
     */
    public Item getItem() {
        return item;
    }
}