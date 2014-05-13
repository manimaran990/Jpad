package jpad;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTabbedPane;
import java.io.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class JPad extends JFrame {
    private final int[] fontSizeArray = {10,12,14,16,18,20};
    private String[] fontNameItems = {"Serif","Monospaced","SansSerif"};
    private int curFontSize=14;
    private String curFontName="";
    private JTabbedPane tabPan;
   
    private RSyntaxTextArea textArea;
    private Font fontstyle;
    private JMenuItem newMenu;
    private JMenuItem openMenu; 
    private String filename="";
    private String classname="";
    private File file;
    private JRadioButtonMenuItem[] fontItems;
    private JRadioButtonMenuItem[] fontNames;
    
    //constructor starts
    public JPad() {
        super("JPad");
        setLookAndFeel();
        JPanel cp = new JPanel(new BorderLayout());     
        textArea = new RSyntaxTextArea(60, 40);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        sp.setFoldIndicatorEnabled(true);
        cp.add(sp);
        fontstyle = new Font(curFontName,Font.TRUETYPE_FONT,curFontSize);    
        textArea.setFont(fontstyle);     
        setContentPane(cp);
                          
        //creating File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F'); //set shortcut

        //adding New menuitem
        newMenu = new JMenuItem("New");
        newMenu.setMnemonic('N');
        newMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        fileMenu.add(newMenu);      

        newMenu.addActionListener(
            new ActionListener() {
                //method
                public void actionPerformed(ActionEvent event) {
                    //method to create a new window
                    JPad newpad = new JPad();
                    newpad.setVisible(true);
                    newpad.setSize(750,500);
                    newpad.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                }
            }

        );
        
        //creating menu item for file menu
        openMenu = new JMenuItem("Open..");
        openMenu.setMnemonic('O'); //set shortcut
        openMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
        fileMenu.add(openMenu); //added to fileMenu
        openMenu.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    JFileChooser filechooser = new JFileChooser();
                    filechooser.setFileSelectionMode(filechooser.FILES_ONLY);
                    int option = filechooser.showOpenDialog(null);
                    
                    if(option==JFileChooser.APPROVE_OPTION) {
                        String line="";
                        try{
                        File file = filechooser.getSelectedFile();
                        filename=file.getAbsolutePath();
            	        classname = file.getName();
			//JOptionPane.showMessageDialog(null,"filename is "+filename,"filename",JOptionPane.INFORMATION_MESSAGE);

                        BufferedReader br = new BufferedReader(new FileReader(file));
                        while((line=br.readLine())!=null) {
                            textArea.append(line+"\n");
                            }
                        setTitle("JPad "+filename);

                        } //end try
                        catch(IOException e) {
                            e.printStackTrace();
                        } //end catch

                    }
                    
                }
            }
        );
                
        
        //creating save menuitem
        JMenuItem saveMenu = new JMenuItem("Save");
        saveMenu.setMnemonic('S');
        saveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        saveMenu.addActionListener(
            new ActionListener() 
            {
                //method to save the file
                public void actionPerformed(ActionEvent event) {
                    //method to save
                    //JOptionPane.showMessageDialog(null,filename,"filename",JOptionPane.INFORMATION_MESSAGE);        
                    if(filename!="")
                    {    
                        try {
                        String content = textArea.getText();    
                        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
                        bw.write(content);
                        bw.close();
                        } // end try
                        catch(IOException e) {
                            e.printStackTrace();
                        } //end catch
                    } // end else

                    else 
                        saveFile();
                } // end method
            } //end listener
        ); 

        fileMenu.add(saveMenu);

        //creating saveAs menuItem
        JMenuItem saveasMenu = new JMenuItem("Save As..");
        saveasMenu.setMnemonic('A');
        saveasMenu.addActionListener(
            new ActionListener() {
                //method to invoke save as
                    public void actionPerformed(ActionEvent event) {
                    //method to save
                    saveFile();
                }
            }
        );
        fileMenu.add(saveasMenu);

        //seperator in filemenu
        fileMenu.addSeparator();

        //creating exit item for file menu
        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.setMnemonic('x');
        exitMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
        fileMenu.add(exitMenu);
        //adding actionListener to exitMenu
        exitMenu.addActionListener(
            new ActionListener() {
                //exit method
                public void actionPerformed(ActionEvent event) {
                    int reply = JOptionPane.showConfirmDialog(null,"Are you serious??","Quit",JOptionPane.YES_NO_OPTION);
                    if(reply == JOptionPane.YES_OPTION)
                    System.exit(0);
                }
            }
        ); //listener end
       

        //creating Run Menu
        JMenu javaMenu = new JMenu("Java");
        javaMenu.setMnemonic('J');

        //creating items for run menu compile and run
        //creating compile menu item
        JMenuItem compileItem = new JMenuItem("Compile");
        compileItem.setMnemonic('C');
        compileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, Event.ALT_MASK));
        compileItem.addActionListener(
            new ActionListener() {
                //method for listener
                public void actionPerformed(ActionEvent event) {
                          //JOptionPane.showMessageDialog(null,"filename is "+"filename "+filename+"\npath:"+file.getPath(),"filename",JOptionPane.INFORMATION_MESSAGE);
                    //method to invoke compilation
                    try {
                        String cmds="@echo off\n javac %1\n pause\n exit";
			//String path = parentpath+"\\"+"compile.bat";
			BufferedWriter  bw = new BufferedWriter(new FileWriter("compile.bat"));
                     	//JOptionPane.showMessageDialog(null,"batch path ",filename,"filename",null);
                        bw.write(cmds);
                        bw.close();       
			Process pr = Runtime.getRuntime().exec("cmd.exe /c start compile.bat "+filename);
			pr.waitFor();

                    } // end try
                    catch(Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        );
        javaMenu.add(compileItem); //added to javaMenu

        //creating run menu item
        JMenuItem runItem = new JMenuItem("Run");
        runItem.setMnemonic('R');
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, Event.CTRL_MASK));
        runItem.addActionListener(
            new ActionListener() {
            //method to run the java code
            
	public void actionPerformed(ActionEvent event) {
	//JOptionPane.showMessageDialog(null,"classname is "+classname,"classname",JOptionPane.INFORMATION_MESSAGE);
	String clas="";
            if (classname.indexOf(".") > 0) {
            clas = classname.substring(0, classname.lastIndexOf("."));
            }
		//JOptionPane.showMessageDialog(null,"classname is "+clas,"class",JOptionPane.INFORMATION_MESSAGE);
			try {
			String cmds="@echo off\n java %1\n pause\n exit";
			BufferedWriter  bw = new BufferedWriter(new FileWriter("run.bat"));
               //JOptionPane.showMessageDialog(null,"batch path ",filename,"filename",null);
               bw.write(cmds);
               bw.close();       
		      Process pr = Runtime.getRuntime().exec("cmd.exe /c start run.bat "+clas);
		      pr.waitFor();
			} catch(Exception e) {
			e.printStackTrace();
			}
		  }
		}
        );
        javaMenu.add(runItem);

        JMenuItem cmdItem = new JMenuItem("cmd prompt");
        cmdItem.setMnemonic('c');
        cmdItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, Event.CTRL_MASK));
        cmdItem.addActionListener(
        	new ActionListener() {
        		public void actionPerformed(ActionEvent event) {
        			try {
        			Process pr = Runtime.getRuntime().exec("cmd.exe /k start cmd");
        			} catch(IOException e) {
        				e.printStackTrace();
        			}
        		}
        	}
        );
        javaMenu.add(cmdItem);
        
        //creating format menu
        JMenu formatMenu = new JMenu("Format");
        formatMenu.setMnemonic('o');
        
                
        JMenu fontSizeMenu = new JMenu("FontSize");
        String fontSizes[] = {"10","12","14","16","18","20"};
        fontItems = new JRadioButtonMenuItem[fontSizeArray.length];
        ButtonGroup sizeButtonGroup = new ButtonGroup();
        ItemHandler itemhandler = new ItemHandler();
        FontHandler fonthandler = new FontHandler();
        
        
        for(int i=0;i<fontSizeArray.length;i++) {
            fontItems[i] = new JRadioButtonMenuItem(fontSizes[i]);
            fontSizeMenu.add(fontItems[i]);
            sizeButtonGroup.add(fontItems[i]);
            fontItems[i].addActionListener(itemhandler);
        }
        fontItems[2].setSelected(true);
        formatMenu.add(fontSizeMenu);
        
        //creating fontName menu for formatMenu
        
        JMenu fontNameMenu = new JMenu("Font");
        fontNameMenu.setMnemonic('f');
        
        JRadioButtonMenuItem[] fontNames = new JRadioButtonMenuItem[fontNameItems.length];
        ButtonGroup fontButtonGroup = new ButtonGroup();
        
        for(int i=0;i<fontNameItems.length;i++) {
            fontNames[i] = new JRadioButtonMenuItem(fontNameItems[i]);
            fontNameMenu.add(fontNames[i]);
            fontButtonGroup.add(fontNames[i]);
            fontNames[i].addActionListener(fonthandler);
        }
        fontNames[0].setSelected(true);
        formatMenu.add(fontNameMenu);
        
       //creating find menu
        
        JMenu findMenu = new JMenu("Search");
        findMenu.setMnemonic('s');
        JMenuItem findItem = new JMenuItem("Find");
        findItem.setMnemonic('f');
        findItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,Event.CTRL_MASK));
        
        findItem.addActionListener(
                new ActionListener() {
                    //method to find words and highlight
                    public void actionPerformed(ActionEvent event) {
                        //method desc
                        //FindFrame ff = new FindFrame(textArea);
                        textArea.setCaretPosition(0);
                        SearchContext context = new SearchContext();
                        String text;                       
                        text=JOptionPane.showInputDialog("Enter phrase to search");                                            
                        if(text.length()==0) {
                            return;
                        }
                        context.setSearchFor(text);                      
                        context.setMatchCase(true);
                        //context.setSearchForward(true);
                        context.setWholeWord(false);
                        
                        boolean found = SearchEngine.find(textArea, context);
                        if(!found) {
                            JOptionPane.showMessageDialog(null,"word not found","not found",JOptionPane.ERROR_MESSAGE);
                        }
                       
                     }
                }
        );
        findMenu.add(findItem);
        
        //find and replace
        JMenuItem replaceItem = new JMenuItem("Replace");
        replaceItem.setMnemonic('r');
        replaceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,Event.CTRL_MASK));
        replaceItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    textArea.setCaretPosition(0);
                    FindFrame replace = new FindFrame(textArea);
                    replace.setLocationRelativeTo(null);
                    
                }
            }
        );
        findMenu.add(replaceItem);
        
                

        //creating help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        //creating items for helpMenu
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic('b');
        aboutItem.addActionListener(
            new ActionListener() {
                //method to invoke about
                public void actionPerformed(ActionEvent event) {
                    JOptionPane.showMessageDialog(JPad.this,"Simple java editor\n\nCoded by ManiG\n\n",
                        "About",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        );
        helpMenu.add(aboutItem);


        //creating bar
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar); //adding it to application
        bar.add(fileMenu);
        bar.add(findMenu);
        bar.add(formatMenu);
        bar.add(javaMenu);
        bar.add(helpMenu);
                
        } //constructor end
    
    
        //inner class for itemhandler
        private class ItemHandler implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                try {
                for(int i=0;i<fontItems.length;i++) {
                    if(fontItems[i].isSelected()) {                       
                        curFontSize=fontSizeArray[i];
                        textArea.setFont(new Font(curFontName,Font.PLAIN,curFontSize));
                        
                    }
                }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                }
            }
        
        //inner class for fonthandler
        private class FontHandler implements ActionListener {
              public void actionPerformed(ActionEvent event) {
                try {
                     for(int i=0;i<fontNameItems.length;i++) {
                       if(fontNames[i].isSelected()) {
                        JOptionPane.showMessageDialog(null,"font selected "+fontNameItems[i],"font",JOptionPane.INFORMATION_MESSAGE);
                        curFontName=fontNameItems[i];
                        SyntaxScheme scheme = textArea.getSyntaxScheme();
                        scheme.getStyle(Token.COMMENT_EOL).font = new Font("Georgia",Font.PLAIN, curFontSize);
                        //JOptionPane.showMessageDialog(null,"font selected "+fontNameItems[i],"font",JOptionPane.INFORMATION_MESSAGE);
                        //curFontName=fontNameItems[i];
                        //textArea.setFont(new Font(curFontName,Font.PLAIN,curFontSize));                 
                    }
                    else textArea.setFont(new Font(curFontName,Font.PLAIN,curFontSize));                 
                  }
                } // end try
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
               
    
        //saveFile() method
        public void saveFile() {
                JFileChooser filechooser = new JFileChooser();
                        filechooser.setFileSelectionMode(filechooser.FILES_ONLY);
                        int retrive = filechooser.showSaveDialog(null);
                    
                        if(retrive == JFileChooser.APPROVE_OPTION) {

                        try {
                        String content = textArea.getText();
                        File file = filechooser.getSelectedFile();
                        filename=file.getAbsolutePath();                    
                        setTitle("JPad "+filename);
                        classname = file.getName();
                        BufferedWriter  bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                        bw.write(content);
                        bw.close();                                                             
                        } //try ends
                        catch(IOException e) {
                            e.printStackTrace();
                            } // end catch
                        } //end if
           } // end method
          

        //lookandFeel
        public void setLookAndFeel() {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch(Exception ex) {
               //nothing
            }
        } //lookandFeel end
        
        public static void main(String[] args) {
        JPad app = new JPad();
        app.setSize(750,500);
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
        app.setVisible(true);
    } //main end

} //class end