package org.vdk;

import org.vdk.mc.MonteCarlo;
import org.vdk.mc.NumericIntegration;
import org.vdk.util.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Main {
    //[31, 42] × [−5, 9]
    private final static BiFunction<Double, Double, Double> function = (x, y) -> y * log(x);
    private final static Function<Point2D, Point3D> dFunction = (p) -> new Point3D(p.x, p.y, function.apply(p.x, p.y));

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java -jar app.jar mode polygon_filename");
            System.out.println("modes: num num_err monte monte_err");
            System.exit(-1);
        }

        Polygon polygon = PolygonReader.read(args[1]);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if (args[0].equalsIgnoreCase("num")) {
            System.out.print("Enter xy step: ");
            double step = Double.parseDouble(br.readLine());
            NumericIntegration numericIntegration = new NumericIntegration(polygon, dFunction, step);
            System.out.println(numericIntegration.integrate());
        } else if (args[0].equalsIgnoreCase("num_err")) {
            System.out.println("Enter three numbers for N: start iteration_count");
            List<Double> collect = Arrays.stream(br.readLine().split(" ")).map(Double::parseDouble).toList();
            double startGrid = collect.get(0);
            NumericIntegration numericIntegration = new NumericIntegration(polygon, dFunction, startGrid);
            double lastValue = numericIntegration.integrate();
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("result.csv"))) {
                for (int i = 1; i <= collect.get(1); i++) {
                    double newGrid = startGrid / (2 << i);
                    NumericIntegration numericIntegration2 = new NumericIntegration(polygon, dFunction, newGrid);
                    double integrateD = numericIntegration2.integrate();
                    bufferedWriter.write(String.format("%d;%f\n", i, abs(integrateD - lastValue)));
                    lastValue = integrateD;
                }
            }
        } else if (args[0].equalsIgnoreCase("monte")) {
            System.out.print("Enter number of points for monte-carlo method: ");
            long points = Long.parseLong(br.readLine());
            MonteCarlo MC = new MonteCarlo(polygon, dFunction, points);
            System.out.println(MC.integrate());
        } else if (args[0].equalsIgnoreCase("monte_err")) {
            System.out.println("Enter three numbers for N: start end step ");
            List<Integer> collect = Arrays.stream(br.readLine().split(" ")).map(Integer::parseInt).toList();
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("result.csv"))) {
                for (int i = collect.get(0); i <= collect.get(1); i += collect.get(2)) {
                    MonteCarlo MC = new MonteCarlo(polygon, dFunction, i);
                    Pair<Double, Double> valErr = MC.integrateAndGetErr();
                    bufferedWriter.write(String.format("%d;%f\n", i, valErr.second));
                }
            }
        } else {
            System.out.println("Lame mode activated");
        }

        br.close();

    }

}

