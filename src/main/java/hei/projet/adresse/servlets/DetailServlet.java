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

@WebServlet("/detail")
public class DetailServlet extends GenericServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());

        String establishmentId = req.getParameter("id");
        Integer restaurantId = Integer.parseInt(establishmentId);

        Restaurant restaurant = RestaurantsService.getInstance().getRestaurantById(restaurantId);
        context.setVariable("establishment", restaurant);

        HttpSession session = req.getSession();
        session.setAttribute("lastURL", "detail?id=" + restaurantId);
        Boolean admin = (Boolean) session.getAttribute("admin");
        if (admin != null) {
            context.setVariable("admin", admin);
        }

        //Création de la page
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("detail", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("searchKeyword");
        String url = "search?keyword=".concat(keyword);
        resp.sendRedirect(url);
    }
}
