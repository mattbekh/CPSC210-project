package model;

import org.json.JSONObject;
import persistence.Writable;

/*
    Class for an individual item, which keeps track of the item's color, size and type
 */
public class Item implements Writable {

    private ItemColor color;
    private ItemSize size;
    private ItemType itemType;

    /*
        Class for the types of item, protects ItemType
     */
    public enum ItemType { HAT, SHIRT, PANTS }

    /*
        Class for the color of item, protects ItemColor
     */
    public enum ItemColor { BLUE, RED, BROWN }

    /*
        Class for the size of item, protects ItemSize
     */
    public enum ItemSize { SMALL, MEDIUM, LARGE }

    // REQUIRES : type not empty, color not empty, size not empty
    // MODIFIES : this
    // EFFECTS  : Constructs an item with input type, color and size
    public Item(String type, String color, String size) {
        this.itemType = getItemTypeEnum(type);
        this.color = getItemColorEnum(color);
        this.size = getItemSizeEnum(size);
    }

    // REQUIRES : itemType not empty
    // EFFECTS  : Takes input and if input is an element of the enum set, returns that item type
    //            otherwise throws illegalArgumentException

    private ItemType getItemTypeEnum(String itemType) throws IllegalArgumentException {
        switch (itemType.toLowerCase()) {
            case "hat":
                return ItemType.HAT;
            case "shirt":
                return ItemType.SHIRT;
            case "pants":
                return ItemType.PANTS;
            default:
                String errorMessage = String.format("Invalid itemType [%s]", itemType);
                throw new IllegalArgumentException(errorMessage);
        }
    }

    // REQUIRES : itemType not empty
    // EFFECTS  : Takes input and if input is an element of the enum set, returns that item type
    //            otherwise throws illegalArgumentException

    private ItemColor getItemColorEnum(String itemColor) throws IllegalArgumentException {
        switch (itemColor.toLowerCase()) {
            case "blue":
                return ItemColor.BLUE;
            case "red":
                return ItemColor.RED;
            case "brown":
                return ItemColor.BROWN;
            default:
                String errorMessage = String.format("Invalid itemColor [%s]", itemColor);
                throw new IllegalArgumentException(errorMessage);
        }
    }

    // REQUIRES : itemType not empty
    // EFFECTS  : Takes input and if input is an element of the enum set, returns that item type
    //            otherwise throws illegalArgumentException

    private ItemSize getItemSizeEnum(String itemSize) throws IllegalArgumentException {
        switch (itemSize.toLowerCase()) {
            case "small":
                return ItemSize.SMALL;
            case "medium":
                return ItemSize.MEDIUM;
            case "large":
                return ItemSize.LARGE;
            default:
                String errorMessage = String.format("Invalid itemSize [%s]", itemSize);
                throw new IllegalArgumentException(errorMessage);
        }
    }

    // This code is based off : https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Override
    public JSONObject toJson() {
        JSONObject jsonItem = new JSONObject();
        jsonItem.put("size", size.toString());
        jsonItem.put("color", color.toString());
        jsonItem.put("type", itemType.toString());
        return jsonItem;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public ItemColor getItemColor() {
        return color;
    }

    public ItemSize getItemSize() {
        return size;
    }
}
