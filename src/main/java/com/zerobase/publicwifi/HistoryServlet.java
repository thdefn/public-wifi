package com.zerobase.publicwifi;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/hs.do")
public class HistoryServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    //조회 히스토리 삭제 기능
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("hist_id"));

        //DB에서 삭제 처리하기
        WifiService wifiService = new WifiService();
        wifiService.deleteHistory(id);

        response.setCharacterEncoding("UTF-8");
        request.setAttribute("id",id);

        //table row 삭제 처리를 위해 다른 jsp 내려줌
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/result.jsp");
        dispatcher.forward(request, response);
    }

    public void destroy() {
    }
}