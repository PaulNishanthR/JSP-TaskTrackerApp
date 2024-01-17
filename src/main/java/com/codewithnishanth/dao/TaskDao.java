package com.codewithnishanth.dao;

import com.codewithnishanth.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskDao {
    private static final String SELECT_ALL_TASKS = "SELECT * FROM tasks;";
    private final Connection connection;
    private final String INSERT_TASK = "INSERT INTO tasks (task_name, description, completed) VALUES (?, ?, false);";

    private static final String DELETE_TASK = "DELETE FROM tasks WHERE id = ?;";

    private static final String UPDATE_TASK_COMPLETED_STATUS = "UPDATE tasks SET completed = ? WHERE id = ?;";

    private static final String UPDATE_TASKS = "UPDATE tasks SET showActions = ?, completed = ? WHERE id = ?;";

    public TaskDao(Connection connection) {
        this.connection = connection;
    }

    public void addTask(String taskName, String description) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK);
            preparedStatement.setString(1, taskName);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TASKS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getInt("id"));
                task.setTaskName(resultSet.getString("task_name"));
                task.setDescription(resultSet.getString("description"));
                task.setCompleted(resultSet.getBoolean("completed"));
                task.setShowActions(false); // Set initial value
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public void updateTaskCompletedStatus(int taskId, boolean completed) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK_COMPLETED_STATUS)) {
            preparedStatement.setBoolean(1, completed);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int taskId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void updateTasks(List<Task> taskList, String[] completedTasks) {
//        try {
//            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASKS)) {
//                for (Task task : taskList) {
//                    if (completedTasks != null) {
//                        task.setShowActions(Arrays.asList(completedTasks).contains(String.valueOf(task.getId())));
//                    } else {
//                        task.setShowActions(false); // Set default value if completedTasks is null
//                    }
//
//                    preparedStatement.setBoolean(1, task.isShowActions());
//                    preparedStatement.setInt(2, task.getId());
//                    preparedStatement.addBatch();
//                }
//                preparedStatement.executeBatch();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void updateTasks(List<Task> taskList, String[] completedTasks) {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASKS)) {
                for (Task task : taskList) {
                    boolean isCompleted = Arrays.asList(completedTasks).contains(String.valueOf(task.getId()));
                    task.setShowActions(isCompleted);

                    // Set completed status in the task object
                    task.setCompleted(isCompleted);

                    preparedStatement.setBoolean(1, task.isShowActions());
                    preparedStatement.setBoolean(2, task.isCompleted());  // Add this line to update the completed column
                    preparedStatement.setInt(3, task.getId());
                    preparedStatement.addBatch();
                }
                // After executeBatch(), add a commit statement
                preparedStatement.executeBatch();
                // Set auto-commit to true
                connection.setAutoCommit(true);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
