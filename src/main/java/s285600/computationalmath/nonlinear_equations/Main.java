package s285600.computationalmath.nonlinear_equations;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import s285600.computationalmath.nonlinear_equations.utils.ConsoleUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.function.DoubleUnaryOperator;

/**
 * @author Kir
 * Created on 13.03.2021
 */

public class Main {
    public static void main(String[] args) {
        ConsoleUtils cs = new ConsoleUtils(new BufferedReader(new InputStreamReader(System.in)), new OutputStreamWriter(System.out));
        NonLinearEquationsCompute nlec = new NonLinearEquationsCompute();
        DoubleUnaryOperator f = cs.read();
        nlec.bisections(f, 2, 100,  0.1);
        nlec.chords(f, 2, 100, 0.1);
    }
}
