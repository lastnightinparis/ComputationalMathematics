package s285600.computationalmath.nonlinear_equations;

import java.util.function.DoubleUnaryOperator;

/**
 * @author Kir
 * Created on 13.03.2021
 */

public class NonLinearEquationsCompute {
    public void bisections(DoubleUnaryOperator f, double l, double r, double eps) {
        int iters = 100_000_000;
        double x = (l + r) * .5;
        for (int i = 0; i < iters; i++) {
            if (Math.abs(r - l) <= eps || Math.abs(f.applyAsDouble(x)) <= eps) {
                System.out.println("ans = " + x);
                break;
            }
            if (f.applyAsDouble(l) * f.applyAsDouble(x) > 0)
                l = x;
            else
                r = x;
            x = (l + r) * .5;
        }
    }

    public void chords(DoubleUnaryOperator f, double l, double r, double eps) {
        int iters = 100_000_000;
        double x = l - (r - l) / (f.applyAsDouble(r) - f.applyAsDouble(l)) * f.applyAsDouble(l);
        double prev = x + 2 * eps;
        for (int i = 0; i < iters; i++) {
            if (Math.abs(prev - x) <= eps || Math.abs(f.applyAsDouble(x)) <= eps) {
                System.out.println("ans = " + x);
                break;
            }
            prev = x;
            if (f.applyAsDouble(l) * f.applyAsDouble(x) > 0)
                l = x;
            else
                r = x;
            x = l - (r - l) / (f.applyAsDouble(r) - f.applyAsDouble(l)) * f.applyAsDouble(l);
        }
    }

    public void newTonsMethod() {

    }
}
