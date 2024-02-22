package hei.projet.adresse.servlets;

import hei.projet.adresse.entity.Restaurant;
import hei.projet.adresse.service.RestaurantsService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/liste")
public class ListServlet extends GenericServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.setAttribute("lastURL", "/liste");

        WebContext context = new WebContext(req, resp, req.getServletContext());

        Boolean admin = (Boolean) session.getAttribute("admin");
        if (admin != null) {
            context.setVariable("admin", admin);
        }

        List<Restaurant> listOfRestaurant = RestaurantsService.getInstance().listRestaurants();
        context.setVariable("restaurantList", listOfRestaurant);

        //Création de la page
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("liste", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("searchKeyword");
        System.out.println(keyword);
        String url = "search?keyword=".concat(keyword);
        resp.sendRedirect(url);
    }
}
