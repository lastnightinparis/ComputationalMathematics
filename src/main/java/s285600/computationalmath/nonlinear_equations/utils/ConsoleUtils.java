package s285600.computationalmath.nonlinear_equations.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

/**
 * @author Kir
 * Created on 13.03.2021
 */

public class ConsoleUtils {
    private BufferedReader reader;
    private Writer writer;

    public ConsoleUtils(BufferedReader reader, Writer writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void writeln(String s) {
        try {
            writer.write(s + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DoubleUnaryOperator read() {
        writeln("Choose an option:");
        writeln("1. Equation");
        writeln("2. System");
        try {
            int choice = Integer.parseInt(reader.readLine().trim());
            switch (choice) {
                case 1:
                    return readEquation();
                case 2:
                    return readSystem();
                default:
                    writeln("Unexpected value");
                    return read();
            }
        } catch (NumberFormatException e) {
            writeln("Enter a number");
            return read();
        } catch (IOException e) {
            writeln("Choose a valid option");
            return read();
        }


    }

    private DoubleUnaryOperator readSystem() {
        writeln("Choose system:");
        writeln("");
        writeln("");
        return null;
    }

    private DoubleUnaryOperator readEquation() {
        writeln("Choose equation:");
        writeln("1. ax^3 + bx^2 + cx + d");
        writeln("2. asin(x) + bcos(x)");
        writeln("3. ax^2 + bx + c");
        writeln("4. sin(x) + ax^2");
        writeln("5. cos(x) + bx^3");
        try {
            int choice = Integer.parseInt(reader.readLine().trim());
            switch (choice) {
                case 1:
                    double[] thr_deg = readCoefs(4);
                    DoubleUnaryOperator f1 = x -> thr_deg[0] * x * x * x + thr_deg[1] * x * x + thr_deg[2] * x + thr_deg[3];
                    return f1;
                case 2:
                    double[] trig = readCoefs(2);
                    DoubleUnaryOperator f2 = x -> trig[0] * Math.sin(x) + trig[1] * Math.cos(x);
                    return f2;
                case 3:
                    double[] sec_deg = readCoefs(3);
                    DoubleUnaryOperator f3 = x -> sec_deg[0] * x * x + sec_deg[1] * x + sec_deg[2];
                    return f3;
                case 4:
                    double[] sin_with_sq = readCoefs(1);
                    DoubleUnaryOperator f4 = x -> Math.sin(x) + sin_with_sq[0] * x * x;
                    return f4;
                case 5:
                    double[] cos_with_thr = readCoefs(1);
                    DoubleUnaryOperator f5 = x -> Math.cos(x) + cos_with_thr[0] * x * x * x;
                    return f5;
                default:
                    writeln("Unexpected value");
                    return readEquation();
            }
        } catch (IOException | NumberFormatException e) {
            return readEquation();
        }
    }

    private double[] readCoefs(int num) {
        double[] arr = new double[num];
        System.out.println("Последовательно введите " + num + " коэффициента:");
        try {
            for (int i = 0; i < num; i++)
                arr[i] = Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Enter a number");
            readCoefs(num);
        }
        return arr;
    }
}
