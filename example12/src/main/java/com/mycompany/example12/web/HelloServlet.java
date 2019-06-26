package com.mycompany.example12.web;

import static com.mycompany.example12.boilerplate.text.NihStringUtil.escapeHtml4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServlet", urlPatterns = {HelloServlet.URLPATTERN})
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String URLPATTERN = "/HelloServlet";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HelloServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HelloServlet at " + escapeHtml4(response.encodeURL(request.getContextPath() + URLPATTERN)) + "</h1>");
            out.println("<form method='POST' action='" + escapeHtml4(response.encodeURL(request.getContextPath() + URLPATTERN)) + "'>");
            out.println("<p>Hello, I'm HelloServlet.</p>");
            out.println("<p><label for='nickname'>I'm <input type='text' name='nickname' id ='nickname' autofocus='autofocus'/>.</label></p>");
            out.println("<p><button type='submit' id='helloButton'>Pleased to meet you!</button></p>");
            out.println("<p><a href='" + escapeHtml4(response.encodeURL(request.getContextPath() + "/")) + "'>Go back</a>.</p>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HelloServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>Hello <span id='nicknameOutput'>" + escapeHtml4(request.getParameter("nickname")) + "</span>!</p>");
            out.println("<p><a href='" + escapeHtml4(response.encodeURL(request.getContextPath() + "/")) + "'>Go back</a>.</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

}
