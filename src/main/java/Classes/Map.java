package Classes;

import Collections.Graph.Graph;
import Collections.Lists.UnorderedArrayList;

public class Map<T> extends Graph<T> {

public Map(){
    super();
}

public UnorderedArrayList<T> getVertexes(){
    UnorderedArrayList<T> list = new UnorderedArrayList<>(numVertices);
    for (int i = 0; i < numVertices; i++) {
        list.addToRear(vertices[i]);
    }
    return list;
}

public int getNumVertices(){
    return numVertices;
}

public boolean[][] getMatrix(){
    boolean[][] matrix = new boolean[numVertices][numVertices];
    for (int i = 0; i < numVertices; i++) {
        for (int j = 0; j < numVertices; j++) {
            matrix[i][j] = adjMatrix[i][j];
        }
    }
    return matrix;
}

public T getVertex(int i){
    if (!indexIsValid(i)){
        return null;
    }
    return vertices[i];
}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Exibe os Vértices (Divisões)
        sb.append("Vértices do Grafo:\n");
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ").append(vertices[i].toString()).append("\n");
        }

        // Exibe a Matriz de Adjacência
        sb.append("\nMatriz de Adjacência:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(adjMatrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }



}