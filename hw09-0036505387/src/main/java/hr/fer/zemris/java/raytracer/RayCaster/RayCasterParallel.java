package hr.fer.zemris.java.raytracer.RayCaster;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import static hr.fer.zemris.java.raytracer.RayCaster.RayCaster.tracer;
import static hr.fer.zemris.java.raytracer.RayCaster.RayCasterParallel2.getIRayTracerProducer;

/**
 * Models a simple ray caster using Phong's model for lighting and {@link ForkJoinPool} for parallelisation.
 *
 * @author Marko Lazarić
 */
public class RayCasterParallel {

    /**
     * Creates a predefined scene and renders it.
     *
     * @param args the arguments are ignored
     */
    public static void main(String[] args) {
        RayTracerViewer.show(getIRayTracerProducer(RayTracerViewer.createPredefinedScene()),
                new Point3D(10,0,0),
                new Point3D(0,0,0),
                new Point3D(0,0,10),
                20, 20);
    }

}