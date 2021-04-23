package s285600.computationalmath.nonlinear_equations;

import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.linear.LinearSystemSolver;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;
import s285600.computationalmath.gauss_seidel.GaussZeidel;
import s285600.computationalmath.nonlinear_equations.utils.Result;

import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

/**
 * @author Kir
 * Created on 13.03.2021
 */

public class NonLinearEquationsCompute {
    private GaussZeidel gz = new GaussZeidel();

    public Result bisections(DoubleUnaryOperator f, double l, double r, double eps) {
        Result result = new Result();
        if (f.applyAsDouble(l) * f.applyAsDouble(r) > 0) {
            double rInit = r;
            r = l;
            while (f.applyAsDouble(l) * f.applyAsDouble(r) > 0) {
                r += 0.5;
                if (r > rInit) {
                    result.setValid(false);
                    result.setMessage("На данном отрезке нет корней.");
                    return result;
                }
            }
        }
        int iters = 100_000_000;
        double x = (l + r) * .5;
        for (int i = 0; i < iters; i++) {
            if (Math.abs(r - l) <= eps || Math.abs(f.applyAsDouble(x)) <= eps) {
                result.setValid(true);
                result.setIters(i + 1);
                result.setX(x);
                return result;
            }
            if (f.applyAsDouble(l) * f.applyAsDouble(x) > 0)
                l = x;
            else
                r = x;
            x = (l + r) * .5;
        }
        result.setValid(false);
        result.setMessage("Метод деления пополам не смог найти решение за допустимое число итераций.");
        return result;
    }

    public Result chords(DoubleUnaryOperator f, double l, double r, double eps) {
        Result result = new Result();
        if (f.applyAsDouble(l) * f.applyAsDouble(r) > 0) {
            double rInit = r;
            r = l;
            while (f.applyAsDouble(l) * f.applyAsDouble(r) > 0) {
                r += 0.5;
                if (r > rInit) {
                    result.setValid(false);
                    result.setMessage("На данном отрезке нет корней.");
                    return result;
                }
            }
        }
        if (f.applyAsDouble(l) * f.applyAsDouble(r) == 0) {
            result.setValid(true);
            result.setIters(1);
            result.setX(f.applyAsDouble(l) == 0 ? l : r);
            return result;
        }
        int iters = 100_000_000;
        while (f.applyAsDouble(l) * f.applyAsDouble(r) < 0)
            l += 0.5;
        l -= 0.5;
        while (f.applyAsDouble(l) * f.applyAsDouble(r) < 0)
            r -= 0.5;
        r += 0.5;
        double x = l - (r - l) / (f.applyAsDouble(r) - f.applyAsDouble(l)) * f.applyAsDouble(l);
        double prev = x + 10 * eps;
        for (int i = 0; i < iters; i++) {
            if (Math.abs(prev - x) <= eps || Math.abs(f.applyAsDouble(x)) <= eps) {
                result.setValid(true);
                result.setX(x);
                result.setIters(i + 1);
                return result;
            }
            prev = x;
            if (f.applyAsDouble(l) * f.applyAsDouble(x) > 0)
                l = x;
            else
                r = x;
            x = l - (r - l) / (f.applyAsDouble(r) - f.applyAsDouble(l)) * f.applyAsDouble(l);
        }
        result.setValid(false);
        result.setMessage("Метод хорд не смог найти решение за допустимое число итераций.");
        return result;
    }

    public Result newtonsMethod(double initial_approx, double eps, List<Function<double[],
            Double>> functions, List<List<Function<double[], Double>>> functionsDerivatives) {
        Result result = new Result();
        double[] x = new double[functions.size()];
        double[] fx = new double[functions.size()];
        double[] prev;
        double[][] jacobian = new double[functions.size()][functions.size()];
        int iterations = 0;

        for (int i = 0; i < functions.size(); i++) {
            x[i] = initial_approx;
        }

        while (iterations < 100_000_000) {
            iterations++;
            prev = x.clone();
            for (int i = 0; i < functions.size(); i++) {
                fx[i] = functions.get(i).apply(x);
                for (int j = 0; j < functions.size(); j++) {
                    jacobian[i][j] = functionsDerivatives.get(i).get(j).apply(x);
                }
            }
            Matrix eqSys = new Basic2DMatrix(jacobian);
            Vector free_vector = new BasicVector(fx);
            LinearSystemSolver solver = eqSys.withSolver(LinearAlgebra.SolverFactory.GAUSSIAN);
            Vector cur_solve = solver.solve(free_vector);
            double[] dx = new double[cur_solve.length()];
            for (int i = 0; i < dx.length; i++)
                dx[i] = cur_solve.get(i);
            for (int i = 0; i < dx.length; i++) {
                x[i] = x[i] - dx[i];
            }

            boolean eps_flag = true;
            for (int i = 0; i < functions.size(); i++) {
                if (Math.abs(prev[i] - x[i]) > eps) {
                    eps_flag = false;
                    break;
                }
            }
            if (eps_flag) {
                result.setAns(x);
                result.setValid(true);
                result.setIters(iterations);
                break;
            }
        }
        if (iterations > 100_000_000) {
            result.setValid(false);
            result.setMessage("Метод Ньютона не смог найти решение за допустимое число итераций.");
        }
        return result;
    }
}
