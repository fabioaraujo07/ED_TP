package Game;

import Classes.*;
import Game.CombatHandler;

import java.util.Scanner;

public class Mission {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao jogo Improbable Mission!");
        System.out.print("Insira o caminho para o arquivo do cenário: ");
        String filepath = scanner.nextLine();



        Building building = new Building(filepath);
        Goal goal = building.getGoal();
        Division startDivision = building.getInAndOut().last();
        ToCruz player = new ToCruz("Tó Cruz", startDivision);

        System.out.println("Divisões registradas no grafo:");
        for (Division division : building.getMap().getVertexes()) {
            System.out.println("- " + division.getName());
        }


        CombatHandler combatHandler = new CombatHandler();

        System.out.println("\nJogo iniciado!");
        System.out.println("Você está na divisão: " + startDivision.getName());
        System.out.println("Objetivo: " + goal.getType() + " na divisão " + goal.getDivision().getName());

        boolean gameRunning = true;

        while (gameRunning) {
            System.out.println("\n--------------------------------------");
            System.out.println("Divisão atual: " + player.getCurrentDivision().getName());
            System.out.println("Vida: " + player.getLifePoints());
            System.out.println("Itens na mochila: " + player.getBag().size());
            System.out.println("--------------------------------------");
            System.out.println("Escolha uma ação:");
            System.out.println("1. Mover-se para outra divisão");
            System.out.println("2. Atacar inimigos (Cenário 1)");
            System.out.println("3. Usar item (Cenário 4)");
            System.out.println("4. Procurar o objetivo (Cenário 5 ou 6)");
            System.out.println("5. Passar turno (Cenário 2)");
            System.out.println("6. Sair do jogo");

            Division currentDivision = player.getCurrentDivision();
            System.out.println("Divisões conectadas:");
            for (Division neighbor : building.getMap().getEdges(currentDivision)) {
                System.out.println("- " + neighbor.getName());
            }


            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Divisões conectadas:");
                    for (Division neighbor : building.getMap().getEdges(player.getCurrentDivision())) {
                        System.out.println("- " + neighbor.getName());
                    }
                    System.out.print("Digite o nome da divisão: ");
                    String divisionName = scanner.nextLine();
                    Division targetDivision = findDivisionByName(building, divisionName);
                    if (targetDivision != null) {
                        player.movePlayer(building.getMap(), targetDivision);
                    } else {
                        System.out.println("Divisão inválida.");
                    }
                    break;

                case 2:
                    combatHandler.scenario1(player, building);
                    break;

                case 3:
                    combatHandler.scenario4(player, building);
                    break;

                case 4:
                    if (player.getCurrentDivision().equals(goal.getDivision())) {
                        combatHandler.scenario6(player, building, goal);
                    } else {
                        System.out.println("Você ainda não está na divisão do objetivo.");
                    }
                    break;

                case 5:
                    combatHandler.scenario2(player, building);
                    break;

                case 6:
                    System.out.println("Saindo do jogo. Até a próxima!");
                    gameRunning = false;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

            if (!player.isAlive()) {
                System.out.println("Tó Cruz morreu. Fim de jogo.");
                gameRunning = false;
            } else if (goal.isRequired() && player.getCurrentDivision().equals(building.getInAndOut().last())) {
                System.out.println("Parabéns! Você concluiu a missão com sucesso!");
                gameRunning = false;
            }
        }

        scanner.close();
    }

    private static Division findDivisionByName(Building building, String name) {
        for (Division division : building.getMap().getVertexes()) {
            if (division.getName().equalsIgnoreCase(name)) {
                return division;
            }
        }
        return null;
    }
}

