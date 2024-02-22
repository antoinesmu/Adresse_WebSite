package hei.projet.adresse.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.LoginAlreadyExistingException;
import hei.projet.adresse.service.UsersService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/signin")
public class AddUserServlet extends GenericServlet {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final UsersService usersService = new UsersService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        HttpSession session = req.getSession();

        String error = (String) session.getAttribute("error");
        if (error != null){
            context.setVariable("error",error);
        }

        Boolean admin = (Boolean) session.getAttribute("admin");
        if (admin != null) {
            context.setVariable("admin", admin);
        }

        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("signin", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");

        try {
            Users newUser = new Users(null, login, password, firstName, lastName, email, false);
            Users createdUser = usersService.addUser(newUser);

            resp.sendRedirect(String.format("admin/userpage?id=%d", createdUser.getId()));

        } catch (IllegalArgumentException | LoginAlreadyExistingException e) {
            String errorMess = "";
            if ( e.getClass() == LoginAlreadyExistingException.class){
                errorMess = "Le login existe déjà.";
            } else if ( e.getClass() == IllegalArgumentException.class) {
                errorMess = "Un des champs est invalide.";
            }
            req.getSession().setAttribute("error", errorMess);
            resp.sendRedirect("/signin");
        }
    }
}