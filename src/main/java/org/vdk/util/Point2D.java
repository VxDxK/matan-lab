package org.vdk.util;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Point2D {
    public double x;
    public double y;

    public Point3D toPoint3D() {
        return new Point3D(x, y, 0);
    }

}
