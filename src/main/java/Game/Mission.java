package Game;
import java.util.Scanner;

public class Mission {

    private MissionControler controller;
    private Scanner scanner;

    public Mission() {
        this.controller = new MissionControler();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Improbable Mission game!");

        System.out.println("Available missions:");
        System.out.println("1. Pata de Coelho");
        System.out.println("2. Operation Stealth");
        System.out.println("3. Infiltration");

        System.out.println("Choose a mission:");
        int missionChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (!controller.selectMission(missionChoice)) {
            System.out.println("Invalid mission choice.");
            return;
        }

        System.out.println("Selected mission file: " + controller.getSelectedMissionFile());

        System.out.println("\nChoose mode:");
        System.out.println("1. Manual");
        System.out.println("2. Automatic");
        int modeChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (modeChoice == 2) {
            controller.startAutomaticGame();
            return;
        }

        controller.startManualGame(scanner);
    }


    public static void main(String[] args) {
        Mission mission = new Mission();
        mission.start();
    }
}
