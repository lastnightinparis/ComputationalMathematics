package s285600.computationalmath.gauss_seidel;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Kir
 * Created on 16.02.2021
 */

public class GaussZeidel {
    public void solve(double[][] m, double[][] f, double eps) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        RealMatrix free = MatrixUtils.createRealMatrix(f);
        normalizeMatrix(matrix, free, eps);
    }

    public void normalizeMatrix(RealMatrix matrix, RealMatrix free_column, double eps) {
        if (diagonalPredominance(matrix)) {
            System.out.println("Матрица обладает диагональным преобладанием");
            transform(matrix, free_column);
            solution(matrix, free_column, eps);
        } else {
            System.out.println("Давайте-ка приведём матрицу к норм виду");
            RealMatrix transformed = modifyToDiagonalPred(matrix, free_column);
            if (diagonalPredominance(transformed)) {
                System.out.println("Матрица приведена к виду диагонального преобладания");
                transform(transformed, free_column);
                solution(transformed, free_column, eps);
            } else {
                System.out.println("Матрица не может быть приведена к виду диагонального преобладания");
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
        System.out.println(transformed);
        return transformed;
    }

    public void transform(RealMatrix m, RealMatrix f) {
        for (int i = 0; i < m.getRowDimension(); i++) {
            Double[] row = ArrayUtils.toObject(m.getRow(i));
            List<Double> list = Arrays.asList(row);
            Double y = list.get(i);
            List<Double> collect = list.stream().map(x -> -x / y).collect(Collectors.toList());
            collect.set(i, -collect.get(i) - 1);
            m.setRow(i, collect.stream().mapToDouble(Double::doubleValue).toArray());
            double[] f_row = f.getRow(i);
            for (int j = 0; j < f_row.length; j++)
                f_row[j] = f_row[j] / y;
            f.setRow(i, f_row);
        }
    }

    public boolean diagonalPredominance(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double[] row = matrix.getRow(i);
            double sum = Math.abs(Arrays.stream(row).sum()) - Math.abs(row[i]);
            if (Math.abs(row[i]) < Math.abs(sum)) {
                return false;
            }
        }
        return true;
    }

    public void solution(RealMatrix m, RealMatrix f, double eps) {
        int iterations = 0;
        double[] newVars = new double[m.getRowDimension()];
        double[] oldVars = new double[m.getRowDimension()];
        double[] errors = new double[m.getRowDimension()];
        //Начальное приближение
        for (int i = 0; i < m.getColumnDimension(); i++) {
            newVars[i] = f.getRow(i)[0];
            oldVars[i] = f.getRow(i)[0];
        }
        System.out.println(Arrays.toString(newVars));
        while (true) {
            iterations++;
            boolean stop = false;
            for (int i = 0; i < m.getRowDimension(); i++) {
                double variable_value = 0;
                for (int j = 0; j < m.getColumnDimension(); j++) {
                    if (i <= j) {
                        variable_value += m.getRow(i)[j] * oldVars[j];
                    } else {
                        variable_value += m.getRow(i)[j] * newVars[j];
                    }
                }
                variable_value += f.getRow(i)[0];
                newVars[i] = variable_value;
            }
            for (int i = 0; i < m.getRowDimension(); i++) {
                errors[i] = Math.abs(newVars[i] - oldVars[i]);
                if (errors[i] < eps) {
                    System.out.println(newVars[i] - oldVars[i] + " < eps");
                    stop = true;
                    break;
                } else {
                    oldVars[i] = newVars[i];
                }
            }
            if (stop) {
                break;
            }
        }
        print(newVars, errors, iterations);
    }

    public void print(double[] newVars, double[] errors, int iterations) {
        System.out.println("Количество итераций: " + iterations);
        System.out.println("Столбец неизвестных:");
        for (int i = 0; i < newVars.length; i++)
            System.out.println("x" + (i + 1) + " = " + newVars[i]);
        System.out.println("Столбец погрешностей:");
        for (int i = 0; i < errors.length; i++)
            System.out.println("x" + (i + 1) + " = " + errors[i]);
    }
}
