<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Tracker Application</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .actions select {
            padding: 6px;
        }
        .actions button {
            padding: 6px 10px;
            margin-left: 5px;
        }
        .completed {
            background-color: #e0e0e0;
        }
    </style>
    <script>
              function toggleDeleteButtonVisibility() {
                         var deleteButton = document.getElementById("deleteButton");
                         var checkbox = document.getElementById("completedTasksCheckbox");

                         // Show the delete button and hide the checkbox
                         deleteButton.style.display = "block";
                         checkbox.style.display = "none";
                     }
        </script>
</head>
<body>
    <h2 class="mt-4">Task App</h2>
    <form id="taskForm" action="TaskController" method="post" onsubmit="toggleDeleteButtonVisibility()">
        <div class="mb-3">
            <label for="dropdown" class="form-label">Select a Task Type:</label>
            <select name="taskName" id="dropdown" class="form-control">
                <option value="">Select Type</option>
                <option value="remainder">remainder</option>
                <option value="todo">todo</option>
                <option value="follow-up">follow-up</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="taskDescription" class="form-label">Enter task description:</label>
            <textarea placeholder="Enter task details ..." name="taskDescription" class="form-control"></textarea>
        </div>
        <button type="submit" name="action" value="add" class="btn btn-primary mb-3">Add</button>
    </form>
       <table id="taskTable" class="table">
           <thead>
               <tr>
                   <th>S.No</th>
                   <th>Task Name</th>
                   <th>Task Description</th>
                   <th>Action</th>
               </tr>
           </thead>
           <tbody>
               <c:forEach var="task" items="${taskList}" varStatus="loop">
                   <!-- Rows for tasks -->

                   <tr class="${task.completed ? 'completed table-success' : ''}">
                       <!-- Task details columns -->
                       <td>${counter+1}</td>
                       <td>${task.taskName}</td>
                       <td>${task.description}</td>
                       <!-- Action column with hidden inputs -->
                       <td>
                       <form action="TaskController" method="get">
                       <c:if test="${task.flagID eq false}">
                          <input type="checkbox" name="completedTasks" value="${task.id}" onchange="this.form.submit()" ${task.completed ? 'checked' : ''}>
                          <label>Completed</label>
                          <input type="hidden" name="taskId" value="${task.id}">
                          <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
                          <!-- Hidden inputs for each task -->
                          <input type="hidden" name="completedTasks_${loop.index}" value="${task.id}">
                          <input type="hidden" name="deletedTasks_${loop.index}" value="${task.id}">
                       </c:if>

                       </form>
                       </td>
                         <c:set var="counter" value="${counter + 1}" scope="page" />
                   </tr>
               </c:forEach>
                <!-- Display details of the added task within the table -->
                        <c:if test="${data != null}">
                            <tr>
                                 <td><c:out value="${dataList.id}" /></td>
                                                   <td><c:out value="${dataList.taskName }"/></td>

                                                   <td><c:out value="${dataList.description} " /></td>
                                                   <td>
                                               <input type="checkbox" name="completedTasks">

                                                <button class="btn btn-danger" type="submit" id="deleteButton" style="display:none;">Delete</button></td>

                                                </tr>

                           </c:if>

           </tbody>
       </table>
       <form action="TaskController" method="post">
       <!-- Save button outside the forEach loop -->
       <button type="submit" name="action" value="save" class="btn btn-primary">Save</button>


    </form>
</body>
</html>