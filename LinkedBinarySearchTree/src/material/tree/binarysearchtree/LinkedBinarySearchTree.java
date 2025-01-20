package material.tree.binarysearchtree;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import material.Position;
import material.tree.binarytree.LinkedBinaryTree;

/**
 *
 * @author Álvaro Martín Martín.
 * @param <E>
 */
public class LinkedBinarySearchTree<E> implements BinarySearchTree<E> {

    LinkedBinaryTree<E> tree;
    Comparator<E> comparator; //Sirve por si el usuario quiere proporcionar un objeto comparator personalizado  (Se manejará en los constructores)
    int size;

    public LinkedBinarySearchTree() { //Se no se pasan parámetros, se usará el DefaultComparator como objeto comparador
        this.tree = new LinkedBinaryTree();
        this.comparator = new DefaultComparator();
        this.size = 0;
    }

    public LinkedBinarySearchTree(Comparator<E> comparador) { //El usuario pasa como parámetro un objeto comparador personalizado
        this.comparator = comparador;
        this.tree = new LinkedBinaryTree();
        this.size = 0;
    }

    //Metodo necesario ya que se va a tener que buscar en find() y en findAll(). No queremos duplicar código
    //Hecho a mi manera, no uso llamadas recursivas. No se hasta que punto estará bien. La otra implementación es mejor.
    /*private Position<E> findAux(E value) { 
        Position<E> aux = this.tree.root();
        if (tree.isEmpty()) {
            throw new RuntimeException("El árbol está vacío, el nodo que intentas buscar no está");
        }
        while (aux != null) {
            if (this.comparator.compare(value, aux.getElement()) < 0) //Irse por la izquierda
            {
                aux = this.tree.left(aux);
            } else if (this.comparator.compare(value, aux.getElement()) > 0) //Irse por la derecha
            {
                aux = this.tree.right(aux);
            } else //Hemos encontrado el elemento
            {
                return aux;
            }
        } // Si sale del bucle es que no ha encontrado nada
        return null;
    }
     */
    //Metodo necesario ya que se va a tener que buscar en find() y en findAll(). No queremos duplicar código
    private Position<E> findAux(E value, Position<E> pos) { //Busca un valor a partir de un position. Usa llamadas recursivas
        int comparacion = this.comparator.compare(value, pos.getElement());

        if (comparacion == 0) {//Caso base
            return pos;
        } else if (comparacion < 0 && this.tree.hasLeft(pos)) //Nos vamos por la izquierda
        {
            return this.findAux(value, this.tree.left(pos));
        } else if (comparacion > 0 && this.tree.hasRight(pos)) //Nos vamos por la derecha
        {
            return this.findAux(value, this.tree.right(pos));
        }

        return null; //Devolverá null si no encuentra el valor
    }

    @Override
    public Position<E> find(E value) {
        if (this.findAux(value, this.tree.root()) == null) {
            throw new RuntimeException("El valor que buscas no se encuentra en el árbol");
        }
        return this.findAux(value, this.tree.root());
    }

    @Override
    public Iterable<? extends Position<E>> findAll(E value) {
        if (this.findAux(value, this.tree.root()) == null) {
            throw new RuntimeException("El valor que buscas no se encuentra en el árbol");
        }
        LinkedList<Position<E>> lista = new LinkedList();
        Position<E> posAux = this.tree.root();
        while (this.findAux(value, posAux) != null) {
            Position<E> newPos = this.findAux(value, posAux);
            lista.add(newPos);
            posAux = newPos;
        }
        return lista;
    }

    @Override
    public Position<E> insert(E value) {
        Position<E> posAux = this.tree.root();
        Position<E> posAnt = this.tree.root();
        int comparacion = this.comparator.compare(value, posAux.getElement());

        while (posAux != null) {
            posAnt = posAux;
            if (comparacion <= 0) { //Aquí cubro también el caso de que los valores sean iguales, se irá por la izquierda
                posAux = this.tree.left(posAux);
            } else {
                posAux = this.tree.right(posAux);
            }
        } //Saldrá del bucle y posAux estará apuntando a null, posAnt estará apuntando al padre donde se quiere insertar

        Position<E> pos;

        if (posAnt == null) //Se quiere insertar la raíz del árbol
        {
            pos = this.tree.addRoot(value);
        } else if (this.comparator.compare(value, posAnt.getElement()) <= 0) //Se debe insertar en la izquierda
        {
            pos = this.tree.insertLeft(posAnt, value);
        } else {
            pos = this.tree.insertRight(posAnt, value);
        }

        return pos;
    }

    @Override
    public boolean isEmpty() {
        return (this.size == 0);
    }

    @Override
    public void remove(Position<E> pos) { // 3 posibilidades
        if (this.tree.isLeaf(pos)) { //El nodo que se quiere borrar es una hoja, se borra la hoja
            this.tree.remove(pos);
        } else if ((this.tree.hasLeft(pos)) && (this.tree.hasRight(pos))) { //El nodo a borrar tiene 2 hijos. Se pone al nodo que se quiere borrar el valor del sucesor y se elimina el sucesor
            this.tree.replace(pos, this.localizarSucesor(pos).getElement());
            this.tree.remove(this.localizarSucesor(pos));
        } else { //El nodo a borrar tiene 1 hijo. Se reemplaza el nodo por su hijo.
            Position<E> hijo = null; // Hay que ver si el hijo que tiene es el derecho o el izquierdo.
            if (this.tree.hasLeft(pos)) {
                hijo = this.tree.left(pos);
            } else if (this.tree.hasRight(pos)) {
                hijo = this.tree.right(pos);
            }
            this.tree.replace(pos, hijo.getElement()); //Salta un aviso pero no debería haber problemas ya que el hijo nunca será nulo. Se está metiendo por el else
            this.tree.remove(hijo);
        }
    }

    private Position<E> localizarSucesor(Position<E> pos) {
        if (this.tree.hasRight(pos)) //Si un nodo tiene hijo derecho, su sucesor es el mínimo del subárbol derecho
        {
            return this.minimo(this.tree.right(pos));
        } //return this.minimo(this.tree.subTree(this.tree.right(pos)).root()); tonteria hecer eso en verdad
        else { //Si no tiene hijo derecho, su sucesor será el primer ancestro mayor que él.
            Position<E> aux = this.tree.parent(pos);
            if (aux == null) {
                throw new RuntimeException("El nodo no tiene sucesor, es la raiz y no tiene hijo derecho");
            }

            while ((aux != null) && (pos == this.tree.right(aux))) { //La segunda condicion es porque en el caso de que quieras saber el sucesor de un nodo el cual es el hijo izquierdo del padre, el sucesor será ese padre, no habrá que seguir subiendo en el árbol
                //Se seguirá subiendo mientras no se llegue a la raíz y mientras el nodo pos sea el hijo derecho de su padre (aux)
                pos = aux;
                aux = this.tree.parent(aux);
                //Se sube hacia arriba
            }
            return pos;
        }

    }

    private Position<E> minimo(Position<E> pos) {
        while (this.tree.left(pos) != null) {
            pos = this.tree.left(pos);
        }
        return pos;
    }

    /* Hecho de forma recursiva, pero es lo mismo
    private Position<E> minimo(Position<E> p) {
        if (arbol.hasLeft(p)) {
            return minimo(arbol.left(p));
        }
        return p;
    }
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterable<? extends Position<E>> rangeIterator(E m, E M) {
        LinkedList<Position<E>> lista = new LinkedList();
        Iterator<Position<E>> it = iterator();
        Position<E> actual = null;

        while (it.hasNext()) {
            actual = it.next();
            if ((this.comparator.compare(actual.getElement(), m) > 0) && (this.comparator.compare(actual.getElement(), M) < 0)) //Si está dentro de los límites, se añade
            {
                lista.add(actual);
            }
        }
        return lista;

    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new LinkedBinarySearchTreeIterator(this.tree);
    }

    public class LinkedBinarySearchTreeIterator<E> implements Iterator<Position<E>> { //Te recorre el arbol como un arbol binario normal, no se si estará bien así

        private LinkedBinaryTree<E> current;
        private Queue<Position<E>> cola;

        public LinkedBinarySearchTreeIterator(LinkedBinaryTree<E> tree) {
            this.current = tree;
            cola = new LinkedList();
            cola.add(current.root());
        }

        @Override
        public boolean hasNext() {
            return !cola.isEmpty();
        }

        @Override
        public Position<E> next() {
            Position<E> pos = cola.remove();
            if (current.hasLeft(pos)) {
                cola.add(pos);
            } else if (current.hasRight(pos)) {
                cola.add(pos);
            }

            return pos;
        }

    }

}
