package com.mycompany.facebookAuth;

import com.mycompany.entity.UserTable;
import com.mycompany.session.UserTableFacade;
import java.io.IOException;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MainMenu extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String code = "";

    @EJB
    private UserTableFacade userFacade;

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
                    System.out.println(user.getFirstName());
                    httpSession.putValue("user_id", user.getId());
                    httpSession.putValue("first_name", user.getFirstName());
                    httpSession.putValue("last_name", user.getLastName());
                    res.sendRedirect(req.getContextPath() + "/faces/Home.xhtml");
                }
                else {
                    System.out.println("Create Account!");
                    httpSession.putValue("first_name", first_name);
                    httpSession.putValue("last_name", last_name);
                    httpSession.putValue("fb_id", fb_id);
                    httpSession.putValue("access_token", fb_id);
                    res.sendRedirect(req.getContextPath() + "/faces/CreateAccount.xhtml");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
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
