package org.vdk.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class Polygon {
    private final List<Point2D> vertex = new ArrayList<>();
    private final Range rangeX = new Range(Double.MAX_VALUE, Double.MIN_VALUE);
    private final Range rangeY = new Range(Double.MAX_VALUE, Double.MIN_VALUE);
    public boolean checkPoint(Point2D point2D) {
        return MathUtil.pointInPolygon(vertex.toArray(new Point2D[0]), point2D);
    }

    public double area() {
        int n = vertex.size();
        Point2D[] p = vertex.toArray(new Point2D[0]);
        double total = 0;
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            total += (p[i].x * p[j].y)
                    - (p[j].x * p[i].y);
        }
        return total / 2;
    }

}
