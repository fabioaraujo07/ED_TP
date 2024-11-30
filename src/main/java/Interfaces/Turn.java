package Interfaces;

import Classes.Division;
import Classes.Map;

public interface Turn<T> {

    public void atack(T t);

    public void move(T t, Map map, Division division);
}

