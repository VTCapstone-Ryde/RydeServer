package com.mycompany.facebookAuth;

import com.mycompany.entity.UserTable;
import com.mycompany.managers.LoginManager;
import java.io.IOException;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MainMenu extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String code = "";
    
    @EJB
    com.mycompany.managers.FacebookManager facebookManager;

    @EJB
    com.mycompany.session.UserTableFacade userFacade;

    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        FBConnection fbConnection = new FBConnection();
        HttpSession httpSession = req.getSession();
        code = req.getParameter("code");
        String state = req.getParameter("state");
        String accessToken = fbConnection.getAccessToken(code);
        
        FBGraph fbGraph = new FBGraph(accessToken);
        String graph = fbGraph.getFBGraph();
        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);

        /* check if the user already has an account */
        String[] name = fbProfileData.get("name").split(" ");
        String first_name = name[0];
        String last_name = name[1];
        String fb_id = fbProfileData.get("id");
        
        String sessionID = httpSession.getId();
        if (state.equals(sessionID)) {
            try {
                //do some specific user data operation like saving to DB or login user
                 UserTable user = userFacade.findByFbId(fb_id);
                if (user != null) {
                    user.setFbTok(accessToken);
                    httpSession.putValue("user_id", user.getId());
                    httpSession.putValue("first_name", user.getFirstName());
                    httpSession.putValue("last_name", user.getLastName());
                    httpSession.putValue("access_token", user.getFbTok());
                }
                else {
                    facebookManager.setFbTok(fb_id);
                    facebookManager.setFirstName(first_name);
                    facebookManager.setLastName(last_name);
                    res.sendRedirect(req.getContextPath() + "/faces/CreateAccount.xhtml");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            res.sendRedirect(req.getContextPath() + "/faces/Home.xhtml");
        } else {
            System.err.println("CSRF protection validation");
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
