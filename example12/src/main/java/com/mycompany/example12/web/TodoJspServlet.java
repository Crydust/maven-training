package com.mycompany.example12.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "TodoJspServlet", urlPatterns = {TodoJspServlet.URLPATTERN})
public class TodoJspServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public static final String URLPATTERN = "/TodoJspServlet";
    private static final String JSP = "/WEB-INF/jsp/todo.jsp";
    private static final Pattern REMOVE_NAME_PATTERN = Pattern.compile("^remove\\[([-0-9a-fA-F]{36})]$");
    private static final Pattern DONE_NAME_PATTERN = Pattern.compile("^done\\[([-0-9a-fA-F]{36})]$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        TodoList todoList = (TodoList) session.getAttribute("todoList");
        if (todoList == null) {
            todoList = new TodoList();
            session.setAttribute("todoList", todoList);
        }

        request.setAttribute("todoList", todoList);
        forwardToJsp(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            redirectToGet(request, response);
            return;
        }
        TodoList todoList = (TodoList) session.getAttribute("todoList");
        if (todoList == null) {
            redirectToGet(request, response);
            return;
        }
        String button = request.getParameter("button");
        if (button == null) {
            redirectToGet(request, response);
            return;
        }
        if ("add".equals(button)) {
            handleAdd(request, todoList);
        } else if (button.startsWith("remove[")) {
            handleRemove(todoList, button);
        } else if ("save".equals(button)) {
            handleSave(request, todoList);
        }
        session.setAttribute("todoList", todoList);
        redirectToGet(request, response);
    }

    private static void forwardToJsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JSP).forward(request, response);
    }

    private static void redirectToGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + URLPATTERN));
    }

    private void handleAdd(HttpServletRequest request, TodoList todoList) {
        final String label = request.getParameter("label");
        todoList.addItem(new TodoItem(label));
    }

    private static void handleRemove(TodoList todoList, String button) {
        final Matcher matcher = REMOVE_NAME_PATTERN.matcher(button);
        if (matcher.matches()) {
            final String idString = matcher.group(1);
            final UUID id = UUID.fromString(idString);
            todoList.removeById(id);
        }
    }

    private static void handleSave(HttpServletRequest request, TodoList todoList) {
        final List<String> parameterNames = Collections.list(request.getParameterNames());
        parameterNames.stream()
                .filter(name -> name.startsWith("__checkbox_"))
                .map(name -> name.substring("__checkbox_".length()))
                .filter(DONE_NAME_PATTERN.asPredicate())
                .collect(Collectors.toMap(
                        name -> {
                            final Matcher matcher = DONE_NAME_PATTERN.matcher(name);
                            if (matcher.find()) {
                                return UUID.fromString(matcher.group(1));
                            }
                            throw new AssertionError("impossible: the filter prevents non-matches from happening");
                        },
                        parameterNames::contains))
                .forEach(todoList::setDoneById);
    }

}
