package org.vdk.util;

import lombok.*;
import org.vdk.util.Point2D;

@Getter
@Setter
@EqualsAndHashCode
public class Point3D extends Point2D {
    public double z;
    public Point3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
