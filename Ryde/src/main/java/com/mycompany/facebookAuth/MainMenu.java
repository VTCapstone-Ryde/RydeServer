package com.mycompany.facebookAuth;

import com.mycompany.entity.UserTable;
import java.io.IOException;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainMenu extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String code = "";

    @EJB
    com.mycompany.session.UserTableFacade userFacade;

    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        code = req.getParameter("code");
        if (code == null || code.equals("")) {
            throw new RuntimeException(
                    "ERROR: Didn't get code parameter in callback.");
        }
        FBConnection fbConnection = new FBConnection();
        String accessToken = fbConnection.getAccessToken(code);

        FBGraph fbGraph = new FBGraph(accessToken);
        String graph = fbGraph.getFBGraph();
        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
        ServletOutputStream out = res.getOutputStream();

        /* check if the user already has an account */
        
        String[] name = fbProfileData.get("name").split(" ");
        String first_name = name[0];
        String last_name = name[0];
        String fb_id = fbProfileData.get("id");

        UserTable user = userFacade.findByFbId(fb_id);

        if (user != null) {
           initializeSessionMap(user);
           res.sendRedirect("http://localhost:8080/Ryde/faces/Home.xhtml");
        }
        else {
            res.sendRedirect("http://localhost:8080/Ryde/faces/CreateAccount.xhtml");
        }

//        out.println("<h1>Facebook Login using Java</h1>");
//        out.println("<h2>Application Main Menu</h2>");
//        out.println("<div>Welcome " + fbProfileData.get("name"));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void initializeSessionMap(UserTable user) {
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("first_name", user.getFirstName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("last_name", user.getLastName());
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

}
