package hei.projet.adresse.servlets;

import hei.projet.adresse.entity.Users;
import hei.projet.adresse.service.UsersService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class ListUserServlet extends GenericServlet {
    private final UsersService usersService = new UsersService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());

        HttpSession session = req.getSession();
        Boolean admin = (Boolean) session.getAttribute("admin");
        String login = (String) session.getAttribute("user");
        if (admin != null){
            context.setVariable("admin",admin);
        }
        if ( login != null ){
            context.setVariable("currentUserLogin",login);
        }

        List<Users> userList = usersService.listUsers();
        context.setVariable("userList", userList);

        //Création de la page
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("users", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("searchKeyword");
        String url = "search?keyword=".concat(keyword);
        resp.sendRedirect(url);
    }
}
