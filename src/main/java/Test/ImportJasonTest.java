package Test;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Import.ImportJason;

public class ImportJasonTest {
    public static void main(String[] args) {

        ImportJason imprt = new ImportJason();
        Map<Division> m = new Map<>();
        LinkedUnorderedList<Enemy> e = new LinkedUnorderedList<>();
        LinkedUnorderedList<Item> i = new LinkedUnorderedList<>();
        String file = "src/main/resources/Missão.json";

        m = imprt.importBuilding(file);
        System.out.println(m);

        e = imprt.importEnemy(file);
        System.out.println(e);

        i = imprt.importItems(file);
        System.out.println(i);

        Goal g = imprt.importGoal(file);
        System.out.println(g);
    }
}
