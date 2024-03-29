/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 *
 * @author xjlop
 */
public class Ventana extends javax.swing.JFrame {

    /**
     * Creates new form Ventana
     */        
    JPanel jPanel1 = new JPanel();
    EditorScrollPane editor = new EditorScrollPane(400, 400,this);
    Directorio dir;
    public Ventana() {
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        dir = new Directorio();
        /*PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec(
        "import sys\n"
        +"sys.argv = ['Que onda chavales int x = 0;']");
        interpreter.execfile("C:\\Users\\xjlop\\Compilador\\AnalizadorLexico\\prueba.py");
        /*PythonInterpreter.initialize(System.getProperties(), System.getProperties(), arguments);
        org.python.util.PythonInterpreter python = new org.python.util.PythonInterpreter();
        StringWriter out = new StringWriter();
        python.setOut(out);
        python.execfile("C:\\Users\\xjlop\\Compilador\\AnalizadorLexico\\prueba.py");
        String outputStr = out.toString();
        System.out.println(outputStr);
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        pythonInterpreter.execfile("C:\\Users\\xjlop\\Compilador\\AnalizadorLexico\\prueba.py");*/
        setTitle("Compilador");
        JMenu menu = new JMenu();
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item = new JMenuItem("Nuevo");
        JMenuItem item2 = new JMenuItem("Abrir");
        JMenuItem item3 = new JMenuItem("Guardar");
        JMenuItem item4 = new JMenuItem("Guardar Como");
        JTextArea errores = new JTextArea();
        JTextArea resultados = new JTextArea();

        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(editor, BorderLayout.CENTER);
        this.add(jPanel1);
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                editorKeyReleased(evt);
                
            }
        });

        //editor.colors();
        initComponents();
        /*ResultadosCode.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e){
                int dot = e.getDot();
                int mark = e.getMark();
                //Solo premitir el movimiento del curso si esta en la
                if(!isCursorAtLastLine(ResultadosCode, dot)){
                    ResultadosCode.setCaretPosition(ResultadosCode.getDocument().getLength());
                }
                if(dot != mark){
                    int start = Math.min(dot, mark);
                    int end = Math.max(dot,mark);
                    String selectedText = ResultadosCode.getText();
                    
                }
            }
        });*/
        
        Font consolasFont = new Font("Consolas", Font.PLAIN, 24);
        LexicoCode.setFont(consolasFont);
        SintacticoCode.setFont(consolasFont);
        SemanticoCode.setFont(consolasFont);
        JSplitPane splitPanev = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPanev.setTopComponent(splitPane);
        splitPanev.setBottomComponent(jTabbedPane1);
        splitPane.setLeftComponent(jTabbedPane3);
        splitPane.setRightComponent(jPanel1);
        jTabbedPane1.setPreferredSize(new Dimension(400, 50)); // Ajusta el tamaño según tus necesidades
        this.add(splitPanev, BorderLayout.CENTER);
        jTabbedPane3.setPreferredSize(new Dimension(800, 200)); // Ajusta el tamaño según tus necesidades
        jScrollPane4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane5.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane6.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane7.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        TabladeSimbolos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    }
        public static JSONObject leerArbolJson(String rutaArchivo) {
            JSONParser parser = new JSONParser();

            try {
                String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
                Object obj = parser.parse(contenido);
                return (JSONObject) obj;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        public static void manejarInputStatements(JSONObject nodo, String archivoEntrada) throws IOException {
            // Verifica si el nodo es un InputStatement
            if ("InputStatement".equals(nodo.get("value"))) {
                JSONObject idListNode = (JSONObject) ((JSONArray) nodo.get("children")).get(0); // Obtener el nodo idList
                JSONArray idListChildren = (JSONArray) idListNode.get("children");
                for (Object o : idListChildren) {
                    JSONObject child = (JSONObject) o;
                    String nombreVariable = (String) child.get("value");
                    String entradaUsuario = JOptionPane.showInputDialog("Ingrese valor para " + nombreVariable + ":");
                    escribirEnArchivo(archivoEntrada, nombreVariable + " " + entradaUsuario);
                }
                //

            }

            // Procesar nodos hijos (si existen)
            if (nodo.containsKey("children")) {
                JSONArray children = (JSONArray) nodo.get("children");
                for (Object o : children) {
                    manejarInputStatements((JSONObject) o, archivoEntrada);
                }
            }
        }

        private static void escribirEnArchivo(String archivoEntrada, String data) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoEntrada, true))) {
                writer.write(data);
                writer.newLine();
            }
        }
        
        private static void limpiarArchivo(String archivo) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write("");  // Escribe una cadena vacía para limpiar el archivo
            }   
        }
        
        public void Salida(){
            String rutaactual = System.getProperty("user.dir");
            System.out.println("Ruta actual: " + rutaactual);
            Path rutaNueva = Paths.get(rutaactual).getParent().getParent();
            try {
                ResultadosCode.setText("");
                String rutaActual = System.getProperty("user.dir");
                System.out.println("Ruta actual: " + rutaActual);
                Path rutaArchivo = Paths.get(rutaActual.toString()).resolve("output.txt");
                System.out.println(rutaArchivo);
                // Ruta al archivo de texto en tu proyecto
                Font consolasFont = new Font("Consolas", Font.PLAIN, 24);
                ResultadosCode.setFont(consolasFont);
                // Abre el archivo y crea un lector (BufferedReader) para leer su contenido
                List<String> lineas = Files.readAllLines(rutaArchivo);

                // Iterar a través de las líneas y mostrar su contenido
                for (String linea : lineas) {
                    System.out.println(linea);
                    ResultadosCode.append(linea+ "\n");
                }
                    //if (!ErroresCode.getText().trim().isEmpty()) {
                        //ResultadosCode.setText("");

                    //}
                // Establece el contenido del JTextArea con el contenido del archivo

            } catch (Exception e) {
                e.printStackTrace();  // Manejo de excepciones, puedes personalizarlo según tus necesidades.
            }

        }
    
    public void CodigoIntermedio(){

        try {
            String rutaActual = System.getProperty("user.dir");
            File directorioActualFile = new File(rutaActual);
            File directorioPadre = directorioActualFile.getParentFile().getParentFile();
            File analizadorLexicoDirectorio = new File(directorioPadre, "AnalizadorLexico");
            String rutactual = System.getProperty("user.dir");
            Path rutaarchivo = Paths.get(rutactual.toString()).resolve("arbol_sintactico.json");
            Path rutaentrada = Paths.get(rutaActual.toString()).resolve("archivo_entrada.txt");
            limpiarArchivo(rutaentrada.toString());
            JSONObject arbol = leerArbolJson(rutaarchivo.toString());
            manejarInputStatements(arbol, rutaentrada.toString());
            String rutaactual = System.getProperty("user.dir");
            System.out.println("Ruta actual: " + rutaactual);
            Path rutaNueva = Paths.get(rutaactual).getParent().getParent();
            System.out.println("Ruta nueva: " + rutaNueva.toString());
            Path ruta = Paths.get(rutaNueva.toString(),"AnalizadorLexico");
            System.out.println(ruta);
            Path rutaScript = Paths.get(ruta.toString()).resolve("ejecucion.py");

            try {
                String salidaPython = PythonRunner.ejecutarScriptPython(rutaScript.toString());
                System.out.println(salidaPython);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            IntermedioCode.setText("");

            if (analizadorLexicoDirectorio.exists() && analizadorLexicoDirectorio.isDirectory()) {
            // Acceder a la carpeta "AnalizadorLexico"
            System.out.println("Ruta completa de la carpeta AnalizadorLexico: " + analizadorLexicoDirectorio.getAbsolutePath());
            } else {
                System.out.println("La carpeta AnalizadorLexico no existe en la ruta proporcionada.");
            }
            Path rutaArchivo = Paths.get(rutaActual).resolve("codigo_intermedio.txt");


            Font consolasFont = new Font("Consolas", Font.PLAIN, 24);
            IntermedioCode.setFont(consolasFont);

            List<String> lineas = Files.readAllLines(rutaArchivo);

            for (String linea : lineas) {
                IntermedioCode.append(linea + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Manejo de excepciones, puedes personalizarlo según tus necesidades.
        }
    }
    
    public void SymbolTable(){
        try {
            tabla.setText("");
            String rutaActual = System.getProperty("user.dir");
            System.out.println("Ruta actual: " + rutaActual);
            Path rutaArchivo = Paths.get(rutaActual.toString()).resolve("TablaSimbolos.txt");
            System.out.println(rutaArchivo);
            // Ruta al archivo de texto en tu proyecto
            Font consolasFont = new Font("Consolas", Font.PLAIN, 24);
            tabla.setFont(consolasFont);
            // Abre el archivo y crea un lector (BufferedReader) para leer su contenido
            List<String> lineas = Files.readAllLines(rutaArchivo);

            // Iterar a través de las líneas y mostrar su contenido
            for (String linea : lineas) {
                System.out.println(linea);
                tabla.append(linea+ "\n");
            }
            // Establece el contenido del JTextArea con el contenido del archivo
            
        } catch (Exception e) {
            e.printStackTrace();  // Manejo de excepciones, puedes personalizarlo según tus necesidades.
        }

    }
    
    public void Errores(){
        try {
            ErroresCode.setText("");
            String rutaActual = System.getProperty("user.dir");
            System.out.println("Ruta actual: " + rutaActual);
            Path rutaArchivo = Paths.get(rutaActual.toString()).resolve("ErroresSemantico.txt");
            Path rutaarchivo = Paths.get(rutaActual.toString()).resolve("Sintaxiserrores.txt");
            System.out.println(rutaArchivo);
            // Ruta al archivo de texto en tu proyecto
            Font consolasFont = new Font("Consolas", Font.PLAIN, 24);
            ErroresCode.setFont(consolasFont);
            // Abre el archivo y crea un lector (BufferedReader) para leer su contenido
            List<String> lineas = Files.readAllLines(rutaArchivo);
            List<String> linead = Files.readAllLines(rutaArchivo);

            // Iterar a través de las líneas y mostrar su contenido
            for (String linea : lineas) {
                System.out.println(linea);
                ErroresCode.append(linea+ "\n");
            }
            for (String linea : linead) {
                System.out.println(linea);
                ErroresCode.append(linea+ "\n");
            }
            // Establece el contenido del JTextArea con el contenido del archivo
            
        } catch (Exception e) {
            e.printStackTrace();  // Manejo de excepciones, puedes personalizarlo según tus necesidades.
        }

    }
    
    public void Semantico(){
        String rutaActual = System.getProperty("user.dir");
        System.out.println("Ruta actual: " + rutaActual);
        Path rutaNueva = Paths.get(rutaActual).getParent().getParent();
        System.out.println("Ruta nueva: " + rutaNueva.toString());
        Path ruta = Paths.get(rutaNueva.toString(),"AnalizadorLexico");
        System.out.println(ruta);
        Path rutaScript = Paths.get(ruta.toString()).resolve("Analizador_Semantico.py");
        
        try {
            String salidaPython = PythonRunner.ejecutarScriptPython(rutaScript.toString());
            SemanticoCode.setText(salidaPython);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void Sintactico(){
        String rutaActual = System.getProperty("user.dir");
        System.out.println("Ruta actual: " + rutaActual);
        Path rutaNueva = Paths.get(rutaActual).getParent().getParent();
        System.out.println("Ruta nueva: " + rutaNueva.toString());
        Path ruta = Paths.get(rutaNueva.toString(),"AnalizadorLexico");
        System.out.println(ruta);
        Path rutaScript = Paths.get(ruta.toString()).resolve("analizadorsintactico.py");
        
        try {
            String salidaPython = PythonRunner.ejecutarScriptPython(rutaScript.toString());
            SintacticoCode.setText(salidaPython);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void obtenerRespuesta(String resp, String err) throws IOException{
        File archivo = new File("Tokens.txt");
        FileWriter writer = new FileWriter(archivo);
        LexicoCode.setText(resp);
        writer.write(resp);
        writer.close();
        File archi = new File("Errores.txt");
        FileWriter writ = new FileWriter(archi);
        ErroresCode.setText(err);
        writ.write(err);
        writ.close();
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        CodigoCode = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        ErroresCode = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        ResultadosCode = new javax.swing.JTextArea();
        TabladeSimbolos = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTextArea();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        LexicoCode = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        SintacticoCode = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        SemanticoCode = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        IntermedioCode = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();

        CodigoCode.setColumns(20);
        CodigoCode.setRows(5);
        jScrollPane1.setViewportView(CodigoCode);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        ErroresCode.setColumns(20);
        ErroresCode.setRows(5);
        ErroresCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ErroresCodeKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(ErroresCode);

        jTabbedPane1.addTab("Errores", jScrollPane2);

        ResultadosCode.setColumns(20);
        ResultadosCode.setRows(5);
        jScrollPane3.setViewportView(ResultadosCode);

        jTabbedPane1.addTab("Resultados", jScrollPane3);

        tabla.setColumns(20);
        tabla.setRows(5);
        TabladeSimbolos.setViewportView(tabla);

        jTabbedPane1.addTab("Tabla de simbolos", TabladeSimbolos);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.PAGE_END);

        jTabbedPane3.setMinimumSize(new java.awt.Dimension(300, 103));

        LexicoCode.setColumns(20);
        LexicoCode.setRows(5);
        jScrollPane4.setViewportView(LexicoCode);

        jTabbedPane3.addTab("Lexico", jScrollPane4);

        SintacticoCode.setColumns(20);
        SintacticoCode.setRows(5);
        jScrollPane5.setViewportView(SintacticoCode);

        jTabbedPane3.addTab("Sintactico", jScrollPane5);

        SemanticoCode.setColumns(20);
        SemanticoCode.setRows(5);
        jScrollPane6.setViewportView(SemanticoCode);

        jTabbedPane3.addTab("Semantico", jScrollPane6);

        IntermedioCode.setColumns(20);
        IntermedioCode.setRows(5);
        IntermedioCode.setMinimumSize(new java.awt.Dimension(500, 100));
        jScrollPane7.setViewportView(IntermedioCode);

        jTabbedPane3.addTab("Codigo Intermedio", jScrollPane7);

        getContentPane().add(jTabbedPane3, java.awt.BorderLayout.LINE_END);

        jMenu1.setText("Archivo");

        jMenuItem1.setLabel("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Guardar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setLabel("Abrir");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Guardar Como");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Editar");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Formato");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        jMenu4.setText("Generar");
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
        });
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Compilar");
        jMenu5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu5MouseClicked(evt);
            }
        });
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ErroresCode.setText("");
        ResultadosCode.setText("");
        dir.Nuevo(this);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        
    }//GEN-LAST:event_formKeyReleased
    private void editorKeyReleased(java.awt.event.KeyEvent evt) {                                        
        int keyCode = evt.getKeyCode();
        if((keyCode >= 65 && keyCode <= 122) || (keyCode != 27 && keyCode <= 57)
            || (keyCode >= 97 && keyCode <= 122 ) || (keyCode != 27 && !(keyCode >= 37
                && keyCode <= 40) && !(keyCode >= 16
                && keyCode <= 18) && keyCode >=524 
                && keyCode != 20)){
            if(!getTitle().contains("*")){
                setTitle(getTitle()+"*");
            }
        }
    }    
    private void ErroresCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ErroresCodeKeyReleased

    }//GEN-LAST:event_ErroresCodeKeyReleased

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        dir.Guardar(this);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        dir.Abrir(this);
        clearAllComp();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        dir.guardarC(this);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Proximamente");
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Proximamente");
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Proximamente");
    }//GEN-LAST:event_jMenu4ActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Yo tambien la necesito");
    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        JOptionPane.showMessageDialog(rootPane, "Proximamente");
    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        JOptionPane.showMessageDialog(rootPane, "Proximamente");
    }//GEN-LAST:event_jMenu3MouseClicked

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        Sintactico();
        Semantico();
        SymbolTable();
        Errores();
        CodigoIntermedio();
        Salida();
        JOptionPane.showMessageDialog(rootPane, "Compilado");
    }//GEN-LAST:event_jMenu4MouseClicked

    private void jMenu5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu5MouseClicked
        
        JOptionPane.showMessageDialog(rootPane, "Compilado");
    }//GEN-LAST:event_jMenu5MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }
    
    public void clearAllComp(){
        ErroresCode.setText("");
        ResultadosCode.setText("");
    }
    
    public void clearErroresCode()throws IOException{
        ErroresCode.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea CodigoCode;
    private javax.swing.JTextArea ErroresCode;
    private javax.swing.JTextArea IntermedioCode;
    private javax.swing.JTextArea LexicoCode;
    private javax.swing.JTextArea ResultadosCode;
    private javax.swing.JTextArea SemanticoCode;
    private javax.swing.JTextArea SintacticoCode;
    private javax.swing.JScrollPane TabladeSimbolos;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextArea tabla;
    // End of variables declaration//GEN-END:variables
}

