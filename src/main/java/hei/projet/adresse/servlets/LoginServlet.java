package hei.projet.adresse.servlets;

import hei.projet.adresse.dao.UsersDao;
import hei.projet.adresse.dao.impl.UsersDaoImpl;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.UsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final Logger LOG = LogManager.getLogger();
    private final UsersService usersService = new UsersService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());
        String error = (String) req.getSession().getAttribute("errorMess");

        if (error != null){
            context.setVariable("error",error);
        }

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(req.getServletContext());
        templateResolver.setPrefix("/WEB-INF/template/");
        templateResolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.process("login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        String loginURL = req.getContextPath().concat("/login");
        String lastURL = (String) req.getSession().getAttribute("lastURL");

        try {
            Users user = usersService.getUserByLogin(login);
            String pass = user.getPassword();
            if (usersService.verifyPassword(pass, password)) {
                HttpSession session = req.getSession(false);

                session.setAttribute("user", login);
                session.setAttribute("admin", user.getAdminPrivilege());

                String errorMess = "";
                req.setAttribute("errorMess", errorMess);

                if (lastURL == null) {
                    resp.sendRedirect("../accueil");
                } else {
                    resp.sendRedirect(lastURL);
                }
            } else {
                LOG.info("Invalid password");
                String errorMess = "Mot de passe invalide.";
                req.getSession().setAttribute("errorMess", errorMess);
                resp.sendRedirect(loginURL);
            }
        } catch (NullPointerException | UserNotFoundException npe) {
            LOG.info("Invalid login");
            String errorMess = "Login introuvable.";
            req.getSession().setAttribute("errorMess", errorMess);
            resp.sendRedirect(loginURL);
        }
    }
}
