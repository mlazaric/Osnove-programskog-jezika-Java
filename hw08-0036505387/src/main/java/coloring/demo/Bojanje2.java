package coloring.demo;

import coloring.algorithms.Coloring;
import coloring.algorithms.Pixel;
import coloring.algorithms.SubspaceExploreUtil;
import marcupic.opjj.statespace.coloring.FillAlgorithm;
import marcupic.opjj.statespace.coloring.FillApp;
import marcupic.opjj.statespace.coloring.Picture;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Demonstrates the usage of {@link coloring.algorithms.SubspaceExploreUtil} using coloring of areas as an example.
 *
 * @author Marko Lazarić
 */
public class Bojanje2 {

    public static void main(String[] args) {
        FillApp.run(FillApp.OWL, Arrays.asList(bfs, dfs, bfsv));
    }

    /**
     * Models a fill algorithm using {@link SubspaceExploreUtil#bfs(Supplier, Consumer, Function, Predicate)}.
     *
     * @author Marko Lazarić
     */
    private static final FillAlgorithm bfs = new FillAlgorithm() {

        @Override
        public String getAlgorithmTitle() {
            return "Moj bfs!";
        }

        @Override
        public void fill(int x, int y, int color, Picture picture) {
            Coloring col = new Coloring(new Pixel(x,y), picture, color);
            SubspaceExploreUtil.bfs(col, col, col, col);
        }

    };

    /**
     * Models a fill algorithm using {@link SubspaceExploreUtil#dfs(Supplier, Consumer, Function, Predicate)}.
     *
     * @author Marko Lazarić
     */
    private static final FillAlgorithm dfs = new FillAlgorithm() {

        @Override
        public String getAlgorithmTitle() {
            return "Moj dfs!";
        }

        @Override
        public void fill(int x, int y, int color, Picture picture) {
            Coloring col = new Coloring(new Pixel(x,y), picture, color);
            SubspaceExploreUtil.dfs(col, col, col, col);
        }

    };

    /**
     * Models a fill algorithm using {@link SubspaceExploreUtil#bfsv(Supplier, Consumer, Function, Predicate)}.
     *
     * @author Marko Lazarić
     */
    private static final FillAlgorithm bfsv = new FillAlgorithm() {

        @Override
        public String getAlgorithmTitle() {
            return "Moj bfsv!";
        }

        @Override
        public void fill(int x, int y, int color, Picture picture) {
            Coloring col = new Coloring(new Pixel(x,y), picture, color);
            SubspaceExploreUtil.bfsv(col, col, col, col);
        }

    };

}
