package s285600.computationalmath.gauss_seidel;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kir
 * Created on 16.02.2021
 */

public class GaussZeidel {
    private final Integer MAX_ITERATIONS = 1000;

    public double[] solve(double[][] m, double[][] f, double eps) {
//        if (eps <= 0) {
//            System.out.println("Неверная погрешность.");
//            try {
//                new InputReader().read();
//            } catch (IOException e) {
//                System.out.println("Произошла непоправимая ошибка, кошмар!");
//                System.exit(0);
//            }
//        }
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        RealMatrix free_column = MatrixUtils.createRealMatrix(f);
        return normalizeMatrix(matrix, free_column, eps);
    }

    public double[] normalizeMatrix(RealMatrix matrix, RealMatrix free_column, double eps) {
        if (diagonalPredominance(matrix)) {
            transform(matrix, free_column);
            return solution(matrix, free_column, eps);
        } else {
            RealMatrix transformed = modifyToDiagonalPred(matrix, free_column);
            if (diagonalPredominance(transformed)) {
                transform(transformed, free_column);
                return solution(transformed, free_column, eps);
            } else {
                return new double[matrix.getRowDimension()];
            }
        }
    }

    //src/s285600/computationalmath/gauss_seidel/input.txt
    public RealMatrix modifyToDiagonalPred(RealMatrix m, RealMatrix f) {
        RealMatrix transformed = MatrixUtils.createRealMatrix(m.getRowDimension(), m.getColumnDimension());
        RealMatrix g = MatrixUtils.createRealMatrix(f.getRowDimension(), f.getColumnDimension());
        for (int i = 0; i < m.getRowDimension(); i++) {
            Double[] column = ArrayUtils.toObject(m.getColumn(i));
            List<Double> doubles = Arrays.asList(column);
            int index = doubles.indexOf(Collections.max(doubles));
            transformed.setRow(i, m.getRow(index));
            g.setRow(i, f.getRow(index));
        }
        f.setColumnMatrix(0, g);
        return transformed;
    }

    public void transform(RealMatrix m, RealMatrix f) {
        for (int i = 0; i < m.getRowDimension(); i++) {
            Double[] row = ArrayUtils.toObject(m.getRow(i));
            List<Double> list = Arrays.asList(row);
            Double xi = list.get(i);
            List<Double> collect = list.stream().map(v -> -v / xi).collect(Collectors.toList());
            collect.set(i, -collect.get(i) - 1);
            m.setRow(i, collect.stream().mapToDouble(Double::doubleValue).toArray());
            double[] f_row = f.getRow(i);
            for (int j = 0; j < f_row.length; j++)
                f_row[j] = f_row[j] / xi;
            f.setRow(i, f_row);
        }
    }

    public boolean diagonalPredominance(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double[] row = matrix.getRow(i);
            double sum = Math.abs(Arrays.stream(row).sum()) - Math.abs(row[i]);
            if (Math.abs(row[i]) <= Math.abs(sum)) {
                return false;
            }
        }
        return true;
    }

    public double[] solution(RealMatrix m, RealMatrix free_column, double eps) {
        boolean stop = false;
        int iterations = 0;
        double[] newVarsValues = new double[m.getRowDimension()];
        double[] oldVarsValues = new double[m.getRowDimension()];
        double[] errors = new double[m.getRowDimension()];
        for (int i = 0; i < m.getColumnDimension(); i++) {
            newVarsValues[i] = free_column.getRow(i)[0];
            oldVarsValues[i] = free_column.getRow(i)[0];
        }
        while (!stop) {
            if (iterations > MAX_ITERATIONS)
                break;
            iterations++;
            for (int i = 0; i < m.getRowDimension(); i++) {
                double variable_value = 0;
                for (int j = 0; j < m.getColumnDimension(); j++) {
                    if (i <= j) {
                        variable_value += m.getRow(i)[j] * oldVarsValues[j];
                    } else {
                        variable_value += m.getRow(i)[j] * newVarsValues[j];
                    }
                }
                variable_value += free_column.getRow(i)[0];
                newVarsValues[i] = variable_value;
            }
            for (int i = 0; i < m.getRowDimension(); i++) {
                errors[i] = Math.abs(newVarsValues[i] - oldVarsValues[i]);
                if (errors[i] < eps) {
                    stop = true;
                } else {
                    stop = false;
                    break;
                }
            }
            for (int i = 0; i < m.getRowDimension(); i++) {
                oldVarsValues[i] = newVarsValues[i];
            }
        }
        //print(newVarsValues, errors, iterations, eps);
        return newVarsValues;
    }

    public void print(double[] newVars, double[] errors, int iterations, double eps) {
        if (iterations > MAX_ITERATIONS)
            System.out.println("Метод Гаусса-Зейделя не может найти решение для данной системы за допустимое количество итераций");
        else {
            System.out.println("Количество итераций: " + iterations);
            System.out.println("Столбец неизвестных:");
            for (int i = 0; i < newVars.length; i++)
                System.out.println("x" + (i + 1) + " " + myRound(newVars[i], eps));
            System.out.println("Заданная погрешность: " + eps);
            System.out.println("Столбец погрешностей:");
            for (int i = 0; i < errors.length; i++)
                System.out.println("delta x" + (i + 1) + " = " + myRound(errors[i], eps / 10));
        }
    }

    public Double myRound(double x, double eps) {
        if (eps < 1)
            return Precision.round(x - x % eps, BigDecimal.valueOf(eps).scale());
        else
            return x - x % eps;
    }
}
