package org.vdk.mc;

import org.vdk.mc.util.MonteCarloCallableN;
import org.vdk.mc.util.MonteCarloCallableP;
import org.vdk.util.Point2D;
import org.vdk.util.Point3D;
import org.vdk.util.Polygon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

public class MonteCarloConcurrent extends Integral {
    private final int threadNumbers;
    private final long pointsAtAll;
    private final double height = 100000;
    private final ExecutorService executorService;
    private final Polygon test;
    private final double all;

    public MonteCarloConcurrent(int threadNumbers, long pointsAtAll, Polygon polygon, Function<Point2D, Point3D> function, ExecutorService executorService) {
        super(polygon, function);
        this.threadNumbers = threadNumbers;
        this.pointsAtAll = pointsAtAll;
        this.executorService = executorService;
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

    @Override
    public double integrate() throws Exception {

        List<Callable<Long>> listPositive = new ArrayList<>();
        List<Callable<Long>> listNegative = new ArrayList<>();
        long actualPointCount = pointsAtAll / threadNumbers;
        for (int i = 0; i < threadNumbers; i++) {
            listPositive.add(new MonteCarloCallableP(polygon, actualPointCount, height, function));
        }
        for (int i = 0; i < threadNumbers; i++) {
            listNegative.add(new MonteCarloCallableN(polygon, actualPointCount, height, function));
        }
        List<Future<Long>> futuresPositive = executorService.invokeAll(listPositive);
        List<Future<Long>> futuresNegative = executorService.invokeAll(listNegative);
        long sumP = 0;
        long sumN = 0;
        while (!futuresPositive.isEmpty() && !futuresNegative.isEmpty()) {
            sumP += getSumIterator(futuresPositive);
            sumN += getSumIterator(futuresNegative);
        }
        double positiveIntegral = all * (double) sumP / (actualPointCount * threadNumbers);
        double negativeIntegral = all * (double) sumN / (actualPointCount * threadNumbers);

        executorService.shutdown();

        return positiveIntegral - negativeIntegral;
    }

    private long getSumIterator(List<Future<Long>> futuresPositive) throws InterruptedException, ExecutionException {
        Iterator<Future<Long>> iteratorPositive = futuresPositive.iterator();
        long sum = 0;
        while (iteratorPositive.hasNext()) {
            Future<Long> now = iteratorPositive.next();
            if (now.isDone()) {
                sum += now.get();
                iteratorPositive.remove();
            }
        }
        return sum;
    }

}
