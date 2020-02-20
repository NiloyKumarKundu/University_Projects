import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FindAndReplace extends JDialog implements ActionListener {
    boolean foundOne, isReplace;
    JTextField searchText, replaceText;
    JCheckBox cbCase, cbWhole;
    JRadioButton up, down;
    JLabel statusInfo;
    JFrame owner;
    JPanel north, center, south;


    public FindAndReplace(JFrame owner, boolean isReplace) {
        super(owner, true);
        this.isReplace = isReplace;

        north = new JPanel();
        center = new JPanel();
        south = new JPanel();

        if (isReplace) {
            setTitle(" Find and Replace");
            setReplacePanel(north);
        } else {
            setTitle("Find");
            setFindPanel(north);
        }
        addComponent(center);
        statusInfo = new JLabel("Status Info: ");
        south.add(statusInfo);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        getContentPane().add(north, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);
        pack();                 // ekti window er vitor component gulo jototuku jaiga niye ache tototuku jaiga niyei thakbe
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        int x = (owner.getWidth() * 3 / 5) - (getWidth() / 2);
        int y = (owner.getHeight() * 3 / 5) - (getHeight() / 2);
        setLocation(x, y);
        setVisible(true);
    }

    private void addComponent(JPanel center) {
        JPanel east = new JPanel();
        JPanel west = new JPanel();

        center.setLayout(new GridLayout(1, 2));
        east.setLayout(new GridLayout(2, 1));
        west.setLayout(new GridLayout(2, 1));

        cbCase = new JCheckBox("Match Case", true);
        cbWhole = new JCheckBox("Match Word", true);

        ButtonGroup group = new ButtonGroup();
        up = new JRadioButton("Search Up", false);
        down = new JRadioButton("Search Down", true);

        group.add(up);
        group.add(down);
        east.add(cbCase);
        east.add(cbWhole);
        east.setBorder(BorderFactory.createTitledBorder("Search Options: "));
        west.add(up);
        west.add(down);
        west.setBorder(BorderFactory.createTitledBorder("Search Direction: "));
        center.add(east);
        center.add(west);
    }

    private void setFindPanel(JPanel north) {
        final JButton NEXT = new JButton("Find Next");
        NEXT.addActionListener(this);
        NEXT.setEnabled(false);
        searchText = new JTextField(20);
        searchText.addActionListener(this);
        searchText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                boolean state = (searchText.getDocument().getLength() > 0);
                NEXT.setEnabled(state);
                foundOne = false;
            }
        });

        if (searchText.getText().length() > 0) {
            NEXT.setEnabled(true);
        }

        north.add(new JLabel("Find Word:"));
        north.add(searchText);
        north.add(NEXT);

    }

    /* Replace Panel */
    private void setReplacePanel(JPanel north) {
        GridBagLayout grid = new GridBagLayout();
        north.setLayout(grid);
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblFword = new JLabel("Find Word:");
        JLabel lblRword = new JLabel("Replace Word:");
        final JButton NEXT = new JButton("Replace Text");
        NEXT.addActionListener(this);
        NEXT.setEnabled(false);

        final JButton REPLACE = new JButton("Replace All");
        REPLACE.addActionListener(this);
        REPLACE.setEnabled(false);
        searchText = new JTextField(20);
        replaceText = new JTextField(20);

        replaceText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean state = (replaceText.getDocument().getLength() > 0);
                NEXT.setEnabled(state);
                REPLACE.setEnabled(state);
                foundOne = false;
            }
        });

        con.gridx = 0;
        con.gridy = 0;
        grid.setConstraints(lblFword, con);
        north.add(lblFword);

        con.gridx = 1;
        con.gridy = 0;
        grid.setConstraints(searchText, con);
        north.add(searchText);

        con.gridx = 2;
        con.gridy = 0;
        grid.setConstraints(NEXT, con);
        north.add(NEXT);

        con.gridx = 0;
        con.gridy = 1;
        grid.setConstraints(lblRword, con);
        north.add(lblRword);

        con.gridx = 1;
        con.gridy = 1;
        grid.setConstraints(replaceText, con);
        north.add(replaceText);

        con.gridx = 2;
        con.gridy = 1;
        grid.setConstraints(REPLACE, con);
        north.add(REPLACE);


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(searchText) || e.getSource().equals(replaceText)) {
            validate();     // validate method er maddhome amara process er moddhe j poriborton gulo korechi seta update hobe. eti use na korle amra poriborton gulo dekhte parbo na
        }
        process();

        if(e.getActionCommand().equals("Replace All")) {
            replaceAll();
        }
    }

    private void process() {
        if (isReplace) {
            statusInfo.setText("Replacing " + searchText.getText());
        } else {
            statusInfo.setText("Searching For " + searchText.getText());
        }

        /* This is the parameter for passing in to the search method */
        int caret = Notepad.getArea().getCaretPosition();
        String word = getWord();
        String text = getAllText();

        caret = search(text, word, caret);
        if (caret < 0) {
            endResult(false, 0);
        }
    }

    private void endResult(boolean isReplaceAll, int tally) {
        String message = "";
        if (isReplaceAll) {
            if(tally == 0) {
                message = searchText.getText() + " not found";
            } else if(tally == 1) {
                message = "One change was made to " + searchText.getText();
            } else {
                message = "" + tally + " changes were made to " + searchText.getText();
            }
        } else {
            String str = "";
            if (isScarchDown()) {
                str = "Search Down";
            } else {
                str = "Search Up";
            }
            if (foundOne && !isReplace) {
                message = "End of " + str + " for " + searchText.getText();
            } else if (foundOne && isReplace) {
                message = "End of Replace " + searchText.getText() + " with " + replaceText.getText();
            }
        }
        statusInfo.setText(message);
    }

    private int search(String text, String word, int caret) {
        boolean found = false;
        int all = text.length();
        int check = word.length();
        if (isScarchDown()) {
            int add = 0;
            for (int i = caret + 1; i < (all - check); i++) {
                String temp = text.substring(i, (i + check));
                if (temp.equals(word)) {
                    if (wholeWordIsSelected()) {                                /////////////////////////
                        if (checkForWholeWord(check, text, add, caret)) {           /// ekhane text er bodole all deya lagte pare
                            caret = i;
                            found = true;
                            break;
                        }
                    } else { // this is also found bt it is not for whole word
                        caret = i;
                        found = true;
                        break;
                    }
                }
            }
        } else {
            int add = caret;
            for (int i = caret - 1; i >= check; i--) {
                add--;
                String temp = text.substring((i - check), i);
                if (temp.equals(word)) {
                    if (wholeWordIsSelected()) {
                        if (checkForWholeWord(check, text, add, caret)) {
                            caret = i;
                            found = true;
                            break;
                        }
                    } else { // this is also found bt it is not for whole word
                        caret = i;
                        found = true;
                        break;
                    }
                }
            }
        }
        Notepad.getArea().setCaretPosition(0);
        if (found) {
            Notepad.getArea().requestFocus();
             if (isScarchDown()) {
                Notepad.getArea().select(caret, caret + check);
            } else {
                Notepad.getArea().select(caret - check, caret);
            }

            /* For Replace*/
            if(isReplace) {
                String replace = replaceText.getText();
                Notepad.getArea().replaceSelection(replace);

                if(isScarchDown()) {
                    Notepad.getArea().select(caret, caret + replace.length());
                } else {
                    Notepad.getArea().select(caret - replace.length(), caret);
                }
            }


            foundOne = true;
            return caret;
        }
        return -1;
    }


    /* For getting the Text*/
    private String getAllText() {
        if (caseNotSelected()) {
            return Notepad.getArea().getText().toLowerCase();
        }
        return Notepad.getArea().getText();
    }


    /* For getting the word */
    private String getWord() {
        if (caseNotSelected()) {
            return searchText.getText().toLowerCase();
        }
        return searchText.getText();
    }

    // getWord ti case sensetive or case insensetive hote pare, er jonno case er jonno notun method
    private boolean caseNotSelected() {
        return !cbCase.isSelected();
    }

    private boolean isScarchDown() {
        return down.isSelected();
    }

    private boolean wholeWordIsSelected() {
        return cbWhole.isSelected();
    }

    private boolean checkForWholeWord(int check, String text, int add, int caret) {
        int offsetLeft = (caret + add) - 1;
        int offsetRight = (caret + add) + check;
        if ((offsetLeft < 0) || (offsetRight > text.length())) {
            return true;
        }
        return ((!Character.isLetterOrDigit(text.charAt(offsetLeft))) && (!Character.isLetterOrDigit(text.charAt(offsetRight))));

    }

    private void replaceAll() {
        String word = searchText.getText();
        String text = Notepad.getArea().getText();
        String insert = replaceText.getText();

        StringBuffer sb = new StringBuffer(text);
        int diff = insert.length() - word.length();
        int offset = 0;
        int tally = 0;

        for(int i = 0; i <= (text.length() - word.length()); i++) {
            String temp = text.substring(i, i + word.length());

            if((temp.equals(word) && checkForWholeWord(word.length(), text, 0, i)))  {
                tally++;
                sb.replace(i + offset, i + offset + word.length(), insert);
                offset += diff;
            }
        }

        Notepad.getArea().setText(sb.toString());
        endResult(true, tally);
        Notepad.getArea().setCaretPosition(0);
    }
}
