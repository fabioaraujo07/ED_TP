package Classes;

import Interfaces.Player;
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
        ToCruz player = (ToCruz) t;
        Division current = player.getCurrentDivision();

        if (map.getEdges(current).contains(division)) {
            player.moveDivision(division);
        }else {
            System.out.println("You can't move this division, no connection");
        }
    }
}
