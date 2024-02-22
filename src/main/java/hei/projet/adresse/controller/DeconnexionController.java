package hei.projet.adresse.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/deconnexion")
public class DeconnexionController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* On supprime l'utilisateur connecte de la session  */
        req.getSession().removeAttribute("user");
        req.getSession().setAttribute("admin", false);
        /* On redirige vers la page de connexion */
        resp.sendRedirect("accueil");
    }

}
