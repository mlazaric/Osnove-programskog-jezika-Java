package searching.demo;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

public class SlagalicaMain {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program expects exactly one argument.");
            System.exit(1);
        }

        if (args[0].length() != 9) {
            System.out.println("The argument's length should be 9.");
            System.exit(1);
        }

        int[] configuration = new int[args[0].length()];
        int index = 0;

        for (char digit : args[0].toCharArray()) {
            configuration[index] = digit - '0';
            ++index;
        }

        KonfiguracijaSlagalice konfig = null;

        try {
            konfig = new KonfiguracijaSlagalice(configuration);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        Slagalica slagalica = new Slagalica(konfig);
        Node<KonfiguracijaSlagalice> solution = SearchUtil.bfsv(slagalica, slagalica, slagalica);

        if (solution == null) {
            System.out.println("No solutions were found.");
        }
        else {
            SlagalicaViewer.display(solution);
        }
    }

}
