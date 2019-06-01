package hr.fer.zemris.math.demo;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple test program to make sure {@link ComplexRootedPolynomial} etc. work. It uses no parallelisation.
 * For the actual task look at {@link hr.fer.zemris.java.fractals.Newton.Newton}.
 */
public class FractalDemo {

    public static void main(String[] args) {
        ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                Complex.ONE, Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
        );

        FractalViewer.show(new MojProducer(crp));
    }

    public static class MojProducer implements IFractalProducer {
        private final ComplexRootedPolynomial crp;
        private final ComplexPolynomial dcrp;

        public MojProducer(ComplexRootedPolynomial crp) {
            this.crp = crp;
            this.dcrp = crp.toComplexPolynom().derive();
            System.out.println(crp);
            System.out.println(dcrp);
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            short[] data = new short[width * height];
            int m = 16*16*16;
            int offset = 0;

            for(int y = 0; y < height; y++) {
                if(cancel.get()) break;
                for(int x = 0; x < width; x++) {
                    double cre = x / (width-1.0) * (reMax - reMin) + reMin;
                    double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;

                    Complex zn = new Complex(cre, cim);
                    Complex znold = null;

                    int iters = 0;

                    do {
                        znold = zn;
                        zn = zn.sub(crp.apply(zn).divide(dcrp.apply(zn)));

                        iters++;
                    } while(iters < m && zn.sub(znold).module() > 1e-3);

                    data[offset] = (short) (crp.indexOfClosestRootFor(zn, 1e-3) + 1);

                    offset++;
                }
            }
            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short)(crp.toComplexPolynom().order() + 1), requestNo);
        }
    }

}
