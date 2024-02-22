package hei.projet.adresse.servlets;

import hei.projet.adresse.dao.RestaurantsDao;
import hei.projet.adresse.dao.impl.RestaurantsDaoImpl;
import hei.projet.adresse.entity.Restaurant;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/accueil")
public class AccueilServlet extends GenericServlet {

    private final RestaurantsDao restaurantsDao = new RestaurantsDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext webContext = new WebContext(req, resp, req.getServletContext());

        HttpSession session = req.getSession();
        session.setAttribute("lastURL", "/accueil");

        Boolean admin = (Boolean) session.getAttribute("admin");
        if (admin != null) {
            webContext.setVariable("admin", admin);
        }

        List<Restaurant> listRestaurants = new ArrayList<>();
        Integer[] listId = new Integer[7];
        Restaurant currentRestaurant;
        Integer range = 0;
        Boolean doublon = false;

        do {
            currentRestaurant = restaurantsDao.getRandomRestaurant();
            for (int i = 0; i < listId.length; i++) {
                if (listId[i] == currentRestaurant.getId()) {
                    doublon = true;
                    break;
                }
            }
            if (!doublon) {
                listRestaurants.add(currentRestaurant);
                listId[range] = currentRestaurant.getId();
                range++;
            }
            doublon = false;
        } while (range < 7);

        for (int i = 0; i < listRestaurants.size(); i++) {
            Restaurant restaurant = listRestaurants.get(i);
            Integer restoId = i + 1;
            String restaurantId = restoId.toString();
            webContext.setVariable("image".concat(restaurantId), restaurant.getImage());
            webContext.setVariable("nom".concat(restaurantId), restaurant.getName());
            webContext.setVariable("id".concat(restaurantId), restaurant.getId());
        }

        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("accueil", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("searchKeyword");
        String url = "search?keyword=".concat(keyword);
        resp.sendRedirect(url);
    }
}
