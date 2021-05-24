package ui;

import model.Account;
import model.AccountManager;
import model.Collection;
import model.Item;
import persistence.JsonSaveAccounts;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/*
    Class which represents the Main Menu frame of an account after login
 */
public class MainMenuFrame extends JFrame {

    private JsonSaveAccounts jsonSaver;
    private static final String JSON_SAVE = "./data/saveState.json";

    private final AccountManager manager;
    private final Account account;
    private JList<Collection> collectionJList = new JList<>();
    private DefaultListModel<Collection> collectionModel = new DefaultListModel<>();

    private JPanel centerPanel = new JPanel();
    private JPanel centerPanelTop = new JPanel();
    private JPanel centerPanelBot = new JPanel();

    private String itemName;
    private String itemColor;
    private String itemSize;

    // REQUIRES : account != null
    // MODIFIES : this, AccountManager, JsonLoadAccounts, JsonSaveAccounts
    // EFFECTS  : creates a new main menu frame and its' components, plays audio upon construction
    public MainMenuFrame(Account account) {
        this.account = account;
        manager = AccountManager.getInstance();
        jsonSaver = new JsonSaveAccounts(JSON_SAVE);

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        try {
            playAuthorized();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.out.println("Error audio file on login");
        }
        setupBanner();
        setupLeftPanel();
        setupCenterPanel();
        setupBottomPanel();

        setVisible(true);
    }

    // EFFECTS  : Plays authorized audio sample
    private void playAuthorized() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem
                .getAudioInputStream(new File("./data/audio/login_authorized.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    // MODIFIES : JsonSaveAccounts
    // EFFECTS  : saves the app state to file, saves accounts and their associated collections + items
    private void saveState() {
        try {
            jsonSaver.open();
            jsonSaver.write(manager);
            jsonSaver.close();
            System.out.println("Saved to " + JSON_SAVE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file");
        }
    }

    // EFFECTS  : Sets up the componenets for hte bottom panel of the frame
    private void setupBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        JButton addColButton = new JButton("Add Collection");
        setupAddColButton(addColButton);

        JButton removeColButton = new JButton("Remove Collection");
        setupRemoveColButton(removeColButton);

        bottomPanel.add(addColButton);
        bottomPanel.add(removeColButton);

        bottomPanel.setBackground(Color.red);
        add(bottomPanel, "South");
    }

    // REQUIRES : addItem != null, selectedCollection != null
    // EFFECTS  : Sets up a button which launches a new window to create an item
    private void setupAddItemButton(JButton addItem, Collection selectedCollection) {
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createItemWindow(selectedCollection);
            }
        });
    }

    // REQUIRES : selectedCollection != null
    // MODIFIES : this
    // EFFECTS  : Allows user to choose options for a new custom item they want to create, sets up the
    //            necessary components
    private void createItemWindow(Collection selectedCollection) {
        itemSize = "";
        itemColor = "";
        itemName = "";
        JFrame createItemFrame = new JFrame();
        createItemFrame.setSize(300, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JLabel label = new JLabel("Create New Item ");
        panel.add(label);

        addComboBoxes(panel);

        JButton addButton = createAddButton(createItemFrame, selectedCollection);
        JButton cancelButton = createCancelButton(createItemFrame);
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(cancelButton);

        panel.add(bottomPanel);

        createItemFrame.add(panel);
        createItemFrame.setVisible(true);
        createItemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // REQUIRES : selectedCollection != null
    // MODIFIES : this, Collection
    // EFFECTS  : Create a button for adding a new item to the selected collection
    private JButton createAddButton(JFrame createItemFrame, Collection selectedCollection) {
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            if (!itemName.isEmpty() && !itemColor.isEmpty() && !itemSize.isEmpty()) {
                selectedCollection.addItem(new Item(itemName, itemColor, itemSize));
                createItemFrame.dispose();
                updateUI();
            }
        });
        return addButton;
    }

    // REQUIRES : createItemFrame != null
    // EFFECTS  : Sets up a button which closes the current frame
    private JButton createCancelButton(JFrame createItemFrame) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createItemFrame.dispose();
            }
        });
        return cancelButton;
    }

    // REQUIRES : panel != null
    // MODIFIES : this
    // EFFECTS  : Sets up the ComboBoxes which hold the selections for item type, size and color the user
    //            can choose from
    private void addComboBoxes(JPanel panel) {
        String[] itemChoiceList = {"", "Shirt", "Pants", "Hat"};
        JComboBox itemChoices = new JComboBox(itemChoiceList);
        addActionListenerToComboBox(itemChoices, 1);
        panel.add(itemChoices);
        String[] itemColorChoiceList = {"", "Blue", "Red", "Brown"};
        JComboBox itemColorChoices = new JComboBox(itemColorChoiceList);
        addActionListenerToComboBox(itemColorChoices, 2);
        panel.add(itemColorChoices);
        String[] itemSizeChoiceList = {"", "Small", "Medium", "Large"};
        JComboBox itemSizeChoices = new JComboBox(itemSizeChoiceList);
        addActionListenerToComboBox(itemSizeChoices, 3);
        panel.add(itemSizeChoices);
    }

    // REQUIRES : itemChoices != null, identifier != null
    // MODIFIES : this
    // EFFECTS  : sets up an action listener for a ComboBox, which stores the selected option in corresponding
    //            field
    private void addActionListenerToComboBox(JComboBox itemChoices, int identifier) {
        switch (identifier) {
            case 1:
                itemChoices.addActionListener(e -> {
                    if (!itemChoices.getSelectedItem().toString().isEmpty()) {
                        this.itemName = (String) itemChoices.getSelectedItem();
                    }
                });
                break;
            case 2:
                itemChoices.addActionListener(e -> {
                    if (!itemChoices.getSelectedItem().toString().isEmpty()) {
                        this.itemColor = (String) itemChoices.getSelectedItem();
                    }
                });
                break;
            case 3:
                itemChoices.addActionListener(e -> {
                    if (!itemChoices.getSelectedItem().toString().isEmpty()) {
                        this.itemSize = (String) itemChoices.getSelectedItem();
                    }
                });
                break;
        }
    }

    // REQUIRES : removeColbutton != null
    // MODIFIES : this, Collection, Account
    // EFFECTS  : setsup a button which removes a collection from account and updates UI
    private void setupRemoveColButton(JButton removeColButton) {
        removeColButton.addActionListener(e -> {
            if (collectionJList.getSelectedIndex() != -1) {
                Collection selectedCollection = collectionJList.getSelectedValue();
                collectionModel.remove(collectionJList.getSelectedIndex());
                account.removeCollection(selectedCollection);
                updateUI();
            }
        });
    }

    // REQUIRES : colButton != null
    // EFFECTS  : sets up a button which launches a new window with options for making a new collection
    private void setupAddColButton(JButton colButton) {
        colButton.addActionListener(e -> {
            createCollectionWindow();
        });
    }

    // MODIFIES : this
    // EFFECTS  : creates a new frame with components for creating a new collection
    private void createCollectionWindow() {
        JFrame createColFrame = new JFrame();
        createColFrame.setSize(300, 150);
        createColFrame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter Collection Name : ");

        JTextField collectionName = new JTextField();
        collectionName.setColumns(10);

        JPanel topPanel = new JPanel();
        topPanel.add(label);
        topPanel.add(collectionName);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> createColFrame.dispose());

        JButton createButton = createCreateButton(createColFrame, collectionName);
        createColFrame.add(topPanel);

        createColFrame.add(cancelButton);
        createColFrame.add(createButton);
        createColFrame.setVisible(true);
        createColFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // REQUIRES : createColFrame != null, collectionName != null
    // MODIFIES : this, Collection, Account
    // EFFECTS  : based on user input, creates a new collection and adds it to the account, then updates UI
    private JButton createCreateButton(JFrame createColFrame, JTextField collectionName) {
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            if (!collectionName.getText().isEmpty()) {
                account.addCollection(new Collection(collectionName.getText()));
                createColFrame.dispose();
                updateUI();
            }
        });
        return createButton;
    }

    // MODIFIES : this
    // EFFECTS  : Sets up the components for the left panel of the frame
    private void setupLeftPanel() {
        JSplitPane splitPane = new JSplitPane();

        collectionJList.setModel(collectionModel);
        for (Collection collection : account.getCollections()) {
            collectionModel.addElement(collection);
        }

        collectionJList.getSelectionModel().addListSelectionListener(e -> {
            try {
                populateCenterPanel(collectionJList.getSelectedValue());
            } catch (IOException ioException) {
                System.out.println("Item Image location is null");
            }

        });
        JScrollPane scrollPane = new JScrollPane(collectionJList);
        scrollPane.setPreferredSize(new Dimension(150, 400));
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(null);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(new JLabel("COLLECTIONS"));
        content.add(splitPane);
        add(content, "West");
    }

    // REQUIRES : selectedCollection != null
    // MODIFIES : this
    // EFFECTS  : fills out the center panel with the data found in the selected collection
    private void populateCenterPanel(Collection selectedCollection) throws IOException {
        centerPanelTop.removeAll();
        if (selectedCollection != null && !selectedCollection.getItems().isEmpty()) {
            int numItems = selectedCollection.getItems().size();
            int numRows = numItems % 3 + 1;
            centerPanelTop.setLayout(new GridLayout(numRows, 1));
            for (Item item : selectedCollection.getItems()) {
                BufferedImage itemPicture = getItemImage(item);
                JLabel picLabel = new JLabel(new ImageIcon(itemPicture));
                centerPanelTop.add(picLabel);
            }
        }
        centerPanelBot.removeAll();
        JButton addItem = new JButton("Add Item");
        setupAddItemButton(addItem, selectedCollection);
        centerPanelBot.add(addItem);
        JButton removeItem = new JButton("Remove Item");
        setupRemoveItemButton(removeItem, selectedCollection);
        centerPanelBot.add(removeItem);

        revalidate();
        repaint();

    }

    // REQUIRES : removeItem != null, selectedCollection != null
    // EFFECTS  : adds an actionlistener to the removeItem button
    private void setupRemoveItemButton(JButton removeItem, Collection selectedCollection) {
        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCollection != null) {
                    removeItemFrame(selectedCollection);
                }
            }
        });
    }

    // REQUIRES : selectedCollection != null
    // MODIFIES : this
    // EFFECTS  : Creates a list of buttons corresponding to the items stored in the collection (in order)
    private void removeItemFrame(Collection selectedCollection) {
        JFrame removeItemFrame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        int index = 0;
        for (Item item : selectedCollection.getItems()) {
            panel.add(makeItemButton(item, index, selectedCollection, removeItemFrame));
            index++;
        }
        JScrollPane scroll = new JScrollPane(panel);
        removeItemFrame.setContentPane(scroll);
        removeItemFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        removeItemFrame.setSize(200, 200);
        removeItemFrame.setVisible(true);
    }

    // REQUIRES : item, index, selectedCollection, removeItemFrame != null
    // MODIFIES : this, Collection
    // EFFECTS  : Each button corresponds to an item in the collection, sets up an action listner which on click
    //           removes item from collection and disposes the frame
    private JButton makeItemButton(Item item, int index, Collection selectedCollection, JFrame removeItemFrame) {
        JButton itemButton = new JButton(item.getItemColor().toString() + " " + item.getItemType().toString());
        itemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCollection.removeItem(index);
                removeItemFrame.dispose();
                updateUI();
            }
        });
        return itemButton;
    }

    // EFFECTS  : Collects and returns a corresponding image from file for each item based on item name and color
    private BufferedImage getItemImage(Item item) throws IOException {
        String pathname = "./data/images/"
                + item.getItemType().toString().toLowerCase() + "_"
                + item.getItemColor().toString().toLowerCase() + "_128.jpg";
        return ImageIO.read(new File(pathname));
    }

    // MODIFIES : this
    // EFFECTS  : Sets up the components for the center panel of the frame
    private void setupCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanelTop = new JPanel();
        centerPanelTop.setBackground(Color.white);
        centerPanelBot = new JPanel();

        centerPanelBot.setBackground(Color.pink);

//        centerPanel.add(null,"Center");
        centerPanel.add(centerPanelTop, "Center");
        centerPanel.add(centerPanelBot, "South");
//        centerPanel.setBackground(Color.cyan);
        add(centerPanel, "Center");
    }

    // MODIFIES : this
    // EFFECTS  : Sets up the components for the top panel of the frame
    private void setupBanner() {
        JPanel banner = new JPanel();
        banner.setLayout(new FlowLayout(FlowLayout.RIGHT));


        banner.setBackground(Color.ORANGE);
        JLabel bannerName = new JLabel("CURRENT USER : " + account.getName().toUpperCase());
        JButton logoutButton = new JButton("LOGOUT");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toSave();
//                new FashionDesigner();
//                dispose();
            }
        });

        banner.add(bannerName);
        banner.add(logoutButton);
        add(banner, "North");
    }

    // MODIFIES : this, FashionDesigner
    // EFFECTS  : Creates a new frame which asks user whether they would like to save what they have done
    //            during their session
    private void toSave() {
        JFrame popup = new JFrame();
        popup.setLayout(new GridLayout());
        popup.setSize(200, 75);
        JLabel label = new JLabel("Save?");
        JButton yes = new JButton("Yes");
        yes.addActionListener(e -> {
            saveState();
            popup.dispose();
            new FashionDesigner();
            dispose();
        });
        JButton no = new JButton("No");
        no.addActionListener(e -> {
            popup.dispose();
            new FashionDesigner();
            dispose();
        });
        popup.add(label);
        popup.add(yes);
        popup.add(no);
        popup.setVisible(true);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // MODIFIES : this
    // EFFECTS  : Refreshes the view of the program to update the changes user has made
    private void updateUI() {
        collectionModel.removeAllElements();
        if (!account.getCollections().isEmpty()) {
            for (Collection collection : account.getCollections()) {
                collectionModel.addElement(collection);
            }
        }
        if (collectionModel.isEmpty()) {
            centerPanelTop.removeAll();
            centerPanelBot.removeAll();
        }
        revalidate();
        repaint();
    }
}
