package Interfaces;

import Classes.Division;
import Classes.Enemy;
import Classes.Item;
import Classes.Map;
import Classes.ToCruz;

public interface PlayerADT<T>{

    public void addItem(Item item);

    public void movePlayer(Map map, Division division);

    public void useItem(Item item);

    public void attack (Enemy enemy);

}
