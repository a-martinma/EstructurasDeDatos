package Tree.BinaryTree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import material.Position;

/**
 *
 * @author Álvaro Martín Martín.
 */

public class LinkedBinaryTree<E> implements BinaryTree<E> {

    public class BTNode<T> implements Position<T> {

        T element;
        BTNode<T> parent;
        BTNode<T> left;
        BTNode<T> right;

        public BTNode(T element, BTNode<T> parent, BTNode<T> left, BTNode<T> right) {
            this.element = element;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public BTNode<T> getParent() {
            return parent;
        }

        public BTNode<T> getLeft() {
            return left;
        }

        public BTNode<T> getRight() {
            return right;
        }

        @Override
        public T getElement() {
            return this.element;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public void setParent(BTNode<T> parent) {
            this.parent = parent;
        }

        public void setLeft(BTNode<T> left) {
            this.left = left;
        }

        public void setRight(BTNode<T> right) {
            this.right = right;
        }

    }

    BTNode<E> root = null;

    private BTNode<E> checkPosition(Position<E> pos) {
        if ((pos == null) || !(pos instanceof BTNode)) {
            throw new RuntimeException("The position is invalid");
        } else {
            return (BTNode<E>) pos;
        }
    }

    private LinkedBinaryTree<E> checkTree(BinaryTree<E> tree) {
        if ((tree.isEmpty()) || !(tree instanceof LinkedBinaryTree)) {
            throw new RuntimeException("The tree is invalid");
        } else {
            return (LinkedBinaryTree<E>) tree;
        }
    }

    @Override
    public Position<E> left(Position<E> v) {
        BTNode<E> node = checkPosition(v);
        if(node.getLeft()==null)
            throw new RuntimeException("El nodo no tiene hijo izquierdo");
        return node.getLeft();
    }

    @Override
    public Position<E> right(Position<E> v) {
        BTNode<E> node = checkPosition(v);
        if(node.getRight()==null)
            throw new RuntimeException("El nodo no tiene hijo derecho");
        return node.getRight();
    }

    @Override
    public boolean hasLeft(Position<E> v) {
        BTNode<E> node = checkPosition(v);
        return (node.getLeft() != null);
    }

    @Override
    public boolean hasRight(Position<E> v) {
        BTNode<E> node = checkPosition(v);
        return (node.getRight() != null);
    }

    @Override
    public boolean isInternal(Position<E> v) {
        BTNode<E> node = checkPosition(v);
        return ((node.getLeft() != null) || (node.getRight() != null));
    }

    @Override
    public boolean isLeaf(Position<E> p) {
        BTNode<E> node = checkPosition(p);
        return ((node.getLeft() == null) && (node.getRight() == null));
    }

    @Override
    public boolean isRoot(Position<E> p) {
        BTNode<E> node = checkPosition(p);
        return (node.getParent() == null);
    }

    @Override
    public Position<E> root() {
        return this.root;
    }

    @Override
    public E replace(Position<E> p, E e) {
        BTNode<E> node = checkPosition(p);
        E valorAntes = node.getElement();
        node.setElement(e);
        return valorAntes;
    }

    @Override
    public Position<E> sibling(Position<E> p) { //Depende de qué hermano seas, devolver el de la izquierda o derecha
        BTNode<E> node = checkPosition(p);

        if (this.isRoot(p)) {
            throw new RuntimeException("El nodo no tiene hermanos porque es la raíz");
        }

        if (node.getParent().getLeft().equals(node)) { //Eres el hijo izquierdo, devolver el hijo derecho del padre si tiene
            if (node.getParent().getRight() != null) {
                return node.getParent().getRight();
            }
        } else if (node.getParent().getRight().equals(node)) { //Eres el hijo derecho, devolver el hijo izquierdo del padre si tiene
            if (node.getParent().getLeft() != null) {
                return node.getParent().getLeft();
            }
        }
        //Si llega hasta aquí es que no tiene hermanos
        throw new RuntimeException("El nodo no tiene hermano");
    }

    @Override
    public Position<E> addRoot(E e) {
        if (this.isEmpty()) {
            this.root = new BTNode(e, null, null, null);
            return this.root;
        }
        throw new RuntimeException("El árbol no está vacío, ya tiene raíz");
    }

    @Override
    public Position<E> insertLeft(Position<E> p, E e) {
        BTNode<E> node = checkPosition(p);
        if (node.getLeft() != null) {
            throw new RuntimeException("El nodo ya tiene hijo izquierdo");
        }
        node.setLeft(new BTNode(e, node, null, null));
        return node.getLeft();
    }

    @Override
    public Position<E> insertRight(Position<E> p, E e) {
        BTNode<E> node = checkPosition(p);
        if (node.getRight() != null) {
            throw new RuntimeException("El nodo ya tiene hijo derecho");
        }
        node.setRight(new BTNode(e, node, null, null));
        return node.getRight();
    }

    @Override
    public E remove(Position<E> p) { //No se puede elimiar un nodo con 2 hijos
        BTNode<E> node = checkPosition(p);

        if (this.hasLeft(p) && this.hasRight(p)) { //Tiene 2 hijos
            throw new RuntimeException("No se puede eliminar un nodo con 2 hijos");

        } else if (!this.hasLeft(p) && !this.hasRight(p)) { //No tiene hijos
            if (node.getParent().getLeft().equals(node)) {//El nodo a eliminar es el hijo izquierdo
                node.getParent().setLeft(null);
            } else if (node.getParent().getRight().equals(node)) { //El nodo a eliminar es el hijo derecho
                node.getParent().setRight(null);
            }
            node.setParent(null);
        } else { //Tiene 1 solo hijo, pues ese hijo ocupará la posición del nodo a borrar
            if (node.getLeft() == null) {//Tiene hijo derecho
                node.getParent().setRight(node.getRight());
                node.getRight().setParent(node.getParent());
            } else {
                node.getParent().setLeft(node.getLeft());
                node.getLeft().setParent(node.getParent());
            }
        }
        return node.getElement();
    }

    @Override
    public void swap(Position<E> p1, Position<E> p2) { //Sustituir los valores, no los nodos (si hacemos esto estaría mal)
        BTNode<E> node1 = checkPosition(p1);
        BTNode<E> node2 = checkPosition(p2);
        E aux = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(aux);
    }

    @Override
    public boolean isEmpty() {
        return (this.root == null);
    }

    @Override
    public Position<E> parent(Position<E> v) {
        BTNode<E> node = checkPosition(v);
        if (node.getParent() == null) {
            throw new RuntimeException("El nodo no tiene padre, es la raíz");
        }
        return node.getParent();
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        BTNode<E> node = checkPosition(v);
        LinkedList<BTNode<E>> childrenList = new LinkedList();
        if (node.getLeft() != null) {
            childrenList.add(node.getLeft());
        }
        if (node.getRight() != null) {
            childrenList.add(node.getRight());
        }
        return childrenList;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new LinkedBinaryTreeIterator(this);
    }

    @Override
    public LinkedBinaryTree<E> subTree(Position<E> h) {
        BTNode<E> node = checkPosition(h);
        LinkedBinaryTree<E> newTree = new LinkedBinaryTree();
        if (node.getParent() == null) //El nodo es la raíz del árbol
        {
            newTree.root = node;
            this.root = null;
        } else {
            if (node.getParent().getLeft().equals(node)) { //Si es el nodo izquierdo
                node.getParent().setLeft(null);
            } else if (node.getParent().getLeft().equals(node)) {
                node.getParent().setRight(null);
            }
            node.setParent(null);
            newTree.root = node;
        }
        return newTree;
    }

    @Override
    public void attachLeft(Position<E> h, BinaryTree<E> t1) {
        if (this.isEmpty()) {
            throw new RuntimeException("El árbol está vacío");
        }
        BTNode<E> node = checkPosition(h);
        LinkedBinaryTree<E> tree = checkTree(t1);
        if (node.getLeft() != null) {
            throw new RuntimeException("No se puede añadir un árbol en esa posicion ya que el nodo donde se quiere insertar ya tiene hijo izquierdo");
        }
        tree.root.setParent(node);
        node.setLeft(tree.root);
    }

    @Override
    public void attachRight(Position<E> h, BinaryTree<E> t1) {
        if (this.isEmpty()) {
            throw new RuntimeException("El árbol está vacío");
        }
        BTNode<E> node = checkPosition(h);
        LinkedBinaryTree<E> tree = checkTree(t1);
        if (node.getRight() != null) {
            throw new RuntimeException("No se puede añadir un árbol en esa posicion ya que el nodo donde se quiere insertar ya tiene hijo derecho");
        }
        tree.root.setParent(node);
        node.setRight(tree.root);
    }

    public class LinkedBinaryTreeIterator<E> implements Iterator<Position<E>> {
        
// El iterador te hace el recorrido en anchura
        
        private LinkedBinaryTree<E> current;
        private Queue<Position<E>> cola = new LinkedList();

        public LinkedBinaryTreeIterator(LinkedBinaryTree<E> current) {
            this.current = current;
            cola.add(current.root);
        }

        @Override
        public boolean hasNext() {
            return (!cola.isEmpty());
        }

        @Override
        public Position<E> next() {
            Position<E> pos = cola.remove();
            BTNode<E> node = (BTNode<E>) pos;
            if (node.getLeft() != null) {
                cola.add(node.getLeft());
            }
            if (node.getRight() != null) {
                cola.add(node.getRight());
            }

            return pos;
        }

    }

}
