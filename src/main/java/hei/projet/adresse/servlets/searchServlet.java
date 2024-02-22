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

@WebServlet("/search")
public class searchServlet extends GenericServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebContext context = new WebContext(req, resp, req.getServletContext());

        String keyword = req.getParameter("keyword");

        //Résultats recherche
        List<Restaurant> listOfRestaurant = RestaurantsService.getInstance().searchResult(keyword);
        context.setVariable("resultList", listOfRestaurant);

        HttpSession session = req.getSession();
        Boolean admin = (Boolean) session.getAttribute("admin");
        if (admin != null) {
            context.setVariable("admin", admin);
        }

        //Nombre résultats recherche
        Integer nbrResult = listOfRestaurant.size();
        if (nbrResult < 0) throw new IOException();

        String legende = "";
        if (nbrResult > 1) { //Accord pluriel
            legende = nbrResult.toString().concat(" résultats trouvés");
        } else {
            legende = nbrResult.toString().concat(" résultat trouvé");
        }
        context.setVariable("sizeList", legende);

        //Création de la page
        TemplateEngine templateEngine = createTemplateEngine(req.getServletContext());
        templateEngine.process("search", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("searchKeyword");
        System.out.println(keyword);
        String url = "search?keyword=".concat(keyword);
        resp.sendRedirect(url);
    }
}
