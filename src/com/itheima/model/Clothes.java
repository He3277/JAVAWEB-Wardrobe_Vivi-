package com.itheima.model;

import java.math.BigDecimal;
import java.util.List;

public class Clothes {
    private int id;
    private String clothName;
    private String image;
    private int typeId;
    private String typeName;
    private List<Size> sizeList;
    private String style;
    private BigDecimal price;

    public Clothes() {
    }

    public Clothes(int id, String clothName, String image, int typeId, String typeName, String style, BigDecimal price) {
        this.id = id;
        this.clothName = clothName;
        this.image = image;
        this.typeId = typeId;
        this.typeName = typeName;
        this.style = style;
        this.price = price;
    }

    public List<Size> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<Size> sizeList) {
        this.sizeList = sizeList;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClothName() {
        return clothName;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
