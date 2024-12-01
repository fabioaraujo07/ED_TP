package Test;

import Classes.*;
import Collections.Lists.LinkedUnorderedList;
import Import.ImportJSON;

public class ImportJSONTest {
    public static void main(String[] args) {

        ImportJSON imprt = new ImportJSON();
        Map<Division> m;
        LinkedUnorderedList<Enemy> e = new LinkedUnorderedList<>();
        LinkedUnorderedList<Item> i = new LinkedUnorderedList<>();
        LinkedUnorderedList<Division> io = new LinkedUnorderedList<>();
        String file = "src/main/resources/Missão.json";

        m = imprt.importBuilding(file);
        System.out.println(m);

        e = imprt.importEnemy(file);
        System.out.println(e);

        i = imprt.importItems(file);
        System.out.println(i);

        Goal g = imprt.importGoal(file);
        System.out.println(g);

        io = imprt.importInAndOut(file);
        System.out.println(io);
    }
}
