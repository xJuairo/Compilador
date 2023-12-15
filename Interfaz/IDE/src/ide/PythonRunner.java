/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ide;

/**
 *
 * @author xjlop
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class PythonRunner {

    public static String ejecutarScriptPython(String rutaScript) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python", rutaScript);
        pb.redirectErrorStream(true);
        Process proceso = pb.start();

        // Capturar la salida del proceso
        try (InputStream inputStream = proceso.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {

            StringBuilder salida = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                salida.append(linea).append("\n");
            }

            // Esperar a que el proceso termine
            try {
                proceso.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return salida.toString();
        }
    }
}
