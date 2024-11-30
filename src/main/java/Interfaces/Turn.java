package Interfaces;

import Classes.Division;
import Classes.Enemy;
import Classes.Map;

public interface Turn<T> {

    public void atack(Enemy enemy);

    public void move(T t, Map map, Division division);
}

