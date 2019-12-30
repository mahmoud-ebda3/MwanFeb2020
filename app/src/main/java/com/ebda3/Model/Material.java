package com.ebda3.Model;

/**
 * Created by work on 23/10/2017.
 */

public class Material
{
    public String MaterialID;
    public String MaterialName;
    public String MaterialImage;

    public Material() {
        MaterialID="";
        MaterialName="";
        MaterialImage="";

    }

    public Material(String materialID, String materialName, String materialImage) {
        MaterialID = materialID;
        MaterialName = materialName;
        MaterialImage = materialImage;
    }

    public String getMaterialID() {
        return MaterialID;
    }

    public void setMaterialID(String materialID) {
        MaterialID = materialID;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getMaterialImage() {
        return MaterialImage;
    }

    public void setMaterialImage(String materialImage) {
        MaterialImage = materialImage;
    }
}
