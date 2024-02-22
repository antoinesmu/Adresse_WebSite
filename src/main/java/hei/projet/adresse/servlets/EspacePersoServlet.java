package hei.projet.adresse.servlets;

import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.UsersService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/userpage")
public class EspacePersoServlet extends GenericServlet {
    private final UsersService usersService = new UsersService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext context = new WebContext(request, response, request.getServletContext());

        HttpSession session = request.getSession();
        String userLogin = (String) session.getAttribute("user");
        Boolean admin = (Boolean) session.getAttribute("admin");
        if (admin != null) {
            context.setVariable("admin", admin);
        }

        Users user = null;
        try {
            user = UsersService.getInstance().getUserByLogin(userLogin);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        if (user != null) {
            context.setVariable("userList", user);
        }

        //Création de la page
        TemplateEngine templateEngine = createTemplateEngine(request.getServletContext());
        templateEngine.process("userpage", context, response.getWriter());

        List<Users> listOfUsers = UsersService.getInstance().listUsers();
        context.setVariable("userList", listOfUsers);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userLogin = (String) session.getAttribute("user");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String verifyPass = req.getParameter("verifyPassword");

        Users user = null;
        try {
            user = UsersService.getInstance().getUserByLogin(userLogin);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        try {
            usersService.changePassword(user, oldPassword, newPassword);
            resp.sendRedirect("/login");
        } catch (IllegalArgumentException e) {
            resp.sendRedirect("/accueil");
        }

    }

    /* Ajout
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("searchKeyword");
        String url = "search?keyword=".concat(keyword);
        resp.sendRedirect(url);
    }
     */
}

