package Interfaces;

import Classes.Division;
import Classes.Map;
import Classes.ToCruz;

public interface EnemyADT {

    public void moveNPC(ToCruz player, Map map, Division division);

    public void attackPlayer(ToCruz player, Map map, Division division);
}
