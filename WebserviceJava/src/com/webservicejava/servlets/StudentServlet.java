package com.webservicejava.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.taskmanager.domain.Task;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet(name = "tasks", urlPatterns = "/tasks/*")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final List<Task> todoList = new ArrayList<Task>();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("aplication/json; charset-UTF=8");
		String pathInfo = request.getPathInfo();
		
		// GET: todo
		if(pathInfo == null || pathInfo.equals("/")){
			doGetAll(request, response);
		    // GET: todo/id
		} else {
			doGetById(request, response, pathInfo);
		}
	}
	
	// GET: todo
	protected void doGetAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		String json = gson.toJson(todoList); response.getWriter().write(json);
	}
	
	// GET: todo/id
	protected void doGetById(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws ServletException, IOException {
		String[] splits = pathInfo.split("/"); if(splits.length != 2) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return; 
	}
	String taskId = splits[1];
	Task task = findTaskById(taskId); 
	if (task == null) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.getWriter().write(""); } 
	else {
		Gson gson = new Gson();
		String json = gson.toJson(task); response.setStatus(HttpServletResponse.SC_OK); response.getWriter().write(json);
		} 
	}

	private Task findTaskById(String taskId) { 
		Optional<Task> result = todoList.stream().filter(task -> task.getId().equals(taskId)) .findFirst();
	if (!result.isPresent()) { 
		return null;
	}
		return result.get(); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("aplication/json; charset-UTF=8"); 
		Gson gson = new Gson();
		Task task = gson.fromJson(request.getReader(), Task.class); 
		todoList.add(task);
		response.setStatus(HttpServletResponse.SC_CREATED);
		response.getWriter().write(gson.toJson(task));
	}
	
	

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("aplication/json; charset-UTF=8");
		String pathInfo = request.getPathInfo(); 
		String[] splits = pathInfo.split("/"); 
		
		if(splits.length != 2) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return; 
		}
		// Get new task from body
		Gson gson = new Gson();
		Task newTask = gson.fromJson(request.getReader(), Task.class);
		
		// Get task from list
		String taskId = splits[1];
		Task task = findTaskById(taskId);
		if (task == null) { 
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); response.getWriter().write("");
			return;
		}
	    //update task
		task.setTitle(newTask.getTitle()); task.setDescrition(newTask.getDescrition()); 
		task.setPriority(newTask.getPriority());
		String json = gson.toJson(task); response.setStatus(HttpServletResponse.SC_OK); 
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("aplication/json; charset-UTF=8");
		String pathInfo = request.getPathInfo(); 
		String[] splits = pathInfo.split("/"); 
		
		if(splits.length != 2) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return; 
		}
		// Get task from list
		
		String taskId = splits[1];
		Task task = findTaskById(taskId);
		if (task == null) { 
			response.setStatus(HttpServletResponse.SC_NOT_FOUND); response.getWriter().write("");
			return;
		}
		
		//Delete task
		todoList.remove(task); response.setStatus(HttpServletResponse.SC_OK); 
		response.getWriter().write("");
	}

}
