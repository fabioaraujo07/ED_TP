package Classes;

import Interfaces.Turn;

public class EnemyTurn<T> extends Enemy implements Turn<T> {

    public EnemyTurn(int power, String name, Division division) {
        super(power, name, division);
    }

    @Override
    public void atack(T t) {

    }

    @Override
    public void move(T t, Map map, Division division) {

    }
}
