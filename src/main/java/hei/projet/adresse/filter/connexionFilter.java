package hei.projet.adresse.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class connexionFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        String identifiant = (String) session.getAttribute("user");
        String loginURL = ((HttpServletRequest) request).getContextPath().concat("/login");
        String lastURL = ((HttpServletRequest) request).getRequestURI();

        //Modify lastURL
        session.setAttribute("lastURL",lastURL);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (identifiant == null || "".equals(identifiant)) {
            System.out.println("Il faut être connecté pour accéder à cette page !");

            httpResponse.sendRedirect(loginURL);
            return;
        } else {
            chain.doFilter(request, response);
            return;
        }
    }
}
