/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.python.core.PyInstance;
import org.python.util.PythonInterpreter;


/**
 *
 * @author xjlop
 */
public class EditorScrollPane extends JScrollPane {

  private static final long serialVersionUID = 1L;

  private JTextPane inputArea;
  private String indentation = "  ";
  private TextLineNumber lineNumbers;
  private Ventana compf;
  private String respuesta = "";
  private String errores = "";
  private int position;
  private HashMap<String, String> map = new HashMap<String, String>();

  /*
   * Here the constructor creates a TextPane as an editor-field and another TextPane for the
   * line-numbers.
   */
  public EditorScrollPane(int width, int height, Ventana compF) {
    // Editor-field
    inputArea = new JTextPane();
    inputArea.setPreferredSize(new Dimension(width, height));
    inputArea.setMinimumSize(new Dimension(width, height));
    compf=compF;
    
    
    
    
    Document doc = inputArea.getDocument();

    // Replacing tabs with two spaces
    ((AbstractDocument) doc).setDocumentFilter(new DocumentFilter() {
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        super.insertString(fb, offset, text.replace("\t", indentation), attrs);
      }
    });


    doc.addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        CambiarEstilos();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        CambiarEstilos();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        CambiarEstilos();
      }
    });
    // Setting font
    this.setFont(new Font("Monospaced", 12, Font.PLAIN));

    // Sets the main-component in the JScrollPane. this.add(inputArea) wasn't
    // enough in this case
    this.getViewport().add(inputArea);

    // Adds lineNumbers as row header on the left side of the main JTextPane
    lineNumbers = new TextLineNumber(inputArea);
    this.setRowHeaderView(lineNumbers);
    
    inputArea.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            editorKeyReleased(evt);
            try {
                limpiarJFrame();
                modificarJFrame(respuesta,errores);
                CambiarEstilos();
            } catch (IOException ex) {
                Logger.getLogger(EditorScrollPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
  }
    
    private void modificarJFrame(String resp,String err) throws IOException{
        Ventana jframe = (Ventana) SwingUtilities.getWindowAncestor(this);
        jframe.obtenerRespuesta(resp,err);
    }
    private void limpiarJFrame() throws IOException{
        Ventana jframe = (Ventana) SwingUtilities.getWindowAncestor(this);
        jframe.clearAllComp();
    }
      private int findLastNonWordChar(String text, int index){
        while(--index >= 0){
            if(String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
        }
        return index;
    }
    
    private int findFirstNonWordChar(String text, int index){
        while(index < text.length()){
            if(String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
            index++;
        }
        return index;        
    }
    
    
    public void CambiarEstilos(){
        position = inputArea.getCaretPosition();
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        
        final AttributeSet colorazul = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(6,57,112));
        final AttributeSet colorrojo = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(121,20,23));
        final AttributeSet colormorado = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(64,20,121));
        final AttributeSet colornegro = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,0,0));
        final AttributeSet colorgris = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(155,155,155));
        final AttributeSet colorverde = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,155,0));
        final AttributeSet colornaranja = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(200,100,100));
        DefaultStyledDocument doca = new DefaultStyledDocument(){

        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException{

            super.insertString(offset, str, a);
            
           String text = getText(0, getLength());
           setCharacterAttributes(0, getLength(), colornegro, true);
           
                Pattern reserved_words = Pattern.compile("\\b(main|if|else|end|do|while|then|repeat|until|cin|cout)\\b");
                Matcher matcher = reserved_words.matcher(text);
                while (matcher.find()) {
                    setCharacterAttributes(matcher.start(),
                            matcher.end() - matcher.start(), colorrojo, true);
                }
                Pattern digits = Pattern.compile("\\b(-?\\d+(\\.\\d+)?)\\b");
                matcher = digits.matcher(text);
                while (matcher.find()) {
                    setCharacterAttributes(matcher.start(),
                            matcher.end() - matcher.start(), colormorado, true);
                }
                Pattern data_type = Pattern.compile("\\b(int|real|boolean|float)\\b");
                matcher = data_type.matcher(text);
                while (matcher.find()) {
                    setCharacterAttributes(matcher.start(),
                            matcher.end() - matcher.start(), colorazul, true);
                }
                Pattern bool_words = Pattern.compile("\\b(true|false)\\b");
                matcher = bool_words.matcher(text);
                while (matcher.find()) {
                    setCharacterAttributes(matcher.start(),
                            matcher.end() - matcher.start(), colornaranja, true);
                }
                Pattern arithmetic_operators = Pattern.compile("[-+*/=<>!]");
                matcher = arithmetic_operators.matcher(text);
                while (matcher.find()) {
                    setCharacterAttributes(matcher.start(),
                            matcher.end() - matcher.start(), colorverde, true);
                }
                Pattern one_line_comments = Pattern.compile("\\/\\/.*");
                matcher = one_line_comments.matcher(text);

                while (matcher.find()) {
                    setCharacterAttributes(matcher.start(),
                            matcher.end() - matcher.start(),colorgris, false);
                }

                Pattern mutiple_line_comments = Pattern.compile("\\/\\*.*?\\*\\/",
                        Pattern.DOTALL);
                matcher = mutiple_line_comments.matcher(text);

                while (matcher.find()) {
                    setCharacterAttributes(matcher.start(),
                            matcher.end() - matcher.start(), colorgris, false);
                }
            }
            public void romeve(int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) {
                    before = 0;
                }
            }
        };
        JTextPane txt = new JTextPane(doca);
        String temp = this.inputArea.getText();
        this.inputArea.setStyledDocument(txt.getStyledDocument());
        this.inputArea.setText(temp);
        inputArea.setCaretPosition(position);
    }            
           /* 
            int before = findLastNonWordChar(text, offset);
            if (before < 0) {
                before = 0;
            }
            int after = findFirstNonWordChar(text, offset + str.length());
            int wordL = before;
            int wordR = before;

            while (wordR <= after) {
                if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                    if (text.substring(wordL, wordR).matches("(\\W)*(main|if|then|else|do|while|repeat|until|cin|cout|end|until)")) {
                        setCharacterAttributes(wordL, wordR - wordL, colorrojo, false);                        
                    } else if (text.substring(wordL, wordR).matches("(\\W)*(int|real|boolean|char)")) {
                        setCharacterAttributes(wordL, wordR - wordL, colorazul, false);
                    } else if (text.substring(wordL, wordR).matches("(\\W)*(\\d+$)")) {
                        setCharacterAttributes(wordL, wordR - wordL, colormorado, false);
                    }
                    else if (text.substring(wordL, wordR).matches("(\\W)*(true|TRUE|false|FALSE)")) {
                        setCharacterAttributes(wordL, wordR - wordL, colorverde, false);
                    } else if (text.substring(wordL, wordR).matches("[-+*//*]")) {
                        setCharacterAttributes(wordL, wordR - wordL, colornaranja, false);
                    }
                    else {
                        setCharacterAttributes(wordL, wordR - wordL, colornegro, false);
                    }
                    wordL = wordR;
                }
                wordR++;
            }*/

    void paiton(){
                PythonInterpreter interpreter = new PythonInterpreter();
                System.out.println(inputArea.getText());
                String[] argumentos = inputArea.getText().split("\\r?\\n");
                String ArgumentosString = "[";
                for (int i = 0; i < argumentos.length; i++) {
                    ArgumentosString += ("'" + argumentos[i] + "'");
                    if (i == (argumentos.length - 1)) {

                    } else {
                        ArgumentosString += ",";
                    }
                }
                ArgumentosString += "]";
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                interpreter.setOut(printStream);
                interpreter.exec(
                        "import sys\n"
                        + "sys.argv = " + ArgumentosString);
                System.out.println(ArgumentosString);
                interpreter.execfile("../../AnalizadorLexico/pruebaarchivo.py");
                String output = outputStream.toString();
                String resp = output;
                int index = resp.indexOf('\n');
                String tam = (index >= 0) ? resp.substring(0, index) : resp; 
                String numericString = tam.replaceAll("\\D+", "");
                int numericValue = Integer.parseInt(numericString);
                String[] lines = resp.split("\n");
                if(numericValue < lines.length - 1)
                    errores = String.join("\n", Arrays.copyOfRange(lines, numericValue+1, lines.length));
                else
                    errores = "";
                //errores = (index >= 0) ? resp.substring(index + tamanio) : resp;
                String[] lineas = resp.split("\n");
                respuesta = resp;
                int start = 1;
                if (start >= 0 && numericValue < lines.length) {
                    respuesta = String.join("\n", Arrays.copyOfRange(lineas, start, numericValue + 1));
                }
            map.clear();
            String[] liner = respuesta.split("\n");
            for (String line : liner) {
                String[] parts = line.replace("[", "").replace("]", "").split(",");
                if (parts.length == 2) { // add this line to check the length of parts
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    map.put(key, value);
                }
            }
    }
    
    
    
    public void editorKeyReleased(java.awt.event.KeyEvent evt) {                                        
        int keyCode = evt.getKeyCode();
        if((keyCode >= 65 && keyCode <= 90) || (keyCode >= 48 && keyCode <= 57)
            || (keyCode >= 97 && keyCode <= 122 ) || (keyCode != 27 && !(keyCode >= 37
                && keyCode <= 40) && !(keyCode >= 16
                && keyCode <= 18) && keyCode !=524 
                && keyCode != 20)){
        if (keyCode == KeyEvent.VK_DELETE){
            
        }
        paiton();
               
            if(!compf.getTitle().contains("*")){
                compf.setTitle(compf.getTitle()+"*");
            }
        }
    }    

  private void lineNumbers() {

                //String s = inputArea.getText();
                //System.out.println(s);

            
        // Plain Style

        /*} catch (BadLocationException e) {
      e.printStackTrace();
    }*/
    }


  /*
   * Setting indentation size in editor-field
   */
  public void setIndentationSize(int size) {
    String cache = indentation;
    indentation = "";
    for (int i = 0; i < size; i++) {
      indentation += " ";
    }
    inputArea.setText(inputArea.getText().replaceAll(cache, indentation));
  }

  /*
   * Overrides the method getText().
   */
  public String getText() {
    return inputArea.getText();
  }

  public void setText(String str) {
    inputArea.setText(str);
  }
  
  public String getRespuesta(){
      return respuesta;
  }
  
  public String getErrores(){
      return errores;
  }
  
  
}