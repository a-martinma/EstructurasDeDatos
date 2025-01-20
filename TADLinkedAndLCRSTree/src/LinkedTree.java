import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import material.Position;

/**
 *
 * @author Álvaro Martín Martín.
 * @param <E>
 */
public class LinkedTree<E> implements NAryTree<E> {

    private class TreeNode<T> implements Position<T> {

        private T elem;
        private TreeNode<T> parent;
        private ArrayList<TreeNode<T>> children;

        public TreeNode(T elem, TreeNode<T> parent, ArrayList<TreeNode<T>> children) {
            this.elem = elem;
            this.parent = parent;
            this.children = children;
        }

        @Override
        public T getElement() {
            return this.elem;
        }

        public TreeNode<T> getParent() {
            return parent;
        }

        public ArrayList<TreeNode<T>> getChildren() {
            return children;
        }

        public void setElem(T elem) {
            this.elem = elem;
        }

        public void setParent(TreeNode<T> parent) {
            this.parent = parent;
        }

        public void setChildren(ArrayList<TreeNode<T>> children) {
            this.children = children;
        }

    }

    private TreeNode<E> root = null;

    private TreeNode<E> checkPosition(Position<E> pos) {
        if ((pos == null) || !(pos instanceof TreeNode)) {
            throw new RuntimeException("The position is invalid");
        }
        return (TreeNode<E>) pos;
    }

    private LinkedTree<E> checkLinkedTree(NAryTree<E> t) {   //Se usará en el método attach 
        if ((t.isEmpty()) || !(t instanceof LinkedTree)) {
            throw new RuntimeException("The tree is invalid");
        }
        return (LinkedTree<E>) t;
    }

    @Override
    public Position<E> addRoot(E e) {
        if (this.root() != null) {
            throw new RuntimeException("El arbol ya tiene raiz");
        }
        this.root = new TreeNode(e, null, new ArrayList<>());
        return this.root;
    }

    @Override
    public Position<E> add(E element, Position<E> p) { //p es el position del padre, se añadirá a la lista de hijos del padre
        TreeNode<E> parent = checkPosition(p);
        TreeNode<E> newNode = new TreeNode(element, parent, new ArrayList<>());
        parent.children.add(newNode);
        return newNode;

    }

    @Override
    public Position<E> add(E element, Position<E> p, int n) { //Añadir como hijo n de un padre
        TreeNode<E> parent = checkPosition(p);
        if ((n < 0) || (n > parent.getChildren().size())) {
            throw new RuntimeException("No se puede introducir el hijo en esa posicion");
        }

        TreeNode<E> newNode = new TreeNode(element, parent, new ArrayList<>());
        parent.getChildren().add(n, newNode);
        return newNode;
    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) { //Intercambiar el valor de los elementos de un nodo
        TreeNode<E> node1 = checkPosition(p1);
        TreeNode<E> node2 = checkPosition(p2);
        E aux = node1.getElement();
        node1.setElem(node2.getElement());
        node2.setElem(aux);
    }

    @Override
    public E replace(Position<E> p, E e) { //Modificar el elemento de un nodo. Devuelve el valor reemplazado
        TreeNode<E> node = checkPosition(p);
        E valorAntes = node.getElement();
        node.setElem(e);
        return valorAntes;
    }

    @Override
    public void remove(Position<E> p) {
        if (this.isEmpty()) {
            throw new RuntimeException("No se puede eliminar nada, el arbol esta vacio");
        }

        TreeNode<E> node = checkPosition(p);
        if (node.getParent() == null) //Si el nodo a eliminar es la raiz
        {
            this.root = null;
        } else {
            node.getParent().getChildren().remove(node); //En memoria sigue existiendo el nodo (y sus hijos si tuviera)pero cuando te quieras recorrer el árbol
        }                                                //no se va a poder acceder a ese nodo. Es como si no existiera. AUNQUE NO ESTOY 100% SEGURO DE SI FUNCIONA ASÍ.
    }

    @Override
    public NAryTree<E> subTree(Position<E> v) {
        if (this.isEmpty()) {
            throw new RuntimeException("No se puede crear un subárbol, el árbol está vacío");
        }

        TreeNode<E> newRoot = checkPosition(v);
        if (newRoot.getParent() == null) //El nodo dado es la raiz, devuelve el árbol completo
        {
            return this;
        } else {
            newRoot.getParent().getChildren().remove(newRoot); //Desvinculación del nodo del árbol original. remove() no elimina de memoria newRoot, en la siguiente linea se puede utilizar sin problema
            newRoot.setParent(null); //Desvinculación del nodo del árbol original
            LinkedTree<E> nuevoArbol = new LinkedTree();
            nuevoArbol.root = newRoot;
            return nuevoArbol;
        }

    }

    @Override
    public void attach(Position<E> p, NAryTree<E> t) {
        if (this.isEmpty()) {
            throw new RuntimeException("No se puede añadir un subárbol a ningun nodo porque el árbol está vacío");
        }
        TreeNode<E> node = checkPosition(p);
        LinkedTree<E> tree = checkLinkedTree(t);
        node.getChildren().add(tree.root);
        tree.root.parent = node;
    }

    @Override
    public boolean isEmpty() {
        return (this.root() == null);
    }

    @Override
    public Position<E> root() {
        return this.root;
    }

    @Override
    public Position<E> parent(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return (node.getParent());
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return (node.getChildren());
    }

    @Override
    public boolean isInternal(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return (!node.children.isEmpty());
    }

    @Override
    public boolean isLeaf(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return (node.children.isEmpty());
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return (node.getParent() == null);
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new LinkedTreeIterator(this.root);//Siempre se va a recorrer desde la raíz. No hace falta haber hecho el iterator a mi manera, 
    }                                            //la manera en la que se ha hecho en clase también es válida

    public int size() {

        int size = 0;
        Iterator<Position<E>> iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            size++;
        }
        return size;

    }

    /*
    public class LinkedTreeIterator<E> implements Iterator<Position<E>>{ //Echo como en clase
        
        private LinkedTree<E> current; //Iterador pensado para que se recorra todo el árbol, no desde un nodo dado.
        private Queue<Position<E>> cola = new LinkedList<>();
        
        
        public LinkedTreeIterator(LinkedTree<E> arbol){
            this.current = arbol;
            
            if(arbol.isEmpty())
                throw new RuntimeException("El árbol está vacío, no se puede recorrer");
            this.cola.add(arbol.root()); //Solo se necesita añadir a la cola la raiz, el metodo next añadirá a la cola cada uno de los hijos...
        }

        @Override
        public boolean hasNext() {
            return (!cola.isEmpty());
        }

        @Override
        public Position<E> next() {
            Position<E> pos = cola.remove(); //Remove() elimina y devuelve el primero en la cola. poll() también lo hace pero este no lanza excepcion si la cola está vacía
            Iterable<? extends Position<E>> hijos = current.children(pos); //OJO ahí
            for(Position<E> hijo: hijos)
                cola.add(hijo);
            
            return pos;
        }
        
    }
     */
    public class LinkedTreeIterator<E> implements Iterator<Position<E>> { //Echo a mi manera

        private TreeNode<E> current; //Iterador pensado para que se recorra el árbol desde un nodo dado. (No hace falta hacerlo así ya que siempres se va a recorrer desde la raíz)
        private Queue<Position<E>> cola = new LinkedList<>();

        public LinkedTreeIterator(TreeNode<E> nodoPartida) {
            this.current = nodoPartida;
            this.cola.add(nodoPartida); //Solo se necesita añadir a la cola la raiz, el metodo next añadirá a la cola cada uno de los hijos...
        }

        @Override
        public boolean hasNext() {
            return (!cola.isEmpty());
        }

        @Override
        public Position<E> next() {
            Position<E> pos = cola.remove(); //Remove() elimina y devuelve el primero en la cola. poll() también lo hace pero este no lanza excepcion si la cola está vacía
            Iterable<? extends Position<E>> hijos = current.getChildren();
            for (Position<E> hijo : hijos) {
                cola.add(hijo);
            }

            return pos;
        }

    }

}
