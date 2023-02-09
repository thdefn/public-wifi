package com.zerobase.publicwifi;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hs.do")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("hist_id"));
        WifiService wifiService = new WifiService();
        wifiService.deleteHistory(id);
    }

    public void destroy() {
    }
}