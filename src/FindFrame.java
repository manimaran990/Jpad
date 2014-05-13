package jpad;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

public class FindFrame extends JFrame {
    private JTextField textFind,textRep;
    private JButton cancel, findReplace;
    private JLabel findLabel,repLabel;
    private RSyntaxTextArea textA;
           
    public FindFrame(RSyntaxTextArea textArea) {
        super("find");
        setVisible(true);
        textA=textArea;
        setSize(320,130);
        //setDefaultCloseOperation();
        setLayout(new FlowLayout());
        //setResizable(false);
        findLabel = new JLabel("Find what:        ");
        textFind = new JTextField(16);
        repLabel = new JLabel("Replace with:   ");
        textRep = new JTextField(16);
        cancel = new JButton("cancel");
        cancel.setActionCommand("cancel");
        findReplace = new JButton("Find and Replace");
        findReplace.setActionCommand("findReplace");
        add(findLabel);
        add(textFind);
        add(repLabel);
        add(textRep);
        add(cancel);
        add(findReplace);
        
        textFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchContext context = new SearchContext();
                String text=textFind.getText();
                context.setSearchFor(text);
                boolean found = SearchEngine.find(textA, context);
                if (!found) {
                 JOptionPane.showMessageDialog(null, "Text not found","not found",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        );
        textRep.addActionListener(new ActionListener() {        
                public void actionPerformed(ActionEvent e) {
                findReplace.doClick(0);
            }
            }
         );
    cancel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            dispose();            
            }
        }
    );
          
    findReplace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                SearchContext context = new SearchContext();
                String text=textFind.getText();
                String phrase = textRep.getText();
                if(text.length()==0 || phrase.length()==0) {
                    return;
                }
                context.setSearchFor(text);
                context.setMatchCase(true);
                context.setReplaceWith(phrase);
                context.setSearchForward(true);
                context.setWholeWord(false);
                boolean replaced = SearchEngine.replace(textA, context);
               
              }
        
        }
           
      );
                   
    }
       
}