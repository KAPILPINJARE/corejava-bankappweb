package com.capgemini.bankapp.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="helloWorld",loadOnStartup=1,urlPatterns= {"/login","/helloWorld","/hello"})
public class HelloWorldController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HelloWorldController() {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String username = req.getParameter("username");
    	String password = req.getParameter("password");
    	
    	resp.setContentType("text/html");
    	PrintWriter writer = resp.getWriter();
    	
    	RequestDispatcher dispatcher; 
    	if(username.equals("root") && password.equals("root@123"))
    	{
    		//writer.println("<h2>Login Successful. Welcome " + username + "</h2>");
    		dispatcher = req.getRequestDispatcher("success.html");
    	}
    	else
    	{
    		//writer.println("<h2>Invalid username and password</h2>");
    		dispatcher = req.getRequestDispatcher("error.html");
    	}
    	dispatcher.forward(req, resp);
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter writer = response.getWriter();
		writer.println("kapil");
		response.setContentType("text/html");
		writer.close();
		System.out.println("hello");
	}
}
