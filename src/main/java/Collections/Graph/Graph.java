package Collections.Graph;

import Collections.Lists.LinkedUnorderedList;
import Collections.Lists.UnorderedArrayList;
import Collections.Queues.LinkedQueue;
import Collections.Stack.LinkedStack;
import Exceptions.ElementNotFound;


import java.util.Iterator;

/**
 * Graph represents an adjacency matrix implementation of a graph.
 *
 */
public class Graph<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices; // number of vertices in the graph
    protected boolean[][] adjMatrix; // adjacency matrix
    protected T[] vertices; // values of vertices

    /**
     * Creates an empty graph.
     */
    public Graph() {
        numVertices = 0;
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    protected int getIndex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        if (isEmpty()){
            throw new ElementNotFound("Empty graph");
        }
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = false;
            adjMatrix[index2][index1] = false;
        }
    }

    @Override
    public Iterator iteratorBFS(T startVertex) {

        int startIndex = getIndex(startVertex);
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        UnorderedArrayList<T> resultList = new UnorderedArrayList<T>();
        if (!indexIsValid(startIndex))
            return resultList.iterator();
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;
        while (!traversalQueue.isEmpty())
        {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x.intValue()]);
            for (int i = 0; i < numVertices; i++)
            {
                if (adjMatrix[x.intValue()][i] && !visited[i])
                {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    @Override
    public Iterator iteratorDFS(T startVertex) {

        int startIndex = getIndex(startVertex);
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        UnorderedArrayList<T> resultList = new UnorderedArrayList<T>();
        boolean[] visited = new boolean[numVertices];
        if (!indexIsValid(startIndex))
            return resultList.iterator();
        for (int i = 0; i < numVertices; i++)
            visited[i] = false;
        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty())
        {
            x = traversalStack.peek();
            found = false;
            for (int i = 0; (i < numVertices) && !found; i++)
            {
                if (adjMatrix[x.intValue()][i] && !visited[i])
                {
                    traversalStack.push(i);
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty())
                traversalStack.pop();
        }
        return resultList.iterator();
    }

    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        if (numVertices == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public int size() {
        return this.numVertices;
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param index1 the first index
     * @param index2 the second index
     */
    public void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            adjMatrix[index2][index1] = true;
        }
    }

    protected    boolean indexIsValid(int index) {
        return index >= 0 && index < numVertices;
    }

    /**
     * Adds a vertex to the graph, expanding the capacity of the graph
     * if necessary. It also associates an object with the vertex.
     *
     * @param vertex the vertex to add to the graph
     */
    public void addVertex(T vertex) {
        if (numVertices == vertices.length)
            expandCapacity();
        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
        numVertices++;
    }

    public void expandCapacity(){
        T[] newVertices = (T[]) (new Object[vertices.length * 2]);
        boolean[][] newAdjMatrix = new boolean[vertices.length * 2][vertices.length * 2];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                newAdjMatrix[i][j] = adjMatrix[i][j];
            }
            newVertices[i] = vertices[i];
        }
        adjMatrix = newAdjMatrix;
        vertices = newVertices;
    }

    @Override
    public void removeVertex(T vertex) {
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                removeVertex(i);
                return;
            }
        }
    }

    public void removeVertex(int index) {
        if (indexIsValid(index)) {
            numVertices--;

            for (int i = index; i < numVertices; i++) {
                vertices[i] = vertices[i + 1];
            }
            vertices[numVertices] = null;

            for (int i = index; i < numVertices; i++) {
                for (int j = 0; j <= numVertices; j++) {
                    adjMatrix[i][j] = adjMatrix[i + 1][j];
                }
            }
            for (int j = 0; j <= numVertices; j++) {
                adjMatrix[numVertices][j] = false;
            }

            for (int i = index; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    adjMatrix[j][i] = adjMatrix[j][i + 1];
                }
            }
            for (int i = 0; i < numVertices; i++) {
                adjMatrix[i][numVertices] = false;
            }
        }else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Grafo:\n");
        sb.append("Vértices:\n");

        for (int i = 0; i < numVertices; i++) {
            sb.append(vertices[i]).append("\n ");
        }

        sb.append("\n\nMatriz de Adjacência:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(adjMatrix[i][j] ? "1 " : "0 ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

