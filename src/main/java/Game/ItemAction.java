package Game;

import Classes.Item;
import Classes.ToCruz;
import Interfaces.Action;

public class ItemAction implements Action {

    private ToCruz player;
    private Item item;

    public ItemAction(ToCruz player) {
        this.player = player;
        this.item = (Item) player.getBag().peek();
    }

    @Override
    public boolean execute() {
        return useItem();
    }

    private boolean useItem(){
        if (item != null){
            player.useItem(item);
            return true;
        }
        return false;
    }

    public Item getItem() {
        return item;
    }
}
