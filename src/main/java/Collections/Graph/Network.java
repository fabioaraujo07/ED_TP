package Collections.Graph;

import java.util.Iterator;

public class Network<T> implements NetworkADT<T> {

    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected double[][] adjMatrix;
    protected T[] vertices;

    public Network() {
        numVertices = 0;
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    private boolean indexIsValid(int index) {
        return index >= 0 && index < numVertices;
    }

    protected void addEdge(int index1, int index2, double weight) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = weight;
            adjMatrix[index2][index1] = weight;
        } else {
            throw new IllegalArgumentException("Vértices inválidos: ");
        }

    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        addEdge(findIndex(vertex1), findIndex(vertex2), weight);
    }


    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        return 0;
    }

    @Override
    public void addVertex(T vertex) {
        if (vertices.length == size()){
            expandCapacity();
        }else {
            vertices[numVertices] = vertex;
            for (int i = 0; i < numVertices; i++) {
                adjMatrix[numVertices][i] = 0;
                adjMatrix[i][numVertices] = 0;
            }
            numVertices++;
        }
    }

    private void expandCapacity() {
        T[] newVertices = (T[]) (new Object[DEFAULT_CAPACITY * 2]);
        double[][] newAdjMatrix = new double[DEFAULT_CAPACITY * 2][DEFAULT_CAPACITY * 2];

        for (int i = 0; i < numVertices; i++) {
            newVertices[i] = vertices[i];
        }
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                newAdjMatrix[i][j] = adjMatrix[i][j];
            }
        }
        vertices = newVertices;
        adjMatrix = newAdjMatrix;
    }

    protected int findIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public void removeVertex(T vertex) {
        int pos = findIndex(vertex);

        if (pos != -1) {
            if (numVertices > 1) {
                vertices[pos] = vertices[numVertices - 1];

                for (int i = 0; i < numVertices; i++) {
                    adjMatrix[i][pos] = adjMatrix[i][numVertices - 1];
                    adjMatrix[pos][i] = adjMatrix[numVertices - 1][i];
                }

                numVertices--;

                vertices[numVertices] = null;
                for (int i = 0; i < numVertices; i++) {
                    adjMatrix[numVertices][i] = 0;
                    adjMatrix[i][numVertices] = 0;
                }

            } else {
                numVertices = 0;
                vertices[0] = null;
                adjMatrix[0][0] = 0;
            }

        }
    }

    private int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Vertex not found: " + vertex);
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 1;
            adjMatrix[index2][index1] = 1;
        }
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        remvoveEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void remvoveEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 0;
            adjMatrix[index2][index1] = 0;
        }
    }

    @Override
    public Iterator iteratorBFS(T startVertex) {
        return null;
    }

    @Override
    public Iterator iteratorDFS(T startVertex) {
        return null;
    }

    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return numVertices == 0;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public int size() {
        return numVertices;
    }
}
