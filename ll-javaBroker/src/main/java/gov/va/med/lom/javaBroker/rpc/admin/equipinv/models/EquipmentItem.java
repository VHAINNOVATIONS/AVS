/*
 * EquipmentItem.java
 *
 * Created on November 1, 2005, 1:34 PM
 */

package gov.va.med.lom.javaBroker.rpc.admin.equipinv.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;
import java.io.Serializable;

/**
 *
 * @author amccarty
 */
public class EquipmentItem extends BaseBean implements Serializable {
    
    
    private String ee;
    private String sn;
    private String categoryIen;
    private String manufactureIen;
    private String modelIen;
    
    /** Creates a new instance of EquipmentItem */
    public EquipmentItem() {
        setEe("");
        setSn("");
        setCategoryIen("");
        setManufactureIen("");
        setModelIen("");
    }

    public String getEe() {
        return this.ee;
    }

    public void setEe(String ee) {
        this.ee = ee;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCategoryIen() {
        return categoryIen;
    }

    public void setCategoryIen(String categoryIen) {
        this.categoryIen = categoryIen;
    }

    public String getManufactureIen() {
        return manufactureIen;
    }

    public void setManufactureIen(String ManufactureIen) {
        this.manufactureIen = ManufactureIen;
    }

    public String getModelIen() {
        return modelIen;
    }

    public void setModelIen(String ModelIen) {
        this.modelIen = ModelIen;
    }
    
}
