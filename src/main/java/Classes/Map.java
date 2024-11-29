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

}