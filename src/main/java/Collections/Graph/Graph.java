package Collections.Graph;




import Collections.Array.UnorderedArrayList;
import Collections.Linked.LinkedQueue;
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
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
    int startIndex = getIndex(startVertex);
    int targetIndex = getIndex(targetVertex);

    if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
        return new UnorderedArrayList<T>().iterator();
    }


    int[] dist = new int[numVertices];
    int[] predecessors = new int[numVertices];
    boolean[] visited = new boolean[numVertices];
    final int INFINITY = Integer.MAX_VALUE;

    for (int i = 0; i < numVertices; i++) {
        dist[i] = INFINITY;
        predecessors[i] = -1;
        visited[i] = false;
    }

    dist[startIndex] = 0;

    // Processar todos os vértices
    for (int i = 0; i < numVertices; i++) {
        // Escolher o vértice não visitado com a menor distância
        int minIndex = -1;
        int minDist = INFINITY;

        for (int j = 0; j < numVertices; j++) {
            if (!visited[j] && dist[j] < minDist) {
                minDist = dist[j];
                minIndex = j;
            }
        }

        if (minIndex == -1) break; // Todos os vértices acessíveis já foram processados

        visited[minIndex] = true;

        // Atualizar distâncias dos vértices adjacentes
        for (int j = 0; j < numVertices; j++) {
            if (adjMatrix[minIndex][j] && !visited[j]) {
                int newDist = dist[minIndex] + 1; // Todas as arestas têm peso 1
                if (newDist < dist[j]) {
                    dist[j] = newDist;
                    predecessors[j] = minIndex;
                }
            }
        }
    }

    // Reconstrução do caminho
    UnorderedArrayList<T> path = new UnorderedArrayList<>();
    int step = targetIndex;

    while (step != -1) {
        path.addToFront(vertices[step]); // Reconstrói o caminho
        step = predecessors[step];
    }

    // Verifica se o caminho é válido (se alcançou o vértice inicial)
    if (path.isEmpty() || !path.first().equals(startVertex)) {
        return new UnorderedArrayList<T>().iterator(); // Caminho vazio
    }

    return path.iterator();
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
    if (isEmpty()) {
        return false;
    }

    boolean[] visited = new boolean[numVertices];
    LinkedQueue<Integer> queue = new LinkedQueue<>();

    // Começa a partir do primeiro vértice
    queue.enqueue(0);
    visited[0] = true;

    while (!queue.isEmpty()) {
        int current = queue.dequeue();
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[current][i] && !visited[i]) {
                visited[i] = true;
                queue.enqueue(i);
            }
        }
    }

    // Verifica se todos os vértices foram visitados
    for (boolean vertexVisited : visited) {
        if (!vertexVisited) {
            return false;
        }
    }
    return true;
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

