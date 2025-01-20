package material.graphs;

import java.util.*;

/**
 * Graph implemented as a edge list
 *
 * @author Álvaro Martín Martín.
 * @param <V>
 * @param <E>
 */
public class ELDigraph<V, E> implements Digraph<V, E> {

    public class ELVertex<V> implements Vertex<V> {

        private V element;

        public ELVertex(V element) {
            this.element = element;
        }

        @Override
        public V getElement() {
            return this.element;
        }

        public void setElement(V element) {
            this.element = element;
        }

        //Necesarios estos 2 métodos 
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + Objects.hashCode(this.element);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ELVertex<?> other = (ELVertex<?>) obj;
            return Objects.equals(this.element, other.element);
        }

    }

    public class ELEdge<E> implements Edge<E> {

        private E element;
        private ELVertex<V> ini;
        private ELVertex<V> fin;

        public ELEdge(E element, ELVertex<V> ini, ELVertex<V> fin) {
            this.element = element;
            this.ini = ini;
            this.fin = fin;
        }

        @Override
        public E getElement() {
            return this.element;
        }

        public ELVertex<V> getIni() {
            return ini;
        }

        public ELVertex<V> getFin() {
            return fin;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public void setIni(ELVertex<V> ini) {
            this.ini = ini;
        }

        public void setFin(ELVertex<V> fin) {
            this.fin = fin;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.element);
            hash = 17 * hash + Objects.hashCode(this.ini);
            hash = 17 * hash + Objects.hashCode(this.fin);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ELEdge<?> other = (ELEdge<?>) obj;
            if (!Objects.equals(this.element, other.element)) {
                return false;
            }
            if (!Objects.equals(this.ini, other.ini)) {
                return false;
            }
            return Objects.equals(this.fin, other.fin);
        }
    }

    private Set<ELVertex<V>> vertices = new HashSet();
    private Set<ELEdge<E>> aristas = new HashSet();

    //Las siguientes 2 operaciones son necesarias para transformar un Vertex<V> en ELVertex<V>. Igual que se hacían los checkPositions
    private ELVertex<V> checkVertex(Vertex<V> v) {
        if (v == null || !(v instanceof ELVertex)) {
            throw new RuntimeException("Vertice no válido");
        }
        return (ELVertex<V>) v;
    }

    private ELEdge<E> checkEdge(Edge<E> e) {
        if (e == null || !(e instanceof ELEdge)) {
            throw new RuntimeException("Arista no válida");
        }
        return (ELEdge<E>) e;
    }

    @Override
    public Collection<? extends Vertex<V>> vertices() {
        return Collections.unmodifiableCollection(this.vertices);
    }

    @Override
    public Collection<? extends Edge<E>> edges() {
        return Collections.unmodifiableCollection(this.aristas);
    }

    //CAMBIADO
    @Override
    public Collection<? extends Edge<E>> incidentEdges(Vertex<V> v) {
        ELVertex<V> vertex = checkVertex(v);
        LinkedList<ELEdge<E>> lista = new LinkedList();
        for (ELEdge<E> edge : this.aristas) {
            if (edge.getFin().equals(vertex)) {
                lista.add(edge);
            }
        }
        return lista;
    }

    @Override
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) {
        ELVertex<V> vertex = checkVertex(v);
        ELEdge<E> edge = checkEdge(e);

        if (edge.getIni().equals(vertex)) {
            return edge.getFin();
        } else if (edge.getFin().equals(vertex)) {
            return edge.getIni();
        } else {
            throw new RuntimeException("La arista no es parte del vértice, no se puede calcular su opuesto");
        }
    }

    //NUEVO
    @Override
    public Vertex<V> endVertice(Edge<E> edge) {
        ELEdge<E> e = checkEdge(edge);
        return e.getFin();

    }

    //NUEVO
    @Override
    public Vertex<V> startVertice(Edge<E> edge) {
        ELEdge<E> e = checkEdge(edge);
        return e.getIni();
    }

    //CAMBIADO
    @Override
    public boolean areAdjacent(Vertex<V> v1, Vertex<V> v2) { //v1 es el vertice de inicio, v2 el de fin
        ELVertex<V> vertex1 = checkVertex(v1);
        ELVertex<V> vertex2 = checkVertex(v2);

        //Busco la arista entre los 2 vértices
        ELEdge<E> edge = null;
        for (ELEdge<E> arista : this.aristas) {
            if ((arista.getIni().equals(vertex1) && arista.getFin().equals(vertex2)) || ((arista.getIni().equals(vertex2) && arista.getFin().equals(vertex1)))) {
                edge = arista;
                break;
            }
        }

        if (edge == null) {
            throw new RuntimeException("No hay arista entre los 2 vértices");
        }
        return((edge.getFin().equals(vertex1)) && (edge.getIni().equals(vertex2)));
        
    }

    @Override
    public V replace(Vertex<V> vertex, V vertexValue) {
        ELVertex<V> v = checkVertex(vertex);
        V antValor = v.getElement();
        v.setElement(vertexValue);
        return antValor;
    }

    @Override
    public E replace(Edge<E> edge, E edgeValue) {
        ELEdge<E> e = checkEdge(edge);
        E antValor = e.getElement();
        e.setElement(edgeValue);
        return antValor;
    }

    @Override
    public Vertex<V> insertVertex(V value) {
        ELVertex<V> newVertex = new ELVertex(value);
        this.vertices.add(newVertex);
        return newVertex;
    }

    @Override
    public Edge<E> insertEdge(Vertex<V> v1, Vertex<V> v2, E edgeValue) {
        ELVertex<V> vertex1 = checkVertex(v1);
        ELVertex<V> vertex2 = checkVertex(v2);

        //Comprobacion de si la arista ya existe
        for (ELEdge<E> arista : this.aristas) {
            if ((arista.getIni().equals(vertex1) && arista.getFin().equals(vertex2)) || ((arista.getIni().equals(vertex2) && arista.getFin().equals(vertex1)))) {
                //throw new RuntimeException("La arista ya existe");
                return null;
            }
        }

        ELEdge<E> newEdge = new ELEdge(edgeValue, vertex1, vertex2);
        this.aristas.add(newEdge);
        return newEdge;
    }

    //NUEVO
    @Override
    public List<Edge<E>> outputEdges(Vertex<V> v) {
        List<Edge<E>> lista = new LinkedList();
        
        for(Edge<E> edge : this.incidentEdges(v)){
            ELEdge<E> e = checkEdge(edge);
            if(e.getIni().equals(v))
                lista.add(e);
        }
        return lista;
    }

    @Override
    public V removeVertex(Vertex<V> vertex) {
        ELVertex<V> v = checkVertex(vertex);
        V valor = v.getElement();

        //Hay que eliminar todas las aristas asociadas a ese vértice
        /*
        for(ELEdge<E> arista : this.aristas)
            if(v.equals(arista.getIni())||v.equals(arista.getFin()))
                this.removeEdge(arista);
         */
        //Se podrá hacer de las 2 maneras 
        for (Edge<E> arista : this.incidentEdges(vertex)) {
            this.removeEdge(arista);
        }

        this.vertices.remove(v);

        return valor;
    }

    @Override
    public E removeEdge(Edge<E> edge) {
        ELEdge<E> e = checkEdge(edge);
        E valor = e.getElement();
        this.aristas.remove(e);
        return valor;
    }

}
