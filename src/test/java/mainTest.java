import hei.projet.adresse.service.UsersService;

public class mainTest {
    public static void main(String[] args) {
        UsersService us = new UsersService();
        us.encode("root");
        us.encode("toor");
    }
}
