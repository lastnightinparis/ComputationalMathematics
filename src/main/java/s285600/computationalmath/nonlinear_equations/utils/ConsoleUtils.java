package s285600.computationalmath.nonlinear_equations.utils;

import javafx.util.Pair;
import org.apache.commons.math3.util.Precision;
import s285600.computationalmath.nonlinear_equations.NonLinearEquationsCompute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

/**
 * @author Kir
 * Created on 13.03.2021
 */

public class ConsoleUtils {
    private BufferedReader reader;
    private Writer writer;
    private NonLinearEquationsCompute nl = new NonLinearEquationsCompute();

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

    public void read() {
        writeln("Choose an option:");
        writeln("1. Equation");
        writeln("2. System");
        try {
            int choice = Integer.parseInt(reader.readLine().trim());
            writeln("Enter epsilon:");
            double eps = Double.parseDouble(reader.readLine());
            switch (choice) {
                case 1:
                    Pair<DoubleUnaryOperator, String> pair = readEquation();
                    writeln("Последовательно введите отрезок:");
                    int l, r;
                    l = Integer.parseInt(reader.readLine().trim());
                    r = Integer.parseInt(reader.readLine().trim());
                    if (l > r) {
                        System.out.println("Неверно заданы координаты отрезка.");
                        read();
                    }
                    Result result_bis = nl.bisections(pair.getKey(), l, r, eps);
                    Result result_ch = nl.chords(pair.getKey(), l, r, eps);
                    if (result_bis.isValid() && result_ch.isValid()) {
                        PlotUtils.drawEq(pair.getKey(), pair.getValue(), l, r, result_bis.getX(), result_ch.getX());
                        System.out.println("Решение методом деления пополам: " + myRound(result_bis.getX(), eps));
                        System.out.println("Количество итераций метода деления пополам: " + result_bis.getIters());
                        System.out.println("Решение методом хорд: " +  myRound(result_ch.getX(), eps));
                        System.out.println("Количество итераций метода хорд: " + result_ch.getIters());
                        System.out.println("Разница между решениями: " + Math.abs(myRound(result_ch.getX(), eps)
                                - myRound(result_bis.getX(), eps)));
                    } else {
                        if (!result_bis.getMessage().equals("default") || !result_ch.getMessage().equals("default"))
                            System.out.println(result_bis.getMessage());
                        else
                            System.out.println("На данном отрезке нет корней.");
                    }
                    break;
                case 2:
                    Solve solve = readSystem(eps);
                    List<Function<double[], Double>> key = solve.getPair().getKey();
                    List<List<Function<double[], Double>>> value = solve.getPair().getValue();
                    Result result = nl.newtonsMethod(0.5, eps, key, value);
                    if (result.isValid()) {
                        if (result.getAns().length != 3) {
                            double rootX = result.getAns()[0];
                            double rootY = result.getAns()[1];
                            System.out.println("Решение системы методом Ньютона:");
                            System.out.println("X = " + myRound(rootX, eps));
                            System.out.println("Y = " + myRound(rootY, eps));
                            System.out.println("Количество итераций метода Ньютона: " + result.getIters());
                            if (solve.getDecr() != null)
                                PlotUtils.drawSystem(solve.getArr(), solve.getDecr(), rootX, rootY);
                        }
                    } else System.out.println("Система не может быть решена методом Ньютона");
                    break;
                default:
                    writeln("Unexpected value");
                    read();
            }
        } catch (NumberFormatException e) {
            writeln("Enter a number");
            read();
        } catch (IOException e) {
            writeln("Choose a valid option");
            read();
        }

    }

    private Solve readSystem(double eps) {
        List<Function<double[], Double>> functions = new ArrayList<>();
        List<List<Function<double[], Double>>> df = new ArrayList<>();
        writeln("Choose system:");
        writeln("{ 1. 3y = 4x^2 + 9x + 10 \n" +
                "{ 2. 5y = 7x + 12x^2 \n");
        writeln("{ 1. cos(x+y) - 2x = 0.5 \n" +
                "{ 2. x^2 + y^3 = 4 \n");
        writeln("{ 1. x^4 + y^4 + z^4 = 1 \n" +
                "{ 2. 4x^2 + 2y^2 = 4z \n" +
                "{ 3. 3x^2 -4y = -z^2 \n");
        try {
            int choice = Integer.parseInt(reader.readLine().trim());
            switch (choice) {
                case 1:
                    DoubleUnaryOperator[] arr_f = new DoubleUnaryOperator[2];
                    List<Function<double[], Double>> eq1_f = new ArrayList<>();
                    List<Function<double[], Double>> eq2_f = new ArrayList<>();
                    arr_f[0] = x -> 4 * Math.pow(x, 2) / 3 + 9 * x / 3 + 10d / 3;
                    arr_f[1] = x -> 7 * x / 5 + 12 * Math.pow(x, 2) / 5;
                    functions.add(x -> 4 * x[0] * x[0] + 9 * x[0] + 10 - 3 * x[1]);
                    functions.add(x -> 7 * x[0] + 12 * x[0] * x[0] - 5 * x[1]);
                    eq1_f.add(x -> 8 * x[0] + 9);
                    eq1_f.add(y -> -3d);
                    eq2_f.add(x -> 7 + 24 * x[0]);
                    eq2_f.add(y -> -5d);
                    df.add(eq1_f);
                    df.add(eq2_f);
                    String[] str = {"3y = 4x^2 + 9x + 10", "5y = 7x + 12x^2"};
                    return new Solve(arr_f, new Pair<>(functions, df), str);
                case 2:
                    DoubleUnaryOperator[] arr_s = new DoubleUnaryOperator[2];
                    arr_s[0] = x -> Math.acos(0.5 + 2 * x) - x;
                    arr_s[1] = x -> Math.cbrt(4 - x * x);

                    functions.add(x -> Math.cos(x[0] + x[1]) - 2 * x[0] - 0.5);
                    functions.add(x -> x[1] * x[1] * x[1] + x[0] * x[0] - 4);

                    ArrayList<Function<double[], Double>> eq1d = new ArrayList<>();
                    ArrayList<Function<double[], Double>> eq2d = new ArrayList<>();
                    eq1d.add(x -> -Math.sin(x[0] + x[1]) - 2);
                    eq1d.add(y -> -Math.sin(y[0] + y[1]));

                    eq2d.add(x -> 2 * x[0]);
                    eq2d.add(y -> 3 * y[1] * y[1]);

                    df.add(eq1d);
                    df.add(eq2d);
                    String[] str_s = new String[]{"cos(x+y) - 2x = 0.5", "x^2 + y^3 = 4"};
                    return new Solve(arr_s, new Pair<>(functions, df), str_s);
                case 3:
                    List<Function<double[], Double>> eq1_t = new ArrayList<>();
                    List<Function<double[], Double>> eq2_t = new ArrayList<>();
                    List<Function<double[], Double>> eq3_t = new ArrayList<>();
                    DoubleUnaryOperator[] arr_t = new DoubleUnaryOperator[2];
                    functions.add(x -> Math.pow(x[0], 4) + Math.pow(x[1], 4) + Math.pow(x[2], 4) - 1);
                    functions.add(x -> 4 * Math.pow(x[0], 2) + 2 * Math.pow(x[1], 2) - 4 * x[2]);
                    functions.add(x -> 3 * Math.pow(x[0], 2) - 4 * x[1] + Math.pow(x[2], 2));
                    eq1_t.add(x -> 4 * x[0] * x[0] * x[0]);
                    eq1_t.add(y -> 4 * y[1] * y[1] * y[1]);
                    eq1_t.add(z -> 4 * z[2] * z[2] * z[2]);

                    eq2_t.add(x -> 8 * x[0]);
                    eq2_t.add(y -> 4 * y[1]);
                    eq2_t.add(z -> -4d);

                    eq3_t.add(x -> 6 * x[0]);
                    eq3_t.add(y -> -4d);
                    eq3_t.add(z -> 2 * z[2]);

                    df.add(eq1_t);
                    df.add(eq2_t);
                    df.add(eq3_t);

                    Result result = nl.newtonsMethod(0.5, eps, functions, df);
                    if (result.isValid()) {
                        System.out.println("Решение системы методом Ньютона: ");
                        for (int i = 0; i < result.getAns().length; i++)
                            System.out.println("x" + (i + 1) + " = " + myRound(result.getAns()[i], eps));
                        System.out.println("Количество итераций метода Ньютона: " + result.getIters());
                    } else System.out.println("Система не может быть решена методом Ньютона.");
                    return new Solve(arr_t, new Pair<>(functions, df));
                default:
                    return new Solve(new DoubleUnaryOperator[2], new Pair<>(functions, df));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return new Solve(new DoubleUnaryOperator[2], new Pair<>(functions, df));
        }
    }

    private Pair<DoubleUnaryOperator, String> readEquation() {
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
                    String f1_s = thr_deg[0] + "x^3 + " + thr_deg[1] + "x^2 + " + thr_deg[2] + "x + " + thr_deg[3];
                    return new Pair<>(f1, f1_s);
                case 2:
                    double[] trig = readCoefs(2);
                    DoubleUnaryOperator f2 = x -> trig[0] * Math.sin(x) + trig[1] * Math.cos(x);
                    String f2_s = trig[0] + "*sin(x) + " + trig[1] + "*cos(x)";
                    return new Pair<>(f2, f2_s);
                case 3:
                    double[] sec_deg = readCoefs(3);
                    DoubleUnaryOperator f3 = x -> sec_deg[0] * x * x + sec_deg[1] * x + sec_deg[2];
                    String f3_s = sec_deg[0] + "x^2 + " + sec_deg[1] + "x + " + sec_deg[2];
                    return new Pair<>(f3, f3_s);
                case 4:
                    double[] sin_with_sq = readCoefs(1);
                    DoubleUnaryOperator f4 = x -> Math.sin(x) + sin_with_sq[0] * x * x;
                    String f4_s = "sin(x) + " + sin_with_sq[0] + "*x^2";
                    return new Pair<>(f4, f4_s);
                case 5:
                    double[] cos_with_thr = readCoefs(1);
                    DoubleUnaryOperator f5 = x -> Math.cos(x) + cos_with_thr[0] * x * x * x;
                    String f5_s = "cos(x) + " + cos_with_thr[0] + "*x^3";
                    return new Pair<>(f5, f5_s);
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
        System.out.println("Последовательно введите " + num + " коэффициентов(а):");
        try {
            for (int i = 0; i < num; i++)
                arr[i] = Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Enter a number");
            readCoefs(num);
        }
        return arr;
    }
    public Double myRound(double x, double eps) {
        if (eps < 1)
            return Precision.round(x - x % eps, BigDecimal.valueOf(eps).scale());
        else
            return x - x % eps;
    }
}
