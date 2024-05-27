package aed;

import java.util.*;

// Todos los tipos de datos "Comparables" tienen el método compareTo()
// elem1.compareTo(elem2) devuelve un entero. Si es mayor a 0, entonces elem1 > elem2
public class ABB<T extends Comparable<T>> implements Conjunto<T> {
    private Nodo raiz;
    private int cardinal;

    private class Nodo {
        T valor;
        Nodo izq;
        Nodo der;
        Nodo ant;
        
        Nodo (T v) { 
            valor = v; 
            izq = null;
            der = null;
            ant = null;
        }
    }

    public ABB() {
        raiz = null;
        cardinal = 0;
    }

    public int cardinal() {
        return cardinal;
    }

    private T buscar_minimo(Nodo x) {
        if (x.izq == null) {
            return x.valor;
        }
        else {
            return buscar_minimo(x.izq);
        }
    }

    public T minimo(){
        Nodo actual = raiz;
        return buscar_minimo(actual);
    }

    private T buscar_maximo(Nodo x) {
        if (x.der == null) {
            return x.valor;
        }
        else {
            return buscar_maximo(x.der);
        }
    }

    public T maximo(){
        Nodo actual = raiz;
        return buscar_maximo(actual);
    }
 
    private void insertar_nodo(Nodo nuevo) {
        Nodo principal = raiz;
        Nodo ultimo_buscado = null;
        while (principal != null) {
            ultimo_buscado = principal;
            if (nuevo.valor.compareTo(principal.valor) > 0) {
                principal = principal.der;
            }
            else {
                principal = principal.izq;
            }
        }
        if (ultimo_buscado == null) {
            raiz = nuevo; // en este caso el árbol estaba vacío y el elemento nuevo es el primero en agregarse
        }
        else if (nuevo.valor.compareTo(ultimo_buscado.valor) > 0) {
            ultimo_buscado.der = nuevo;
            ultimo_buscado.der.ant = ultimo_buscado;
        }
        else {
            ultimo_buscado.izq = nuevo;
            ultimo_buscado.izq.ant = ultimo_buscado;
        }
    }

    public void insertar(T elem){
        if (pertenece(elem) == false){
            Nodo nuevo = new Nodo(elem);
            insertar_nodo(nuevo); 
            cardinal++;
        }
    }

    private boolean busqueda_recursiva(Nodo actual, T elem) {
        if (actual == null) {
            return false;
        }
        if (actual.valor.equals(elem)) {
            return true;
        }
        else {
            if (elem.compareTo(actual.valor) > 0) {
                return busqueda_recursiva(actual.der, elem);
            }
            else {
                return busqueda_recursiva(actual.izq, elem);
            }
        }
    }

    public boolean pertenece(T elem){
       Nodo aux = raiz;
       return busqueda_recursiva(aux, elem);
    }

    private Nodo buscar(Nodo actual, T elem) {
        if (actual.valor.equals(elem)) {
            return actual;
        }
        else {
            if (elem.compareTo(actual.valor) > 0) {
                return buscar(actual.der, elem);
            }
            else {
                return buscar(actual.izq, elem);
            }
        }
    }

    private Nodo buscar_nodo_minimo(Nodo n) {
        if (n.izq == null) {
            return n;
        }
        else {
            return buscar_nodo_minimo(n.izq);
        }
    }

    private Nodo sucesor(Nodo x) {
        return buscar_nodo_minimo(x.der);
    }
    
    public void eliminar(T elem){
        Nodo aux = buscar(raiz, elem);
        if (pertenece(elem)) {
            if (aux.izq == null && aux.der == null) {      // Eliminar si no tiene descendencia
                if (aux.ant.valor.compareTo(aux.valor) > 0) {
                    aux.ant.der = null;
                }
                else {
                    aux.ant.izq = null;
                }
            }
            else if (aux.izq != null && aux.der == null) {    // Eliminar si tiene descendencia por izq
                aux.valor = aux.izq.valor;
                aux.izq = null;
            }
            else if (aux.izq == null && aux.der != null) {    // Eliminar si tiene descendencia por der
                aux.valor = aux.der.valor;
                aux.der = null;
            }
            else {                                            // Eliminar si tiene doble descendencia
                aux.valor = sucesor(aux).valor;
                if (sucesor(aux).ant.izq != null && sucesor(aux).ant.der != null) {  // Caso en el que el anterior al nodo a eliminar tiene doble descendencia
                    if (sucesor(aux).ant.izq.equals(sucesor(aux))) {
                        sucesor(aux).ant.izq = null;
                    }
                    else {
                        sucesor(aux).ant.der = null;
                    }
                }
                else if (sucesor(aux).ant.izq.equals(null)) {     // Caso en el que el anterior al nodo a eliminar tiene como única descendencia al nodo a eliminar (ubicado como hijo derecho)
                    sucesor(aux).ant.der = null;
                }
                else {
                    sucesor(aux).ant.izq = null;                // Caso en el que el anterior al nodo a eliminar tiene como única descendencia al nodo a eliminar (ubicado como hijo izquierdo)
                }
            }
        }
        cardinal--;
    }

    public String toString(){
        String res = "{";
        List<T> arbol_en_orden = inorder(raiz);
        for (int j=0; j < arbol_en_orden.size(); j++) {
            if (j != arbol_en_orden.size() - 1) {
                res = res + arbol_en_orden.get(j) + ",";
            }
            else {
                res = res + arbol_en_orden.get(j);
            }
        }
        res = res + "}";
        return res;
    }

    private List<T> añadir_elementos(List<T> nueva, List<T> lista) {
        for (int i=0; i < lista.size(); i++) {
            nueva.add(lista.get(i));
        }
        return nueva;
    }

    private List<T> inorder(Nodo x) {
        List<T> res = new ArrayList<T>();
        if (x == null) {
            return res;
        }
        else {
            añadir_elementos(res, inorder(x.izq));
            res.add(x.valor);
            añadir_elementos(res, inorder(x.der));
        }
        return res;
    }


    private class ABB_Iterador implements Iterador<T> {
        private Nodo _actual = raiz;
        private int contador;

        List<T> lista_en_orden = inorder(_actual);

        public boolean haySiguiente() {            
            return (contador != cardinal());
        }
    
        public T siguiente() {
            T siguiente = lista_en_orden.get(contador);
            contador++;
            return siguiente;
        }
    }

    public Iterador<T> iterador() {
        return new ABB_Iterador();
    }

}
