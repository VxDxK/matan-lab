package org.vdk.mc.util;

import lombok.AllArgsConstructor;
import org.vdk.util.Point2D;
import org.vdk.util.Point3D;
import org.vdk.util.Polygon;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;

@AllArgsConstructor
public class MonteCarloCallableP implements Callable<Long> {
    private final Polygon polygon;
    private final long iterations;
    private final double height;
    private final Function<Point2D, Point3D> function;



    @Override
    public Long call() {
        Random random = new Random();
        long hits = 0;
        for (long i = 0; i < iterations; i++) {
            Point3D point = new Point3D(random.nextDouble(polygon.getRangeX().getBegin(), polygon.getRangeX().getEnd()),
                    random.nextDouble(polygon.getRangeY().getBegin(), polygon.getRangeY().getEnd()), random.nextDouble(0, height));
            if (polygon.checkPoint(point)) {
                if (point.z <= function.apply(point).z) {
                    ++hits;
                }
            }
        }
        return hits;
    }
}
