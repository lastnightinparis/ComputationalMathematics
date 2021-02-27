package s285600.computationalmath.gauss_seidel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Kir
 * Created on 10.02.2021
 */

public class InputReader {

    public void read() throws IOException {
        System.out.println("Введите название файла (для ввода с консоли нажмите Enter):");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        if (input.equals("")) {
            readConsole(in);
        } else
            readFile(input);
    }

    // src/s285600/computationalmath/gauss_seidel/input.txt
    private void readFile(String filename) {
        filename = "src/s285600/computationalmath/gauss_seidel/input.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int n = Integer.parseInt(br.readLine());
            double[][] matrix = new double[n][n];
            for (int i = 0; i < n; i++) {
                matrix[i] = readArray(br);
            }
            double[] free = Arrays.stream(br.readLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
            double [][] j = new double[free.length][1];
            for (int i = 0; i < free.length; i++)
                j[i][0] = free[i];
            double eps = Double.parseDouble(br.readLine());
            new GaussZeidel().solve(matrix, j, eps);
            //System.out.println(Arrays.deepToString(matrix));
        } catch (IOException e) {
            //TODO handle exception
        }

    }

    private double[] readArray(BufferedReader br) throws IOException {
        try {
            return Arrays.stream(br.readLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
        } catch (IOException | NumberFormatException e) {
            System.out.println("Некорректный файл.");
            e.printStackTrace();
            read();
            return new double[0];
        }
    }

    public boolean validate() {
        return false;
    }

    private double[][] readConsole(Scanner in) {
        System.out.println("Введите два числа (размерность матрицы m x n): ");
        try {
            int m = in.nextInt();
            int n = in.nextInt();
            double[][] a = new double[m][n + 1];
            System.out.println("Построчно введите коэффициенты матрицы:");
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    a[i][j] = in.nextInt();
                }
            }
            System.out.println("Введите столбец свободных членов:");
            for (int i = 0; i < m; i++) {
                a[i][n] = in.nextInt();
            }
            System.out.println(Arrays.deepToString(a));
            return a;
        } catch (InputMismatchException e) {
            System.out.println("Произошла ошибка. Повторите ввод заново.");
            in.nextLine();
            readConsole(in);
            return new double[0][0];
            //TODO catch
        }
    }
}

