import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.io.*;
import java.util.StringTokenizer;

public class Notepad extends JFrame {
    static JTextArea mainArea;
    JMenuBar menuBar;
    JMenu menuFile, menuEdit, menuFormat, menuHelp;
    // File Menu Item
    JMenuItem itemNew, itemOpen, itemSave, itemSaveAs, itemExit;
    // Edit Menu Item
    JMenuItem itemCut, itemCopy, itemPaste;
    // Format Menu Item
    JMenuItem itemFontColor, itemFind, itemReplace, itemFont;
    JMenuItem itemAbout;
    JCheckBoxMenuItem wordWarp;
    String fileName;
    JFileChooser jc;
    String fileContent;
    UndoManager undo;
    UndoAction undoAction;
    RedoAction redoAction;
    String findText;
    int fnext = 1;

    // For finding purpose
    public static Notepad frmNotepad = new Notepad();

    // Helper class for font
    FontHelper font;


    /* Constructors */
    public Notepad() {
        initComponent();


        /* Action Listeners */

        /* Listener for Save*/
        itemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        /* Listener for Save As*/
        itemSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });

        /* Listener for Open*/
        itemOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });

        /* Listener for Open New*/
        itemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNew();
            }
        });

        /* Listener for Exit */
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        /* Listener for Cut */
        itemCut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.cut();
            }
        });

        /* Listener for Copy */
        itemCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.copy();
            }
        });

        /* Listener for Paste */
        itemPaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.paste();
            }
        });

        /* Adding Undo and Redo */
        mainArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
                undoAction.Update();
                redoAction.Update();
            }
        });


        /* Listener for Ward Warp */
        wordWarp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wordWarp.isSelected()) {
                    mainArea.setLineWrap(true);
                    mainArea.setWrapStyleWord(true);
                } else {
                    mainArea.setLineWrap(false);
                    mainArea.setWrapStyleWord(false);
                }
            }
        });

        /* Listener for Font Color */
        itemFontColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(rootPane, "Coose Font Color", Color.black);
                mainArea.setForeground(c);
            }
        });


        itemFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(frmNotepad, false);
            }
        });


        itemReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(frmNotepad, true);
            }
        });

        itemFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(true);
            }
        });

        font.getOk().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.setFont(font.font());
                font.setVisible(false);
            }
        });

        font.getCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(false);
            }
        });

        itemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String aboutText =
                        "<html><big>Your Javapad</big><hr><hr>"
                                + "<p align=right>Developed By <b style: color = blue>Niloy</b> </p>"
                                + "<hr><p align=left>I Used jdk11.1 to compile the source code.<br><br>"
                                + "<strong>Thanx for using Notepad!</strong><br>"
                                + "Ur Comments as well as bug reports r very welcome at<p align=center>"
                                + "<hr><em><big>nkkundu2018@gmail.com</big></em><hr><html>";
                JOptionPane.showMessageDialog(rootPane, aboutText, "Dedicated 2 u!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }


    private void initComponent() {
        /* File Chooser */
        jc = new JFileChooser(".");

        /* Default Configuration */
        mainArea = new JTextArea();
        getContentPane().add(mainArea);
        getContentPane().add(new JScrollPane(mainArea), BorderLayout.CENTER);
        setTitle("Untitled Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // for Font
        font = new FontHelper();

        /* Undo Part */
        undo = new UndoManager();
        // IconImage for Undo
        ImageIcon iconUndo = new ImageIcon(getClass().getResource("img/undo.png"));
        ImageIcon iconRedo = new ImageIcon(getClass().getResource("img/redo.png"));
        undoAction = new UndoAction(iconUndo);
        redoAction = new RedoAction(iconRedo);


        /* Menu Bar */
        menuBar = new JMenuBar();

        // Menu
        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuFormat = new JMenu("Format");
        menuHelp = new JMenu("Help");

        // add Icon to Menu Item
        ImageIcon iconNew = new ImageIcon(getClass().getResource("img/new.png"));
        ImageIcon icoOpen = new ImageIcon(getClass().getResource("img/open.PNG"));
        ImageIcon iconSave = new ImageIcon(getClass().getResource("img/save.PNG"));
        ImageIcon iconSaveAs = new ImageIcon(getClass().getResource("img/saveas.png"));
        ImageIcon iconExit = new ImageIcon(getClass().getResource("img/exit.PNG"));
        ImageIcon iconCut = new ImageIcon(getClass().getResource("img/cut.png"));
        ImageIcon iconCopy = new ImageIcon(getClass().getResource("img/copy.png"));
        ImageIcon iconPaste = new ImageIcon(getClass().getResource("img/paste.png"));
        ImageIcon iconFind = new ImageIcon(getClass().getResource("img/find.png"));
        ImageIcon iconReplace = new ImageIcon(getClass().getResource("img/replace.png"));
        ImageIcon iconFont = new ImageIcon(getClass().getResource("img/font.PNG"));

        /* Menu Item */
        itemNew = new JMenuItem("New", iconNew);
        itemOpen = new JMenuItem("Open", icoOpen);
        itemSave = new JMenuItem("Save", iconSave);
        itemSaveAs = new JMenuItem("Save As", iconSaveAs);
        itemExit = new JMenuItem("Exit", iconExit);
        itemCut = new JMenuItem("Cut", iconCut);
        itemCopy = new JMenuItem("Copy", iconCopy);
        itemPaste = new JMenuItem("Paste", iconPaste);
        itemFind = new JMenuItem("Find", iconFind);
        itemReplace = new JMenuItem("Replace", iconReplace);
        wordWarp = new JCheckBoxMenuItem("Word Wrap");
        itemFontColor = new JMenuItem("Font Color");
        itemFont = new JMenuItem("Font", iconFont);
        itemAbout = new JMenuItem("About Notepad");


        /* Adding Shortcut Key */
        itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));


        /* For Mac OS Shortcut Key */
        itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));


        /* Add Menu Item */
        // Adding in Menu File
        menuFile.add(itemNew);
        menuFile.add(itemOpen);
        menuFile.add(itemSave);
        menuFile.add(itemSaveAs);
        menuFile.addSeparator();
        menuFile.add(itemExit);

        // Adding for Menu Edit
        menuEdit.add(undoAction);
        menuEdit.add(redoAction);
        menuEdit.addSeparator();
        menuEdit.add(itemCut);
        menuEdit.add(itemCopy);
        menuEdit.add(itemPaste);
        menuEdit.add(itemFind);
        menuEdit.add(itemReplace);
        menuEdit.add(itemFont);


        // Adding for Menu Format
        menuFormat.add(wordWarp);
        menuFormat.add(itemFontColor);

        // Adding for Menu help
        menuHelp.add(itemAbout);




        /* Add Menu Item to Menu Bar */
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuFormat);
        menuBar.add(menuHelp);

        // add Menu bar to frame
        setJMenuBar(menuBar);


    }


    /* Save Method */
    private void save() {
        PrintWriter fout = null;
        //int returnValue = -1;

        try {
            if (fileName == null) {
                saveAs();
            } else {
                fout = new PrintWriter(new FileWriter(fileName));

                String s = mainArea.getText();
                StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                while (st.hasMoreElements()) {
                    fout.println(st.nextToken()); // ekti ekti kore line (jekhane \n nei) seti write korbe
                }
                JOptionPane.showMessageDialog(rootPane, "File Saved!");
                fileContent = mainArea.getText();

            }
        } catch (IOException e) {

        } finally {
            if (fout != null)
                fout.close();
        }
    }


    /* Save As */
    private void saveAs() {
        PrintWriter fout = null;
        int returnValue = -1;
        try {
            returnValue = jc.showSaveDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (jc.getSelectedFile().exists()) {
                    int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to replace the file?", "Confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (option == 0) {
                        fout = new PrintWriter(new FileWriter(jc.getSelectedFile()));
                        String s = mainArea.getText();
                        StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                        while (st.hasMoreElements()) {
                            fout.println(st.nextToken()); // ekti ekti kore line (jekhane \n nei) seti write korbe
                        }
                        JOptionPane.showMessageDialog(rootPane, "File Saved!");
                        fileContent = mainArea.getText();
                        fileName = jc.getSelectedFile().getName();
                        setTitle(fileName = jc.getSelectedFile().getName());
                    } else {
                        saveAs();
                    }
                } else {
                    fout = new PrintWriter(new FileWriter(jc.getSelectedFile()));
                    String s = mainArea.getText();
                    StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                    while (st.hasMoreElements()) {
                        fout.println(st.nextToken()); // ekti ekti kore line (jekhane \n nei) seti write korbe
                    }
                    JOptionPane.showMessageDialog(rootPane, "File Saved!");
                    fileContent = mainArea.getText();
                    fileName = jc.getSelectedFile().getName();
                    setTitle(fileName = jc.getSelectedFile().getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fout != null) {
                fout.close();
            }
        }
    }

    /* Open Method */
    private void open() {
        try {
            int returnValue = jc.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                mainArea.setText(null);
                Reader in = new FileReader(jc.getSelectedFile());
                char[] buffer = new char[10000000];
                int nch;
                while ((nch = in.read(buffer, 0, buffer.length)) != -1) {
                    mainArea.append(new String(buffer, 0, nch));
                }

                fileName = jc.getSelectedFile().getName();
                setTitle(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Open New */
    private void openNew() {
        if (!mainArea.getText().equals("") && !mainArea.getText().equals(fileContent)) {
            if (fileName == null) {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    saveAs();
                    clear();
                } else if (option == 2) {

                } else {
                    clear();
                }
            } else {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    save();
                    clear();
                } else if (option == 2) {

                } else {
                    clear();
                }
            }
        } else {
            clear();
        }
    }

    /* Clear Method*/
    private void clear() {
        mainArea.setText(null);
        setTitle("Untitled Notepad");
        fileName = null;
        fileContent = null;
    }

    /* Class for Undo */
    class UndoAction extends AbstractAction {
        public UndoAction(ImageIcon undoIcon) {
            super("Undo", undoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }

            Update();
            redoAction.Update();
        }

        /* Update Undo */
        private void Update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "Undo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    /* Class for Redo */
    class RedoAction extends AbstractAction {
        public RedoAction(ImageIcon redoIcon) {
            super("Rodo", redoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                ex.printStackTrace();
            }

            Update();
            undoAction.Update();
        }

        /* Update Redo */
        private void Update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "Redo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }


    public static void main(String[] args) {
        Notepad jn = new Notepad();
        jn.setVisible(true);
    }

    public static JTextArea getArea() {
        return mainArea;
    }
}
