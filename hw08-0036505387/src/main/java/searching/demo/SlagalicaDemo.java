package searching.demo;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Demonstrates the usage of {@link SearchUtil} to solve a simple puzzle.
 *
 * @author Marko Lazarić
 */
public class SlagalicaDemo {

    public static void main(String[] args) {
        Slagalica slagalica = new Slagalica(
                new KonfiguracijaSlagalice(new int[] {2,3,0,1,4,6,7,5,8})
        );

        Node<KonfiguracijaSlagalice> rješenje =
                SearchUtil.bfs(slagalica, slagalica, slagalica);

        printSolution(rješenje);

        Node<KonfiguracijaSlagalice> rješenje2 =
                SearchUtil.bfsv(slagalica, slagalica, slagalica);

        printSolution(rješenje2);

        Slagalica slagalica3 = new Slagalica(
                new KonfiguracijaSlagalice(new int[] {1,6,4,5,0,2,8,7,3})
        );

        Node<KonfiguracijaSlagalice> rješenje3 =
                SearchUtil.bfsv(slagalica3, slagalica3, slagalica3);

        printSolution(rješenje3);
    }

    private static void printSolution(Node<KonfiguracijaSlagalice> rješenje) {
        if(rješenje==null) {
            System.out.println("Nisam uspio pronaći rješenje.");
        } else {
            System.out.println(
                    "Imam rješenje. Broj poteza je: " + rješenje.getCost()
            );
            List<KonfiguracijaSlagalice> lista = new ArrayList<>();
            Node<KonfiguracijaSlagalice> trenutni = rješenje;
            while(trenutni != null) {
                lista.add(trenutni.getState());
                trenutni = trenutni.getParent();
            }
            Collections.reverse(lista);
            lista.stream().forEach(k -> {
                System.out.println(k);
                System.out.println();
            });
        }
    }

}