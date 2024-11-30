package Interfaces;

import Classes.Division;
import Classes.Map;
import Classes.ToCruz;

public interface Enemy {

    public void movePlayer(ToCruz player, Map map, Division division);

}
