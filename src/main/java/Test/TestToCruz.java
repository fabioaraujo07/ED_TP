package Test;

import Classes.Map;
import Classes.ToCruz;
import Classes.Division;
import Classes.Item;
import Enumerations.Items;
import Exceptions.ItemNotFound;

public class TestToCruz {
    public static void main(String[] args) {
        // Criar uma divisão inicial
        Division initialDivision = new Division("Heliporto");
        Division newDivision = new Division("Garagem");
        Division otherDivision = new Division("Escada2");
        Division otherDivision2 = new Division("Escada3");


        Map map = new Map();
        map.addVertex(initialDivision);
        map.addVertex(newDivision);
        map.addVertex(otherDivision);
        map.addEdge(initialDivision, newDivision);

        // Criar o jogador
        ToCruz<Item> player = new ToCruz<>("John", initialDivision);

        // Exibir informações iniciais do jogador
        System.out.println("Informações iniciais do jogador:");
        System.out.println(player);

        // Criar itens
        Item item1 = new Item(initialDivision, Items.COLETE,20); // Dá 20 pontos de vida
        Item item2 = new Item(initialDivision, Items.KIT_VIDA,20);   // Dá 15 pontos de vida

        // Adicionar itens à bolsa do jogador
        System.out.println("\nAdicionando itens à bolsa...");
        player.addItem(item1);
        player.addItem(item2);

        // Exibir informações após adicionar itens
        System.out.println("\nInformações do jogador após adicionar itens:");
        System.out.println(player);

        // Tentar usar um item
        System.out.println("\nTentando usar o item no topo da bolsa...");
        try {
            player.useItem(item2); // Tenta usar o "Mana Potion"
        } catch (ItemNotFound e) {
            System.out.println(e.getMessage());
        }

        // Exibir informações após usar o item
        System.out.println("\nInformações do jogador após usar um item:");
        System.out.println(player);

        // Movendo o jogador para uma nova divisão
        System.out.println("\nMovendo o jogador para outra divisão...");
        player.moveDivision(otherDivision);

        // Exibir informações após mover o jogador
        System.out.println("\nInformações do jogador após mover de divisão:");
        System.out.println(player);

        // Movendo o jogador para uma nova divisão
        System.out.println("\nMovendo o jogador para outra divisão...");
        player.moveDivision(otherDivision2);

        // Exibir informações após mover o jogador
        System.out.println("\nInformações do jogador após mover de divisão:");
        System.out.println(player);
    }
}
