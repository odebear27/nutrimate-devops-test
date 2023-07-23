package sg.edu.ntu.nutrimate.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class Address {

    private String block;
    private String streetName;
    private String unitNumber;
    private String buildingName;

    public Address() {
    }

    public Address(String block, String streetName, String unitNumber, String buildingName, String postalCode) {
        this.block = block;
        this.streetName = streetName;
        this.unitNumber = unitNumber;
        this.buildingName = buildingName;
        this.postalCode = postalCode;
    }

    @Size(min = 6, max = 6, message = "Postal Code must be 6 numberical digits")
    private String postalCode;

    public String getBlock() {
        return block;
    }
    public void setBlock(String block) {
        this.block = block;
    }
    public String getStreetName() {
        return streetName;
    }
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    public String getUnitNumber() {
        return unitNumber;
    }
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    public String getBuildinName() {
        return buildingName;
    }
    public void setBuildinName(String buildingName) {
        this.buildingName = buildingName;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }    
    
}
