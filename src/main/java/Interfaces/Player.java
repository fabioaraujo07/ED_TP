package Interfaces;

import Classes.*;

public interface Player<T>{

    public void addItem(Item item);

    public void movePlayer(ToCruz player, Map map, Division division);

    public void useItem(ToCruz player, Item item);

    public void atack(Enemy enemy);

}
