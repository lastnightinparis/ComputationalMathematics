package s285600.computationalmath.gauss_seidel;

import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

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
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealMatrix inv = solver.getInverse();
        RealMatrix d = MatrixUtils.createRealMatrix(matrix.getRowDimension(), matrix.getColumnDimension());
        d = d.scalarAdd(1);
        for (int i = 0; i < d.getColumnDimension(); i++) {
            d.setEntry(i, i, d.getColumnDimension());
        }
        RealMatrix b = d.multiply(inv);
        f = b.multiply(f);
        transform(d, f);
    }

    public boolean checkNorm(RealMatrix matrix) {
        return matrix.getNorm() < 1;
    }

    public void transform(RealMatrix matrix, RealMatrix f) {
        RealMatrix C = MatrixUtils.createRealMatrix(matrix.getRowDimension(), matrix.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++){

        }
        System.out.println(matrix.toString());
        System.out.println(C.toString());
    }
}
