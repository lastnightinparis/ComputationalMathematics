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
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int n = Integer.parseInt(br.readLine());
            double[][] matrix = new double[n][n];
            for (int i = 0; i < n; i++) {
                matrix[i] = readArray(br);
            }
            double[] free = Arrays.stream(br.readLine().split(" ")).mapToDouble(Double::parseDouble).toArray();
            double[][] j = new double[free.length][1];
            for (int i = 0; i < free.length; i++)
                j[i][0] = free[i];
            double eps = Double.parseDouble(br.readLine());
            new GaussZeidel().solve(matrix, j, eps);
        } catch (NumberFormatException | IOException | NullPointerException | NegativeArraySizeException e) {
            System.out.println("Произошла ошибка при чтении файла.");
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

    private void readConsole(Scanner in) {
        System.out.println("Введите размерность матрицы n x n: ");
        try {
            int n = in.nextInt();
            in.nextLine();
            System.out.println("Коэффициенты матрицы будут сгенерированы случайно? (y/n)");
            String s = in.nextLine();
            if (s.equals("y")) {
                System.out.println("Введите погрешность:");
                double eps = in.nextDouble();
                generateCoefs(n, eps);
            } else {
                if (!s.equals("n")) throw new IOException();
                double[][] matrix = new double[n][n];
                double[][] free = new double[n][1];
                System.out.println("Построчно введите коэффициенты матрицы:");
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        matrix[i][j] = in.nextDouble();
                    }
                }
                System.out.println("Введите столбец свободных членов:");
                for (int i = 0; i < n; i++) {
                    free[i][0] = in.nextDouble();
                }
                System.out.println("Введите погрешность:");
                double eps = in.nextDouble();
                new GaussZeidel().solve(matrix, free, eps);
            }
        } catch (InputMismatchException | IOException e) {
            System.out.println("Произошла ошибка. Повторите ввод заново. (Для разделения разрядов используется запятая)");
            in.nextLine();
            readConsole(in);
        }
    }
    public void generateCoefs(int n, double eps){
        double[][] matrix = new double[n][n];
        int max = 1000;
        int min = 1;
        double[][] free = new double[n][1];
        for (int i = 0; i < n; i++){
            free[i][0] = Math.random() * ((max - min) + 1) + min;
            for (int j = 0; j < n; j++){
                if (i != j)
                    matrix[i][j] = Math.random() * ((max - min) + 1) + min;
            }
        }
        for (int i = 0; i < n; i++){
            matrix[i][i] = Arrays.stream(matrix[i]).sum() + Math.random() * ((max - min) + 1) + min;
        }
        System.out.println("Сгенерированная матрица:" + Arrays.deepToString(matrix));
        System.out.println("Свободные коэффициенты:" + Arrays.deepToString(free));
        new GaussZeidel().solve(matrix, free, eps);
    }
}

