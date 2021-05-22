package com.andvilouette.vilouette.DataObject;

import com.google.gson.annotations.SerializedName;


/**
 * Created by aamad on 1/22/2018.
 */

public class Products {
    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    @SerializedName("productID") private String product_id;

    @SerializedName("imageID") private String image_id;

    @SerializedName("image_name") private String image_name;
    @SerializedName("image_url") private String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    @SerializedName("productName") private String product_name;

}
