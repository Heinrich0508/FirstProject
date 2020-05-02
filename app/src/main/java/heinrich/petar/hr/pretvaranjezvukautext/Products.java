package heinrich.petar.hr.pretvaranjezvukautext;

import java.util.ArrayList;

public class Products {
    private Integer id;
    private String rootProduct;
    private String fullNameOfProduct;
    private String quantity;
    private Integer imageOfProduct;
    //todo new
    private String crossedFullNameOfProduct;
    private String priceOfProduct;
    private Integer imageCheckedItem;
    //todo new -end


    public Products(Integer id, String rootProduct, String fullNameOfProduct, String quantity, Integer imageOfProduct, String crossedFullNameOfProduct, String priceOfProduct, Integer imageCheckedItem) {
        this.id = id;
        this.rootProduct = rootProduct;
        this.fullNameOfProduct = fullNameOfProduct;
        this.quantity = quantity;
        this.imageOfProduct = imageOfProduct;
        this.crossedFullNameOfProduct = crossedFullNameOfProduct;
        this.priceOfProduct = priceOfProduct;
        this.imageCheckedItem = imageCheckedItem;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRootProduct() {
        return rootProduct;
    }

    public void setRootProduct(String rootProduct) {
        this.rootProduct = rootProduct;
    }

    public String getFullNameOfProduct() {
        return fullNameOfProduct;
    }

    public void setFullNameOfProduct(String fullNameOfProduct) {
        this.fullNameOfProduct = fullNameOfProduct;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getImageOfProduct() {
        return imageOfProduct;
    }

    public void setImageOfProduct(Integer imageOfProduct) {
        this.imageOfProduct = imageOfProduct;
    }


    //todo new items
    public String getCrossedFullNameOfProduct() {
        return crossedFullNameOfProduct;
    }

    public void setCrossedFullNameOfProduct(String crossedFullNameOfProduct) {
        this.crossedFullNameOfProduct = crossedFullNameOfProduct;
    }

    public String getPriceOfProduct() {
        return priceOfProduct;
    }

    public void setPriceOfProduct(String priceOfProduct) {
        this.priceOfProduct = priceOfProduct;
    }

    public Integer getImageCheckedItem() {
        return imageCheckedItem;
    }

    public void setImageCheckedItem(Integer imageCheckedItem) {
        this.imageCheckedItem = imageCheckedItem;
    }

}