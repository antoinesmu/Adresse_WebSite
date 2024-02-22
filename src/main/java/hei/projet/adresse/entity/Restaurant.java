package hei.projet.adresse.entity;

public class Restaurant {
    private Integer id;
    private String name;
    private String specialty;
    private String address;
    private String phone;
    private String website;
    private Integer price;
    private String moreInfo;
    private String image;
    private String carte;

    //Constructor that initialize the review
    public Restaurant(Integer id, String name, String specialty, String address, String phone, String website, Integer price, String moreInfo) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.price = price;
        this.moreInfo = moreInfo;
        this.image = null;
        this.carte = null;

    }

    //Constructor for establishment which already have reviews
    public Restaurant(Integer id, String name, String specialty, String address, String phone, String website, Integer price, String moreInfo, String image, String carte) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.price = price;
        this.moreInfo = moreInfo;
        this.image = image;
        this.carte = carte;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCarte() {
        return carte;
    }

    public void setCarte(String carte) {
        this.carte = carte;
    }

}
