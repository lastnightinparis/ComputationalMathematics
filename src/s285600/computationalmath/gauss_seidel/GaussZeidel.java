package s285600.computationalmath.gauss_seidel;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kir
 * Created on 16.02.2021
 */

public class GaussZeidel {
    public void solve(double[][] m, double[][] f) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        RealMatrix free = MatrixUtils.createRealMatrix(f);
        normalizeMatrix(matrix, free);
    }

    public void normalizeMatrix(RealMatrix matrix, RealMatrix f) {
        if (diagonalPredominance(matrix))
            System.out.println("Матрица обладает диагональным преобладанием");
        else {
            System.out.println("Давайте-ка приведём матрицу к норм виду");
            RealMatrix transformed = modifyToDiagonalPred(matrix);
            System.out.println(transformed);
            if (diagonalPredominance(transformed)) {
                System.out.println("Матрица преведена к виду диагонального преобладания");
                transform(transformed);
                solution(transformed, f, 0.1);
            } else {
                System.out.println("Матрица не может быть преведена к виду диагонального преобладания");
            }
        }
    }

    //src/s285600/computationalmath/gauss_seidel/input.txt
    public RealMatrix modifyToDiagonalPred(RealMatrix m) {
        RealMatrix transformed = MatrixUtils.createRealMatrix(m.getRowDimension(), m.getColumnDimension());
        for (int i = 0; i < m.getRowDimension(); i++) {
            Double[] column = ArrayUtils.toObject(m.getColumn(i));
            List<Double> doubles = Arrays.asList(column);
            int index = doubles.indexOf(Collections.max(doubles));
            transformed.setRow(i, m.getRow(index));
        }
        return transformed;
    }

    public void transform(RealMatrix m) {
        for (int i = 0; i < m.getRowDimension(); i++) {
            Double[] row = ArrayUtils.toObject(m.getRow(i));
            List<Double> list = Arrays.asList(row);
            Double y = list.get(i);
            List<Double> collect = list.stream().map(x -> -x / y).collect(Collectors.toList());
            collect.set(i,  - collect.get(i) - 1);
            m.setRow(i, collect.stream().mapToDouble(Double::doubleValue).toArray());
        }
    }

    public boolean checkNorm(RealMatrix matrix) {
        return matrix.getNorm() < 1;
    }

    public boolean diagonalPredominance(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double[] row = matrix.getRow(i);
            double sum = Arrays.stream(row).sum() - row[i];
            if (row[i] <= sum)
                return false;
        }
        return true;
    }
    public void solution(RealMatrix m, RealMatrix f, double eps) {
        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < f.getRowDimension(); i++) {
            map.put(i, f.getRow(i)[0]);
        }
        System.out.println(map.toString());
    }
}
