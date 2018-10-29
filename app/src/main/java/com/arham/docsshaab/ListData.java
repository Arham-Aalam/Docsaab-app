package com.arham.docsshaab;

/**
 * Created by HP on 17/08/2018.
 */

public class ListData {
    public String desc_en, desc_hi, image_path, desc_gujj;
    public int _id;

    public ListData(int id, String en, String hi, String gujj) {
        this._id = id;
        this.desc_en = en;
        this.desc_hi = hi;
        this.desc_gujj = gujj;
    }

    public void setImage(String image) {
        this.image_path = image;
    }
}
