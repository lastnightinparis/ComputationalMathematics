package s285600.computationalmath.gauss_seidel;

import java.io.*;

/**
 * @author Kir
 * Created on 10.02.2021
 */

public class InputReader {

    public void read() throws IOException {
        System.out.println("Введите название файла (для ввода с консоли нажмите Enter):");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();

    }

    private boolean validate(String s) {
        return true;
    }

    // src/s285600/computationalmath/gauss_seidel/input.txt
    private void readFile(String filename) {
        try (FileReader fr = new FileReader(filename)) {
            System.out.println(fr.read());
        } catch (IOException e) {
            //TODO handle exception

        }

    }

    private void readConsole(BufferedReader br) {
    }
}

