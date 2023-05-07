/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
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
  private JTextPane lineNumbers;
  private Ventana compf;

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
    lineNumbers();

    // Replacing tabs with two spaces
    ((AbstractDocument) doc).setDocumentFilter(new DocumentFilter() {
      public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
          throws BadLocationException {
        super.insertString(fb, offset, text.replace("\t", indentation), attrs);
      }
    });

    // Line-numbers
    lineNumbers = new JTextPane();
    lineNumbers.setBackground(Color.GRAY);
    lineNumbers.setEditable(false);
    // Line-numbers should be right-aligned
    SimpleAttributeSet rightAlign = new SimpleAttributeSet();
    StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
    lineNumbers.setParagraphAttributes(rightAlign, true);

    doc.addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        lineNumbers();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        lineNumbers();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        lineNumbers();
      }
    });
    // Setting font
    this.setFont(new Font("Monospaced", 12, Font.PLAIN));

    // Sets the main-component in the JScrollPane. this.add(inputArea) wasn't
    // enough in this case
    this.getViewport().add(inputArea);

    // Adds lineNumbers as row header on the left side of the main JTextPane
    this.setRowHeaderView(lineNumbers);
    
    inputArea.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            editorKeyReleased(evt);
        }
    });
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
    
    public void colors(){

    }
    
    private void editorKeyReleased(java.awt.event.KeyEvent evt) {                                        
        int keyCode = evt.getKeyCode();
        if((keyCode >= 65 && keyCode <= 122) || (keyCode != 27 && keyCode <= 57)
            || (keyCode >= 97 && keyCode <= 122 ) || (keyCode != 27 && !(keyCode >= 37
                && keyCode <= 40) && !(keyCode >= 16
                && keyCode <= 18) && keyCode >=524 
                && keyCode != 20)){
        if (keyCode == KeyEvent.VK_DELETE){
            lineNumbers();
        }
            if(!compf.getTitle().contains("*")){
                compf.setTitle(compf.getTitle()+"*");
            }
        }
    }    

  private void lineNumbers() {
    //try {
      //String stra = inputArea.getText();
              final StyleContext cont = StyleContext.getDefaultStyleContext();
        
        final AttributeSet colorazul = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(6,57,112));
        final AttributeSet colorrojo = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(121,20,23));
        final AttributeSet colormorado = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(64,20,121));
        final AttributeSet colornegro = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,0,0));
        final AttributeSet colorgris = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(155,155,155));
        
        DefaultStyledDocument doca = new DefaultStyledDocument(){
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException{
                String stra = inputArea.getText();
                System.out.println(stra);
                SimpleAttributeSet plain = new SimpleAttributeSet();
      StyleConstants.setFontFamily(plain, "Monospaced");
      StyleConstants.setFontSize(plain, 12);

      // Bold style
      SimpleAttributeSet bold = new SimpleAttributeSet();
      StyleConstants.setBold(bold, true);

      // Remove all from document
      Document doc = lineNumbers.getDocument();
      doc.remove(0, doc.getLength());

      // Calculating the number of lines
      int length = stra.length() - stra.replaceAll("\n", "").length() + 1;

      // Adding line-numbers
      for (int i = 1; i <= length; i++) {

        // Non-bold line-numbers
        if (i < length) {
          doc.insertString(doc.getLength(), i + "\n", plain);

        // Last line-number bold
        } else {
          doc.insertString(doc.getLength(), i + "\n", bold);
        }
      }
                super.insertString(offset, str, a);
                String text = getText(0,getLength());
                int before = findLastNonWordChar(text,offset);
                if(before < 0){
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offset+str.length());
                int wordL = before;
                int wordR = before;
                
                while(wordR <= after){
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")){
                        if(text.substring(wordL, wordR).matches("(\\W)*(main|if|then|else|do|while|repeat|until|cin|cout)")){
                            setCharacterAttributes(wordL, wordR-wordL, colorrojo, false);
                        }
                        else if(text.substring(wordL, wordR).matches("(\\W)*(int|real|boolean|char)")){
                            setCharacterAttributes(wordL, wordR-wordL, colorazul, false);
                        }
                        else if(text.substring(wordL, wordR).matches("(\\d)*{10}")){
                            setCharacterAttributes(wordL, wordR-wordL, colormorado, false);
                        }
                        else{
                            setCharacterAttributes(wordL, wordR-wordL, colornegro, false);
                        }
                        wordL=wordR;
                    }
                    else if(text.substring(wordL, wordR).matches("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/")){
                        setCharacterAttributes(wordL, wordR-wordL, colorgris, false);
                    }
                    wordR++;
                }
                //String s = inputArea.getText();
                //System.out.println(s);
                PythonInterpreter interpreter = new PythonInterpreter();
                System.out.println(inputArea.getText());
                String[] argumentos = inputArea.getText().split("\\r?\\n");
                String ArgumentosString = "[";
                for(int i = 0; i < argumentos.length; i++){
                    ArgumentosString += ("'"+argumentos[i]+"'");
                    if(i==(argumentos.length-1)){
                        
                    }
                    else
                        ArgumentosString += ",";
                }
                ArgumentosString += "]";
                System.out.println(ArgumentosString);
                interpreter.exec(
                        "import sys\n"
                    +"sys.argv = "+ArgumentosString);
                interpreter.execfile("../../AnalizadorLexico/prueba.py");
            }
            public void remove(int offs, int len) throws BadLocationException{
                super.remove(offs, len);
                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if(before < 0){
                    before = 0;
                }
            }
        };
        JTextPane txt = new JTextPane(doca);
        String temp = inputArea.getText();
        inputArea.setStyledDocument(txt.getStyledDocument());
        inputArea.setText(temp);
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
    // Replace all previous indentations (at beginning of lines)
    inputArea.setText(inputArea.getText().replaceAll(cache, indentation));
  }

  /*
   * Overrides the method getText().
   */
  public String getText() {
    return inputArea.getText();
  }

  /*
   * Overrides the method setText().
   */
  public void setText(String str) {
    inputArea.setText(str);
  }
  
}