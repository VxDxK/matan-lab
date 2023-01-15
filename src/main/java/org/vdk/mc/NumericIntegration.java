package org.vdk.mc;

import lombok.AllArgsConstructor;
import org.vdk.Point2D;
import org.vdk.Point3D;
import org.vdk.Polygon;

import java.util.function.Function;

public class NumericIntegration extends Integral {
    private final double gridStep;

    public NumericIntegration(Polygon polygon, Function<Point2D, Point3D> function, double gridStep) {
        super(polygon, function);
        this.gridStep = gridStep;
    }

    @Override
    public double integrate() throws Exception {
        double ans = 0;

        for (double xI = polygon.getRangeX().getBegin(); xI <= polygon.getRangeX().getEnd(); xI += gridStep) {
            for (double yI = polygon.getRangeY().getBegin(); yI < polygon.getRangeY().getEnd(); yI += gridStep) {
                Point2D point = new Point2D(xI, yI);
                if(polygon.checkPoint(point)) {
                    Point3D apply = function.apply(point);
                    ans += apply.getZ() * (gridStep * gridStep);
                }
            }
        }
        return ans;
    }
}
