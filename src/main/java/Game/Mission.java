package Game;
import java.util.Scanner;

public class Mission {

    private MissionControler controller;
    private Scanner scanner;

    /**
     * Constructs a Mission instance and initializes the controller and scanner.
     */
    public Mission() {
        this.controller = new MissionControler();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game by displaying available missions and modes, and handling user input.
     */
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

    /**
     * The main method to start the game.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mission mission = new Mission();
        mission.start();
    }
}
