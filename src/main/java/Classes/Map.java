package Classes;

import Collections.Graph.Graph;
import Collections.Lists.LinkedUnorderedList;
import Collections.Lists.UnorderedArrayList;
import Exceptions.ElementNotFound;

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

    public LinkedUnorderedList<T> getEdges(T v) throws ElementNotFound {
        int vertex = getIndex(v);
        if (!indexIsValid(vertex)){
            throw new ElementNotFound("Vertex not valid");
        }

        LinkedUnorderedList<T> edges = new LinkedUnorderedList<>();
        for (int i = 0; i < numVertices; i++) {
            if(adjMatrix[vertex][i]){
                edges.addToRear(vertices[i]);
            }
        }
        return edges;
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

        sb.append("Vértices do Grafo:\n");
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ").append(vertices[i].toString()).append("\n");
        }

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