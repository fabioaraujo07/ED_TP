package Classes;

import Interfaces.Turn;

public class PlayerTurn<T> extends ToCruz implements Turn<T> {

    public PlayerTurn(String name, Division currentDivision) {
        super(name, currentDivision);
    }

    @Override
    public void atack(T t) {

    }

    @Override
    public void move(T t, Map map, Division division) {

    }
}
