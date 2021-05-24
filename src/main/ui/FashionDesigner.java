package ui;

import model.Account;
import model.AccountManager;
import persistence.JsonLoadAccounts;
import persistence.JsonSaveAccounts;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
    Launches console GUI class which allows the user to interact with the application. User is
    able to create an account, create a password, manage collections and add or remove items from collections.
 */

public class FashionDesigner {

    private static final String JSON_SAVE = "./data/saveState.json";
    private String workingState = "./data/saveState.json"; // Default is the non empty location

    private JsonLoadAccounts jsonLoader;
    private JsonSaveAccounts jsonSaver;

    private JFrame frame;
    private JPanel panelContainer;
    private CardLayout cardLayout;


    /*
        Class for the different window frames (if we needed more)
     */
    public enum FrameType { LOGIN, MAIN }

    // MODIFIES : this
    // EFFECTS  : Create/ Retrieves an instance of account manager and creates a field for storing user input.
    //            Displays welcome page and launches private run method
    public FashionDesigner() {
        jsonSaver = new JsonSaveAccounts(JSON_SAVE);
        runGUI();
    }

    // MODIFIES : this
    // EFFECTS  : Sets up a GUI login frame which has 2 panels for user to login or create new account
    //            Plays sound upon opening
    private void runGUI() {

        frame = new JFrame();
        panelContainer = new JPanel();
        cardLayout = new CardLayout();

        panelContainer.setLayout(cardLayout);
        panelContainer.add(new LoginPanel(this), "login_panel");
        panelContainer.add(new NewUserPanel(this), "new_user_panel");
        cardLayout.show(panelContainer, "login_panel");
        frame.add(panelContainer);
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            playLogin();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            System.out.println("Error audio file on login");
        }
        frame.setVisible(true);
    }

    // EFFECTS  : Plays a "Please login" audio clip
    private void playLogin() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream audioInputStream = AudioSystem
                .getAudioInputStream(new File("./data/audio/please_login.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    // REQUIRES : identifier != null
    // MODIFIES : this
    // EFFECTS  : Changes the panel which is visible to the user
    public void switchPanels(String identifier) {
        saveState();
        loadData();
        cardLayout.show(panelContainer, identifier);
    }

    // REQUIRES : frame != null , account != null
    // EFFECTS  : switches the frames based on the desired frame (more options could be added)
    public void switchFrames(FrameType frame, Account account) {
        switch (frame) {
            case MAIN:
                mainMenu(account);
                break;
//            case LOGIN:
//                runGUI();
//                break;
        }
    }

    // REQUIRES : account != null
    // MODIFIES : this, MainMenuFrame
    // EFFECTS  : Disposes of the current frame then launches a new frame with user account details in it
    private void mainMenu(Account account) {
        frame.dispose();
        new MainMenuFrame(account);
    }

    // REQUIRES : state != null
    // MODIFIES : this
    // EFFECTS  : sets the directory location of where we want to load/save data from
    //            helps with clearing/loading data
    public void setWorkingState(String state) {
        workingState = state;
    }

    // MODIFIES : this
    // EFFECTS  : loads the user accounts + saved data from file
    public void loadData() {
        jsonLoader = new JsonLoadAccounts(workingState);
        try {
            jsonLoader.loadAccounts();
            System.out.println("Loaded from " + workingState);
        } catch (IOException e) {
            System.out.println("Unable to read from file");
        }
    }

    // EFFECTS  : saves the app state to file, saves accounts and their associated collections + items
    private void saveState() {
        try {
            jsonSaver.open();
            jsonSaver.write(AccountManager.getInstance());
            jsonSaver.close();
            System.out.println("Saved to " + JSON_SAVE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file");
        }
    }
}

