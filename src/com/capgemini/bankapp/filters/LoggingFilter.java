package com.capgemini.bankapp.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * Servlet Filter implementation class LoggingFilter
 */
@WebFilter("*.do")
public class LoggingFilter implements Filter {

    
	static final Logger LOGGER = Logger.getLogger(LoggingFilter.class);
    public LoggingFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		long beforeTime = System.currentTimeMillis();
		
		chain.doFilter(request, response);
		
		long afterTime = System.currentTimeMillis();
		long difference = afterTime - beforeTime;
		
		LOGGER.log(Level.INFO,"Time required to processs the request" + difference + "ms");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
