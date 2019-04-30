package coloring.demo;

import coloring.algorithms.Coloring;
import coloring.algorithms.Pixel;
import coloring.algorithms.SubspaceExploreUtil;
import marcupic.opjj.statespace.coloring.FillAlgorithm;
import marcupic.opjj.statespace.coloring.FillApp;
import marcupic.opjj.statespace.coloring.Picture;

import java.util.Arrays;

public class Bojanje2 {

    public static void main(String[] args) {
        FillApp.run(FillApp.OWL, Arrays.asList(bfs, dfs, bfsv));
    }

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
