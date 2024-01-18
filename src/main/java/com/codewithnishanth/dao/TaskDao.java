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

    private static final String UPDATE_TASKS = "UPDATE tasks SET completed = ? WHERE id = ?;";


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

    public void updateTasks(List<Task> taskList) {
        try {
            // Disable autocommit
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASKS)) {
                for (Task task : taskList) {
                    preparedStatement.setBoolean(1, task.isCompleted());
                    preparedStatement.setInt(2, task.getId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            }
        } catch (SQLException e) {

            e.printStackTrace();
            try {
                // Rollback the transaction in case of an exception
                connection.rollback();
            } catch (SQLException rollbackException) {

                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                // Re-enable autocommit
                connection.setAutoCommit(true);
            } catch (SQLException autocommitException) {
                autocommitException.printStackTrace();
            }
        }
    }

    public void clearAllTasks() {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tasks")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}