package Interfaces;

import Classes.Division;
import Classes.Item;

public interface Player<T>{

    public void addItem(Item item);

    public void removeItem(Item item);

    public void moveDivision(Division division);

    public void LifePointsChanged();



}
