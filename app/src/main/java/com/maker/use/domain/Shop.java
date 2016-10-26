package com.maker.use.domain;

import android.net.Uri;

/**
 * Created by XISEVEN on 2016/10/23.
 */

public class Shop {
    private Uri imgUri;
    private String name;
    private String description;

    public Shop(String name, String descrition, Uri imgUri) {
        this.name = name;
        this.description = descrition;
        this.imgUri = imgUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descrition) {
        this.description = descrition;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUrl) {
        this.imgUri = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
