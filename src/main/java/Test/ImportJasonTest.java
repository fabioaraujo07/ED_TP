package Test;

import Classes.Division;
import Classes.Map;
import Import.ImportJason;

public class ImportJasonTest {
    public static void main(String[] args) {

        ImportJason imprt = new ImportJason();
        Map<Division> m = new Map<>();

        m = imprt.importJason("src/main/resources/Missão.json");
        System.out.println(m);

    }
}
