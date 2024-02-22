
package hei.projet.adresse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hei.projet.adresse.entity.Users;
import hei.projet.adresse.exception.users.UserNotFoundException;
import hei.projet.adresse.service.UsersService;
import org.apache.logging.log4j.LogManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/users/privilege")
public class PrivilegeController extends HttpServlet {
    private final UsersService usersService = UsersService.getInstance();
    private final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = Integer.parseInt(req.getParameter("id"));
        String nameUser = (String) req.getSession().getAttribute("user");
        Users user = null;
        Users currentUser = null;

        try {
            user = UsersService.getInstance().getUserById(userId);
            currentUser = usersService.getUserByLogin(nameUser);
        } catch (UserNotFoundException e) {
            LOG.info("User not found");
        }
        if (user.getLogin().equals(currentUser.getLogin())) {
            String idCurrentUserJson = MAPPER.writeValueAsString("error");
            resp.getWriter().print(idCurrentUserJson);
            resp.setContentType("application/json; utf-8");
        } else {
            try {
                usersService.changePrivilege(userId);
            } catch (UserNotFoundException e) {
                LOG.info("User not found");
            }
        }
    }
}