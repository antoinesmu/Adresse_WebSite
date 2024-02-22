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

@WebServlet("/admin/newrestaurant")
public class AddRestaurantServlet extends GenericServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorMessage = (String) req.getSession().getAttribute("errorMessage");
        req.getSession().removeAttribute("errorMessage");

        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("error", errorMessage);

        HttpSession session = req.getSession();
        Boolean admin = (Boolean) session.getAttribute("admin");
        if (admin != null) {
            context.setVariable("admin", admin);
        }

        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("newrestaurant", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String specialty = req.getParameter("specialty");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        Integer price = null;
        String image = null;
        try {
            price = Integer.parseInt(req.getParameter("price"));
        } catch (NumberFormatException nfe) {
        }
        String moreInfo = req.getParameter("moreInfo");

        try {
            Restaurant newRestaurant = new Restaurant(null, name, specialty, address, phone, website, price, moreInfo);
            Restaurant createdRestaurant = RestaurantsService.getInstance().addRestaurant(newRestaurant);
            resp.sendRedirect(String.format("detail?id=%d", createdRestaurant.getId()));
        } catch (IllegalArgumentException iae) {
            req.getSession().setAttribute("errorMessage", iae.getMessage());
            resp.sendRedirect("newrestaurant");
        }
    }
}
