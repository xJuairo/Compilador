/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ide;

import java.awt.Dimension;

/**
 *
 * @author xjlop
 */
public class IDE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ventana ventana = new Ventana();
        ventana.setResizable(false);
        ventana.setPreferredSize(new Dimension(900,600));
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
    
}
