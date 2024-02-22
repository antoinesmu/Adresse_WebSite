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

@WebServlet("/admin/edit")
public class EditRestaurantServlet extends GenericServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());

        Boolean admin = (Boolean) req.getSession().getAttribute("admin");
        if (admin != null) {
            context.setVariable("admin", admin);
        }

        String establishmentId = req.getParameter("id");
        Integer restaurantId = Integer.parseInt(establishmentId);

        Restaurant restaurant = RestaurantsService.getInstance().getRestaurantById(restaurantId);
        context.setVariable("establishment", restaurant);

        HttpSession session = req.getSession();
        session.setAttribute("lastURL", "/detail?id=" + restaurantId);

        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("edit", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = null;
        String name = req.getParameter("name");
        String specialty = req.getParameter("specialty");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        Integer price = null;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException nfe) {
        }
        try {
            price = Integer.parseInt(req.getParameter("price"));
        } catch (NumberFormatException nfe) {
        }
        String moreInfo = req.getParameter("moreInfo");

        try {
            RestaurantsService.getInstance().editRestaurant(RestaurantsService.getInstance().getRestaurantById(id), name, specialty, address, price, moreInfo, phone, website);
            resp.sendRedirect(String.format("../detail?id=%d", id));
        } catch (IllegalArgumentException iae) {
            req.getSession().setAttribute("errorMessage", iae.getMessage());
            resp.sendRedirect("../liste");
        }
    }
}
