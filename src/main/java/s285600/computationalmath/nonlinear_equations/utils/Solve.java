package s285600.computationalmath.nonlinear_equations.utils;

import javafx.util.Pair;

import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

/**
 * @author Kir
 * Created on 28.03.2021
 */

public class Solve {
    private DoubleUnaryOperator[] arr;
    private Pair<List<Function<double[], Double>>, List<List<Function<double[], Double>>>> pair;
    private double[] sol;
    private String[] decr;

    public Solve(DoubleUnaryOperator[] arr, Pair<List<Function<double[], Double>>, List<List<Function<double[], Double>>>> pair) {
        this.arr = arr;
        this.pair = pair;
    }

    public double[] getSol() {
        return sol;
    }

    public void setSol(double[] sol) {
        this.sol = sol;
    }

    public DoubleUnaryOperator[] getArr() {
        return arr;
    }

    public void setArr(DoubleUnaryOperator[] arr) {
        this.arr = arr;
    }

    public Pair<List<Function<double[], Double>>, List<List<Function<double[], Double>>>> getPair() {
        return pair;
    }

    public void setPair(Pair<List<Function<double[], Double>>, List<List<Function<double[], Double>>>> pair) {
        this.pair = pair;
    }

    public String[] getDecr() {
        return decr;
    }

    public void setDecr(String[] decr) {
        this.decr = decr;
    }

    public Solve(DoubleUnaryOperator[] arr, Pair<List<Function<double[], Double>>, List<List<Function<double[], Double>>>> pair, String[] decr) {
        this.arr = arr;
        this.pair = pair;
        this.decr = decr;
    }
}
