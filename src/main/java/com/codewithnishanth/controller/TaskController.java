package com.codewithnishanth.controller;

import com.codewithnishanth.dao.TaskDao;
import com.codewithnishanth.db.DatabaseConnection;
import com.codewithnishanth.model.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TaskController extends HttpServlet {
    private TaskDao taskDao;

    public TaskController() {
        this.taskDao = new TaskDao(DatabaseConnection.getConnection()); // Initialize the TaskDao here
    }

    public TaskController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskName = request.getParameter("taskName");
        String taskDescription = request.getParameter("taskDescription");
        String taskComplete = request.getParameter("completedTasks");
        System.out.println("Task ID: " + taskComplete);
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            taskDao.addTask(taskName, taskDescription);
        } else if ("save".equals(action)) {
            List<Task> taskList = taskDao.getAllTasks();
            for (Task task : taskList) {
                task.setShowActions(true);
            }

            String[] completedTasks = request.getParameterValues("completedTasks");
//            System.out.println(completedTasks);
            taskDao.updateTasks(taskList, completedTasks);
            System.out.println();
            if (completedTasks != null) {
                for (String taskId : completedTasks) {
                    taskDao.updateTaskCompletedStatus(Integer.parseInt(taskId), true);
                }
            }

            String[] deletedTasks = request.getParameterValues("deletedTasks");
            if (deletedTasks != null) {
                for (String taskId : deletedTasks) {
                    taskDao.deleteTask(Integer.parseInt(taskId));
                }
            }
        } else if ("delete".equals(action)) {
            String taskId = request.getParameter("taskId");
            if (taskId != null && !taskId.isEmpty()) {
                taskDao.deleteTask(Integer.parseInt(taskId));
            }
        }
        List<Task> taskList = taskDao.getAllTasks();
        request.setAttribute("taskList", taskList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> taskList = taskDao.getAllTasks();
        request.setAttribute("taskList", taskList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
