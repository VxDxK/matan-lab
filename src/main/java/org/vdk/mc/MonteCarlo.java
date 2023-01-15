package org.vdk.mc;

import org.vdk.util.Point2D;
import org.vdk.util.Point3D;
import org.vdk.util.Polygon;
import org.vdk.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MonteCarlo extends Integral{
    private final double height = 50;
    private final long N;
    private final Polygon test;
    private final double all;

    public MonteCarlo(Polygon polygon, Function<Point2D, Point3D> function, long N) {
        super(polygon, function);
        this.N = N;
        this.test = new Polygon() {
            @Override
            public double area() {
                return Math.abs(super.area());
            }
        };
        test.getVertex().add(new Point2D(polygon.getRangeX().getBegin(), polygon.getRangeY().getBegin()));
        test.getVertex().add(new Point2D(polygon.getRangeX().getBegin(), polygon.getRangeY().getEnd()));
        test.getVertex().add(new Point2D(polygon.getRangeX().getEnd(), polygon.getRangeY().getEnd()));
        test.getVertex().add(new Point2D(polygon.getRangeX().getEnd(), polygon.getRangeY().getBegin()));
        all = test.area() * height;
    }


    public Pair<Double, Double> integrateAndGetErr() throws Exception {
        Random rnd = new Random();

        List<Point2D> points = new ArrayList<>();

        while (points.size() != N) {
            Point2D point = new Point2D(rnd.nextDouble(polygon.getRangeX().getBegin(), polygon.getRangeX().getEnd()), rnd.nextDouble(polygon.getRangeY().getBegin(), polygon.getRangeY().getEnd()));
            if(polygon.checkPoint(point)) {
                points.add(point);
            }
        }

        double sum = points.stream().map(function).mapToDouble(Point3D::getZ).sum();


        double D = 0;
        for (int i = 0; i < N; i++)
            D += pow(function.apply(points.get(i)).z - (sum / N), 2);
        D *= (1d)/(N - 1d);

        double err = test.area() / sqrt(N) * sqrt(D);

        return new Pair<>(test.area() / N * sum, err);
    }


    @Override
    public double integrate() throws Exception {
        return integrateAndGetErr().first;
    }
}
