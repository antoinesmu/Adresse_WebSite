package hei.projet.adresse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.LoginAlreadyExistingException;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.UsersService;
import org.apache.logging.log4j.LogManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signin/verifyLogin")
public class CheckDoubleLoginController extends HttpServlet {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Users user = null;
        Integer idUser = Integer.parseInt(req.getParameter("id"));
        boolean verif = false;
        try {
            user = UsersService.getInstance().getUserById(idUser);
            verif = UsersService.getInstance().checkSimilarLogin(user);
        } catch (LoginAlreadyExistingException | UserNotFoundException e) {
            LOG.info("Login already exist");
        } finally {
            String reviewsJson = MAPPER.writeValueAsString(verif);
            resp.getWriter().print(reviewsJson);
            resp.setContentType("application/json; utf-8");
        }

    }
}
