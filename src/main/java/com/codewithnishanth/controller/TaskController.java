package com.codewithnishanth.controller;

import com.codewithnishanth.dao.TaskDao;
import com.codewithnishanth.db.DatabaseConnection;
import com.codewithnishanth.model.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
//        String taskName = request.getParameter("taskName");
//        String taskDescription = request.getParameter("taskDescription");
        String taskComplete = request.getParameter("completedTasks");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String taskName = request.getParameter("taskName");
            String taskDescription = request.getParameter("taskDescription");
            int size = data.size();
            Task task = new Task(0, taskName, taskDescription, false);
            data.add(task);
            request.setAttribute("dataList", null);
//            request.setAttribute("data", data);
        } else if ("save".equals(action)) {
//            taskDao.updateTasks(data);
//            data=taskDao.getAllTasks();
//            data.clear();
            if (!data.isEmpty()) {
                taskDao.clearAllTasks();
//                taskDao.updateTasks(data);
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
                System.out.printf("no tasks to save");
            }
//        } else if ("delete".equals(action)) {
//            String taskId = request.getParameter("taskId");
////            if (taskId != null && !taskId.isEmpty()) {
////                int id = Integer.parseInt(taskId);
////                if (id >= 1 && id <= data.size()) {
////                    data.remove(id - 1);
////                }
////            }
//            taskDao.deleteTask(Integer.parseInt(taskId));
//            data = taskDao.getAllTasks();
//            request.setAttribute("dataList", null);
//        }
        }

        request.setAttribute("taskList", data);
        List<Task> taskList = taskDao.getAllTasks();
        request.setAttribute("task", taskList);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");


        String[] ids = request.getParameterValues("completedTasks");
        System.out.println("ID:"+ids);

        if (ids != null && ids.length > 0) {
            System.out.println(Arrays.asList(ids));
            for (String id : ids) {
                taskDao.updateTaskCompletedStatus(Integer.parseInt(id), true);
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
            String taskId = request.getParameter("taskId");
            taskDao.deleteTask(Integer.parseInt(taskId));

//            request.setAttribute("dataList", null);
        }
        data = taskDao.getAllTasks();
        request.setAttribute("taskList", data);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }




}

