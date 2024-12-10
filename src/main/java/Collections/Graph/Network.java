package Collections.Graph;

import java.util.*;

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

    protected boolean indexIsValid(int index) {
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
        int startIndex = getIndex(vertex1);
        int targetIndex = getIndex(vertex2);
        double[] distances = new double[numVertices];
        boolean[] visited = new boolean[numVertices];
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(i -> distances[i]));

        Arrays.fill(distances, Double.MAX_VALUE);
        distances[startIndex] = 0;
        priorityQueue.add(startIndex);

        while (!priorityQueue.isEmpty()) {
            int currentIndex = priorityQueue.poll();
            visited[currentIndex] = true;

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[currentIndex][i] > 0 && !visited[i]) {
                    double newDist = distances[currentIndex] + adjMatrix[currentIndex][i];
                    if (newDist < distances[i]) {
                        distances[i] = newDist;
                        priorityQueue.add(i);
                    }
                }
            }
        }

        return distances[targetIndex];
    }

    @Override
    public void addVertex(T vertex) {
        if (vertices.length == size()) {
            expandCapacity();
        }
        vertices[numVertices] = vertex;
        for (int i = 0; i < numVertices; i++) {
            adjMatrix[numVertices][i] = 0;
            adjMatrix[i][numVertices] = 0;
        }
        numVertices++;
    }

    private void expandCapacity() {
        T[] newVertices = (T[]) (new Object[vertices.length * 2]);
        double[][] newAdjMatrix = new double[vertices.length * 2][vertices.length * 2];

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

    protected int getIndex(T vertex) {
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
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = 0;
            adjMatrix[index2][index1] = 0;
        }
    }

    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        LinkedList<T> traversalQueue = new LinkedList<>();
        ArrayList<T> resultList = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];

        int startIndex = getIndex(startVertex);
        traversalQueue.add(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            T vertex = traversalQueue.poll();
            resultList.add(vertex);
            int vertexIndex = getIndex(vertex);

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[vertexIndex][i] > 0 && !visited[i]) {
                    traversalQueue.add(vertices[i]);
                    visited[i] = true;
                }
            }
        }

        return resultList.iterator();
    }

    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        Stack<T> traversalStack = new Stack<>();
        ArrayList<T> resultList = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];

        int startIndex = getIndex(startVertex);
        traversalStack.push(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            T vertex = traversalStack.pop();
            resultList.add(vertex);
            int vertexIndex = getIndex(vertex);

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[vertexIndex][i] > 0 && !visited[i]) {
                    traversalStack.push(vertices[i]);
                    visited[i] = true;
                }
            }
        }

        return resultList.iterator();
    }

    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(targetVertex);
        double[] distances = new double[numVertices];
        int[] predecessors = new int[numVertices];
        boolean[] visited = new boolean[numVertices];
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(i -> distances[i]));

        Arrays.fill(distances, Double.MAX_VALUE);
        distances[startIndex] = 0;
        priorityQueue.add(startIndex);

        while (!priorityQueue.isEmpty()) {
            int currentIndex = priorityQueue.poll();
            visited[currentIndex] = true;

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[currentIndex][i] > 0 && !visited[i]) {
                    double newDist = distances[currentIndex] + adjMatrix[currentIndex][i];
                    if (newDist < distances[i]) {
                        distances[i] = newDist;
                        predecessors[i] = currentIndex;
                        priorityQueue.add(i);
                    }
                }
            }
        }

        LinkedList<T> path = new LinkedList<>();
        for (int at = targetIndex; at != startIndex; at = predecessors[at]) {
            path.addFirst(vertices[at]);
        }
        path.addFirst(vertices[startIndex]);

        return path.iterator();
    }

    @Override
    public boolean isEmpty() {
        return numVertices == 0;
    }

    @Override
    public boolean isConnected() {
        if (numVertices == 0) {
            return false;
        }

        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0] = true;

        int visitedCount = 1;
        while (!queue.isEmpty()) {
            int vertexIndex = queue.poll();
            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[vertexIndex][i] > 0 && !visited[i]) {
                    queue.add(i);
                    visited[i] = true;
                    visitedCount++;
                }
            }
        }

        return visitedCount == numVertices;
    }

    @Override
    public int size() {
        return numVertices;
    }
}