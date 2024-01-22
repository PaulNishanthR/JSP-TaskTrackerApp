package com.codewithnishanth.controller;

import com.codewithnishanth.dao.TaskDao;
import com.codewithnishanth.db.DatabaseConnection;
import com.codewithnishanth.model.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskController extends HttpServlet {
    private TaskDao taskDao;
    private List<Task> data;

    public TaskController() {
        this.taskDao = new TaskDao(DatabaseConnection.getConnection());
        this.data = new ArrayList<>();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskComplete = request.getParameter("completedTasks");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String taskName = request.getParameter("taskName");
            String taskDescription = request.getParameter("taskDescription");
            int size = data.size();
            Task task = new Task(0, taskName, taskDescription, false);
            data.add(task);
            request.setAttribute("dataList", null);
        } else if ("save".equals(action)) {
            if (!data.isEmpty()) {
                taskDao.clearAllTasks();
                for (Task d : data) {
                    taskDao.addTask(d.getTaskName(), d.getDescription());
                }
                data.clear();
                data = taskDao.getAllTasks();
                System.out.println("Tasks saved and retrieved from the database.");
                for (Task task : data) {
                    String completedParam = request.getParameter("completedTasks_" + task.getId());
                    boolean completedTask = completedParam != null && "on".equals(completedParam);
                    task.setCompleted(completedTask);
                }
            } else {
                System.out.println("no tasks to save");
            }
        }
        request.setAttribute("completedTasks", request.getParameterValues("completedTasks"));
        request.setAttribute("taskList", data);
        List<Task> taskList = taskDao.getAllTasks();
        request.setAttribute("task", taskList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        String[] ids = request.getParameterValues("completedTasks");
        System.out.println("ID:" + Arrays.toString(ids));

        if (ids != null && ids.length > 0) {
            System.out.println(Arrays.asList(ids));
            for (String id : ids) {
                if ("on".equals(id)) {
                    System.out.println("Checkbox is xchecked");
                } else {
                    taskDao.updateTaskCompletedStatus(Integer.parseInt(id), true);

                }
            }
            List<String> allIds = taskDao.getAllId();
            allIds.removeAll(Arrays.asList(ids));
            for (String id : allIds) {
                taskDao.updateTaskCompletedStatus(Integer.parseInt(id), false);
            }
        } else {
            List<String> allIds = taskDao.getAllId();
            for (String id : allIds) {
                taskDao.updateTaskCompletedStatus(Integer.parseInt(id), false);
            }
        }
        if ("delete".equals(action)) {
            String taskIdParam = request.getParameter("taskId");
            if (taskIdParam != null && !taskIdParam.isEmpty()) {
                try {
                    int taskId = Integer.parseInt(taskIdParam);
                    taskDao.deleteTask(taskId);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid Task ID format: " + taskIdParam);
                }
            } else {
                System.out.println("Task ID parameter is null or empty.");
            }
        }
        data = taskDao.getAllTasks();
        request.setAttribute("taskList", data);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}


