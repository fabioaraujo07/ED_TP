package Interfaces;

import Classes.Division;
import Classes.Item;
import Classes.Map;
import Classes.ToCruz;

public interface Player<T>{

    public void addItem(Item item);

    public void movePlayer(ToCruz player, Map map, Division division);

    public void useItem(ToCruz player, Item item);



}
