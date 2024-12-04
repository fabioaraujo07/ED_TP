package Interfaces;

import Classes.Building;
import Classes.Division;
import Classes.Map;
import Classes.ToCruz;

public interface EnemyADT {

    public void moveNPC(Building building);

    public void attackPlayer(ToCruz player);
}
