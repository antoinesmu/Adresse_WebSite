package hei.projet.adresse.entity;

public class Review {
    private Integer id;
    private Integer id_user;
    private Integer id_restaurant;
    private String titre;
    private String champ;
    private Integer note;

    //Usual constructor
    public Review(Integer id, Integer id_user, Integer id_restaurant, String titre, String champ, Integer note) {
        this.id = id;
        this.id_user = id_user;
        this.id_restaurant = id_restaurant;
        this.titre = titre;
        this.champ = champ;
        this.note = note;
    }

    public Review(ReviewWithLogin reviewWithLogin) {
        this.id = reviewWithLogin.getId();
        this.id_user = reviewWithLogin.getId_user();
        this.id_restaurant = reviewWithLogin.getId_restaurant();
        this.titre = reviewWithLogin.getTitre();
        this.champ = reviewWithLogin.getChamp();
        this.note = reviewWithLogin.getNote();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public void setId_restaurant(Integer id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setChamp(String champ) {
        this.champ = champ;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public Integer getId_user() {
        return id_user;
    }

    public Integer getId_restaurant() {
        return id_restaurant;
    }

    public String getTitre() {
        return titre;
    }

    public String getChamp() {
        return champ;
    }

    public Integer getNote() {
        return note;
    }
}
