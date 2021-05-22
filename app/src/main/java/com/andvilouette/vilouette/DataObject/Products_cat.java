package com.andvilouette.vilouette.DataObject;

import com.google.gson.annotations.SerializedName;


/**
 * Created by aamad on 1/22/2018.
 */

public class Products_cat {

    public String getImage_List() {
        return image_List;
    }

    public void setImage_List(String image_List) {
        this.image_List = image_List;
    }

    @SerializedName("imageID") private String image_id;

    @SerializedName("image_name") private String image_name;

    @SerializedName("imagelist") private String image_List;


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
}
