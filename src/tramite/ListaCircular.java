package tramite;

import java.util.ArrayList;
import java.util.List;

public class ListaCircular<T> { //alertas
    private Nodo<T> inicio;

    public void add(T x) {
        Nodo<T> nuevo = new Nodo<>(x);

        if (inicio == null) {
            inicio = nuevo;
            nuevo.setSiguiente(nuevo);
        } else {
            Nodo<T> temp = inicio;
            while (temp.getSiguiente() != inicio) {
                temp = temp.getSiguiente();
            }
            temp.setSiguiente(nuevo);
            nuevo.setSiguiente(inicio);
        }
    }

    public void remove(T x) {
        if (inicio == null) return;

        Nodo<T> actual = inicio;
        Nodo<T> anterior = null;

        do {
            if (actual.getDato().equals(x)) {
                if (anterior == null) {
                    // Solo hay un nodo
                    if (actual.getSiguiente() == actual) {
                        inicio = null;
                    } else {
                        Nodo<T> temp = inicio;
                        while (temp.getSiguiente() != inicio) {
                            temp = temp.getSiguiente();
                        }
                        inicio = actual.getSiguiente();
                        temp.setSiguiente(inicio);
                    }
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                return;
            }

            anterior = actual;
            actual = actual.getSiguiente();
        } while (actual != inicio);
    }

    public List<T> toList() {
        List<T> lista = new ArrayList<>();
        if (inicio == null) return lista;

        Nodo<T> temp = inicio;
        do {
            lista.add(temp.getDato());
            temp = temp.getSiguiente();
        } while (temp != inicio);

        return lista;
    }
}
