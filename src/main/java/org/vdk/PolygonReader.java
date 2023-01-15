package org.vdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PolygonReader {
    public static Polygon read(String filename) throws Exception{
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            int n = Integer.parseInt(br.readLine());
            Polygon polygon = new Polygon();

            for (int i = 0; i < n; i++) {
                Point2D point2D = enterPoint(br);

                polygon.getRangeX().setBegin(min(polygon.getRangeX().getBegin(), point2D.getX()));
                polygon.getRangeX().setEnd(max(polygon.getRangeX().getEnd(), point2D.getX()));

                polygon.getRangeY().setBegin(min(polygon.getRangeY().getBegin(), point2D.getY()));
                polygon.getRangeY().setEnd(max(polygon.getRangeY().getEnd(), point2D.getY()));

                polygon.getVertex().add(point2D);
            }
            return polygon;
        }
    }
    public static Point2D enterPoint(BufferedReader bufferedReader) throws IOException {
        String[] s = bufferedReader.readLine().trim().split(" ");

        return new Point2D(Double.parseDouble(s[0]), Double.parseDouble(s[1]));
    }
}
