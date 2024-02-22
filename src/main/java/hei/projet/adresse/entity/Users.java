package hei.projet.adresse.entity;

import hei.projet.adresse.service.UsersService;

public class Users {
    private Integer id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean adminPrivilege;

    private final UsersService usersService = new UsersService();

    //Usual constructor
    public Users(Integer id, String login, String password, String firstName, String lastName, String email, Boolean adminPrivilege) {
        this.id = id;
        this.login = login;
        this.password = usersService.encode(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.adminPrivilege = adminPrivilege;
    }

    //Constructor that DO NOT hash password (used in case of an already hashed password)
    public Users(String willNotHashPassword, Integer id, String login, String password, String firstName, String lastName, String email, Boolean adminPrivilege) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.adminPrivilege = adminPrivilege;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAndHashPassword(String password) {
        this.password = usersService.encode(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdminPrivilege() {
        return adminPrivilege;
    }

    public void setAdminPrivilege(Boolean adminPrivilege) {
        this.adminPrivilege = adminPrivilege;
    }
}
