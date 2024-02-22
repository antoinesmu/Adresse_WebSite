package hei.projet.adresse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.UsersService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/detail/reviews/connexion")
public class ConnexionInfoDetail extends HttpServlet {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final UsersService usersService = new UsersService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userLogin = (String) req.getSession().getAttribute("user");
        String connected = "none";

        try {
            if (userLogin != null) {
                Users user = usersService.getUserByLogin(userLogin);
                if (user.getAdminPrivilege()) {
                    connected = "admin";
                } else {
                    connected = "user";
                }
            }
        } catch (UserNotFoundException e) {
            resp.sendRedirect("../login");
        }

        String reviewsJson = MAPPER.writeValueAsString(connected);
        resp.getWriter().print(reviewsJson);
        resp.setContentType("application/json; utf-8");
    }
}
