package list;

import java.util.Iterator;
import material.Position;

/**
 * Generic LinkedList
 * 
 * @author Álvaro Martín Martín.
 */
public class MyLinkedListBetter<E> implements MyListBetter<E> {

    public class Node<T> implements Position<T> {

        private T elem;
        private Node<T> prev;
        private Node<T> next;

        @Override
        public T getElement() {
            return this.elem;
        }

        public Node(T elem, Node<T> prev, Node<T> next) {
            this.elem = elem;
            this.prev = prev;
            this.next = next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setElem(T elem) {
            this.elem = elem;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    private int size;
    private Node<E> head;

    public MyLinkedListBetter() {
        this.size = 0;
        this.head = null;
    }

    private Node<E> checkPosition(Position<E> pos) {
        if (pos == null || !(pos instanceof Node)) {
            throw new RuntimeException("Position is invalid");
        }
        return (Node<E>) pos;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isempty() {
        return (size == 0);
    }

    @Override
    public Position<E> add(E value) { //Añadir por cabecera
        Node<E> newNode = new Node(value, null, head);

        if (head != null) //En el caso de añadir a una lista con mas de 0 elementos
        {
            head.setPrev(newNode);
        }
        size++;
        head = newNode;

        return newNode;
    }

    @Override
    public Position<E> addAfter(Position<E> pos, E value) {
        Node<E> antNode = checkPosition(pos); //antNode porque toma el papel de nodo anterior al que se quiere añadir
        Node<E> newNode = new Node(value, antNode, antNode.getNext());

        antNode.setNext(newNode);
        if (newNode.getNext() != null) //En el caso de el position no es el del ultimo nodo de la lista
        {
            newNode.getNext().setPrev(newNode);
        }
        size++;

        return newNode;
    }

    @Override
    public Position<E> addBefore(Position<E> pos, E value) {
        Node<E> nextNode = checkPosition(pos); //postNode porque toma el papel de nodo siguiente al que se quiere añadir
        Node<E> newNode = new Node(value, nextNode.getPrev(), nextNode);

        nextNode.setPrev(newNode);

        if (newNode.getPrev() != null) //En el caso de el position no es el del primer nodo de la lista
        {
            newNode.getPrev().setNext(newNode);
        } else //Si el position es el primer elemento de la lista, sería como insertar por cabecera, hay que controlar el head
        {
            this.head = newNode;
        }

        size++;

        return newNode;
    }

    @Override
    public E remove(Position<E> pos) {
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }
        Node<E> node = checkPosition(pos);

        if (node.getPrev() == null) { //Se quiere eliminar el primer elemento de la lista
            this.head = node.getNext();//Se pone que head sea el siguiente elemento
            if (node.getNext() != null)// En el caso de que haya mas de 1 elemento en la lista 
            {
                node.getNext().setPrev(null);
                node.setNext(null); //Quizá no haga falta
            } //No se hace else para cubrir el caso en el que solo haya 1 elemento en la lista, no hace falta ya que head apunta a null
        } else if (node.getNext() == null) { //Se quiere eliminar el ultimo elemento de la lista
            node.setPrev(null); //Quizá no haga falta
            node.getPrev().setNext(null);
        } else { //Se quiere eliminar un elemento entre medias de la lista
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
        size--;
        return node.getElement();
    }

    @Override
    public Position<E> get() { //Devuelve la cabeza de la lista
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }
        return this.head;
    }

    @Override
    public Position<E> set(Position<E> pos, E value) {
        Node<E> node = checkPosition(pos);
        node.setElem(value);
        return node;
    }

    @Override
    public Position<E> search(E value) {
        if (this.isempty()) {
            throw new RuntimeException("The list is empty");
        }
        Iterator<Position<E>> iterator = iterator(); //Crear instancia del iterador
        while (iterator.hasNext()) {
            Position<E> current = iterator.next(); //Aqui te devuleve la posicion actual y se mueve al siguiente
            if (current.getElement().equals(value)) {
                return current;
            }
        }
        return null;
    }

    @Override
    public boolean contains(E value) {
        return (this.search(value) != null);
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new MyLinkedListBetterIterator(this.head);
    }

    public class MyLinkedListBetterIterator implements Iterator<Position<E>> {  

        public Node<E> current;

        public MyLinkedListBetterIterator(Node<E> node) { //Se creará un iterator a partir de un nodo, se irá recorriendo hacia delante
            this.current = node;
        }

        @Override
        public boolean hasNext() {
            return (current.getNext() != null);
        }

        @Override
        public Position<E> next() {//Devuelve la posicion actual y avanza a la siguiente
            Position<E> pos = this.current;
            Node<E> newCurrent = this.current;
            this.current = newCurrent.getNext();

            return pos;
        }

    }

}
