package Game;

import Classes.Item;
import Classes.ToCruz;
import Interfaces.Action;

public class ItemAction implements Action {

    private ToCruz player;
    private Item item;

    public ItemAction(ToCruz player) {
        this.player = player;
        this.item = null;
    }

    @Override
    public boolean execute() {
        return useItem();
    }

    private boolean useItem() {
        try {
            item = (Item) player.getBag().peek();
        } catch (Exception e) {
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

    public Item getItem() {
        return item;
    }
}
