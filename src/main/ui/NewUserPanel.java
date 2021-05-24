package ui;

import model.Account;
import model.AccountManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Class which represents the create new user page when the user wants to create a new account
 */
public class NewUserPanel extends JPanel {

    private AccountManager manager;
    private FashionDesigner fashionDesigner;

    private JTextField accountText;
    private JPasswordField passwordText;
    private JLabel displayLoginMessage;


    // REQUIRES : fashionDesigner != null
    // MODIFIES : this, AccountManager, FashionDesigner
    // EFFECTS  : creates a new create user panel and its' components
    public NewUserPanel(FashionDesigner fashionDesigner) {
        this.fashionDesigner = fashionDesigner;
        setLayout(null);
        this.manager = AccountManager.getInstance();
        setupComponents();
        setupCreateButton();
        setupBackButton();
        setupExitButton();
    }

    // EFFECTS  : Creates button which on click : exits the program
    private void setupExitButton() {
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitButton.setBounds(35, 100, 80, 25);
        add(exitButton);
    }

    // EFFECTS  : Creates button which on click : switches the panel back to the login menu
    private void setupBackButton() {
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
                fashionDesigner.switchPanels("login_panel");
            }
        });
        backButton.setBounds(120, 100, 80, 25);
        add(backButton);
    }

    // EFFECTS  : Creates a button responsible for creating user
    private void setupCreateButton() {
        JButton createButton = new JButton("Create User");
        createButton.setBounds(205, 100, 120, 25);
        createButton.addActionListener(new ActionListener() {
            @Override
            // MODIFIES : this, AccountManager, FashionDesigner
            // EFFECTS  : If the fields are empty, tells user fields can't be empty
            //                  Otherwise checks if the user account name already exists
            //                  if it does, tells user that account already exists
            //                  otherwise, creates a new account with desired name and password and stores in file
            //                      then takes user to the login page
            public void actionPerformed(ActionEvent e) {
                if (accountText.getText().isEmpty() || passwordText.getText().isEmpty()) {
                    displayLoginMessage.setText("Fields can't be empty.");
                } else if (manager.isMember(accountText.getText())) {
                    displayLoginMessage.setText("User already exists.");
                } else {
                    Account newAccount = new Account(accountText.getText(),passwordText.getText());
                    manager.addAccount(newAccount);
                    clearText();
                    fashionDesigner.switchPanels("login_panel");
                }
            }
        });
        add(createButton);
    }

    // MODIFIES : this
    // EFFECTS  : Clears the fields on the screen
    private void clearText() {
        accountText.setText("");
        passwordText.setText("");
        displayLoginMessage.setText("");
    }

    // MODIFIES : this
    // EFFECTS  : Sets up all the visual components of this frame
    private void setupComponents() {
        JLabel accountLabel = new JLabel("Desired Name :");
        accountLabel.setBounds(10, 20, 140, 25);
        add(accountLabel);
        accountText = new JTextField();
        accountText.setBounds(160, 20, 165, 25);
        add(accountText);

        JLabel passwordLabel = new JLabel("Desired Password :");
        passwordLabel.setBounds(10, 60, 140, 25);
        add(passwordLabel);
        passwordText = new JPasswordField();
        passwordText.setBounds(160, 60, 165, 25); // x, y, width, height
        add(passwordText);

        displayLoginMessage = new JLabel("");
        displayLoginMessage.setBounds(10,140,300,25);
        displayLoginMessage.setForeground(Color.red);
        add(displayLoginMessage);
    }
}
