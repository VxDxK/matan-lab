package org.vdk.mc;

import lombok.AllArgsConstructor;
import org.vdk.util.Point2D;
import org.vdk.util.Point3D;
import org.vdk.util.Polygon;

import java.util.function.Function;

@AllArgsConstructor
public abstract class Integral {
    protected final Polygon polygon;
    protected final Function<Point2D, Point3D> function;

    public abstract double  integrate() throws Exception;
}
