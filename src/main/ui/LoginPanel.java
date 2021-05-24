package ui;

import model.Account;
import model.AccountManager;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/*
    Class which represents the login page when the user runs the program
 */
public class LoginPanel extends JPanel {
    private static final String JSON_SAVE = "./data/saveState.json";
    private static final String JSON_EMPTY = "./data/emptyState.json";

    private AccountManager manager;
    private FashionDesigner fashionDesigner;

    private JTextField accountText;
    private JPasswordField passwordText;
    private JLabel displayLoginMessage;

    // REQUIRES : fashionDesigner != null
    // MODIFIES : this, AccountManager, FashionDesigner
    // EFFECTS  : creates a new login panel and its' components
    public LoginPanel(FashionDesigner fashionDesigner) {
        this.fashionDesigner = fashionDesigner;
        setLayout(null);
        this.manager = AccountManager.getInstance();

        setupLoadButton();
        setupEmptyState();
        setupComponents();
        setupLoginButton();
        setupCreateUserButton();
        setupExitButton(100, 100);
    }

    // MODIFIES : AccountManager, FashionDesigner
    // EFFECTS  : Creates button
    //                  which on click  : Clears the data stored on file, destructs the current instance of manager
    private void setupEmptyState() {
        JButton clearButton = new JButton("Clear Data");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.destruct();
                fashionDesigner.setWorkingState(JSON_EMPTY);
                fashionDesigner.loadData();
            }
        });
        clearButton.setBounds(10, 175, 110, 25);
        add(clearButton);
    }

    // MODIFIES : AccountManager, FashionDesigner
    // EFFECTS  : Creates button which
    //                  on click : Loads the data stored on file
    private void setupLoadButton() {
        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager = AccountManager.getInstance();
                fashionDesigner.setWorkingState(JSON_SAVE);
                fashionDesigner.loadData();
            }
        });
        loadButton.setBounds(275, 175, 100, 25);
        add(loadButton);
    }

    // EFFECTS  : Creates button which on click : exits the program
    private void setupExitButton(int horizontal, int vertical) {
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitButton.setBounds(horizontal, vertical, 80, 25);
        add(exitButton);
    }

    // MODIFIES : FashionDesigner
    // EFFECTS  : Creates button which on click : switches views of panels to the new user panel
    private void setupCreateUserButton() {
        JButton newUserButton = new JButton("Create New Account");
        newUserButton.setBounds(100, 140, 164, 25);
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fashionDesigner.switchPanels("new_user_panel");
                clearText();
            }
        });
        newUserButton.setBackground(Color.gray);
        newUserButton.setForeground(Color.white);
        add(newUserButton);
    }

    // MODIFIES : this
    // EFFECTS  : Clears the fields on the screen
    private void clearText() {
        accountText.setText("");
        passwordText.setText("");
        displayLoginMessage.setText("");
    }

    // MODIFIES : AccountManager, FashionDesigner
    // EFFECTS  : Creates button which : logs user into the main menu
    private void setupLoginButton() {
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(184, 100, 80, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            // MODIFIES : this, FashionDesigner, AccountManager
            // EFFECTS  : If the fields are empty, tell user they can not be empty
            //            If account is a member of the stored accounts
            //                retrieve account and compare the entered password
            //                    if entered password is wrong, tell user wrong password & play rejected audio
            //                    otherwise, take to the main menu
            public void actionPerformed(ActionEvent e) {
                if (accountText.getText().isEmpty() || passwordText.getText().isEmpty()) {
                    displayLoginMessage.setText("Fields can't be blank");
                } else if (manager.isMember(accountText.getText())) {
                    Account currentAccount = manager.getAccount(accountText.getText(), passwordText.getText());
                    if (currentAccount != null) {
                        fashionDesigner.switchFrames(FashionDesigner.FrameType.MAIN, currentAccount);
                    } else {
                        displayLoginMessage.setText("Wrong password");
                        playRejected();
                    }
                } else {
                    displayLoginMessage.setText("User not found");
                }
            }
        });
        add(loginButton);
    }

    // EFFECTS  : Plays rejected audio sample
    private void playRejected() {
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem
                    .getAudioInputStream(new File("./data/audio/login_rejected.wav"));
        } catch (UnsupportedAudioFileException | IOException e) {
            System.out.println("Error in rejected audio file");
        }
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException e) {
            System.out.println("Error in playing rejected audio file");
        }
        clip.start();
    }

    // MODIFIES : this
    // EFFECTS  : Sets up all the visual components of this frame
    private void setupComponents() {
        JLabel accountLabel = new JLabel("Account :");
        accountLabel.setBounds(10, 20, 80, 25);
        add(accountLabel);
        accountText = new JTextField();
        accountText.setBounds(100, 20, 165, 25);
        add(accountText);

        JLabel passwordLabel = new JLabel("Password :");
        passwordLabel.setBounds(10, 60, 80, 25);
        add(passwordLabel);
        passwordText = new JPasswordField();
        passwordText.setBounds(100, 60, 165, 25); // x, y, width, height
        add(passwordText);

        displayLoginMessage = new JLabel("");
        displayLoginMessage.setBounds(130, 180, 300, 25);
        displayLoginMessage.setForeground(Color.red);
        add(displayLoginMessage);
    }
}
