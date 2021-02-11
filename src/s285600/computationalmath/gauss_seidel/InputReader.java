package s285600.computationalmath.gauss_seidel;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    private boolean validate(String s) {
        return true;
    }

    // src/s285600/computationalmath/gauss_seidel/input.txt
    private void readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<Integer> dim = readList(br);
            List<List<Integer>> matrix = new ArrayList<>();
            for (int i = 0; i < dim.get(0); i++) {
                try {
                    matrix.add(readList(br));
                } catch (NumberFormatException e) {
                    break;
                }
            }
            System.out.println(matrix);
        } catch (IOException e) {
            //TODO handle exception
        }

    }

    public boolean validate() {
        return false;
    }

    private List<Integer> readList(BufferedReader br) throws IOException {
        return Arrays.stream(br.readLine().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private int[][] readConsole(Scanner in) {
        System.out.println("Введите два числа (размерность матрицы m x n): ");
        try {
            int m = in.nextInt();
            int n = in.nextInt();
            int[][] a = new int[m][n + 1];
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
            return new int[0][0];
            //TODO catch
        }
    }
}

