package com.zerobase.publicwifi;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hs.do")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("hist_id"));
        WifiService wifiService = new WifiService();
        wifiService.deleteHistory(id);

        response.setCharacterEncoding("UTF-8");
        request.setAttribute("id",id);

        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/result.jsp");
        dispatcher.forward(request, response);
    }

    public void destroy() {
    }
}