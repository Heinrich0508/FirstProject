package heinrich.petar.hr.pretvaranjezvukautext;

import java.util.ArrayList;

public class Products {
    private Integer id;
    private String rootProduct;
    private String fullNameOfProduct;
    private String quantity;
    private Integer imageOfProduct;
    //todo new
    private String priceOfProduct;
    private Integer imageCheckedItem;
    private String measure;
    //todo new -end


    public Products(Integer id, String rootProduct, String fullNameOfProduct, String quantity, Integer imageOfProduct, String priceOfProduct, Integer imageCheckedItem,String measure) {
        this.id = id;
        this.rootProduct = rootProduct;
        this.fullNameOfProduct = fullNameOfProduct;
        this.quantity = quantity;
        this.imageOfProduct = imageOfProduct;
        this.priceOfProduct = priceOfProduct;
        this.imageCheckedItem = imageCheckedItem;
        this.measure = measure;
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
    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }


}