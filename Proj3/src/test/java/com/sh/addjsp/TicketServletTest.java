package com.sh.addjsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junit.framework.TestCase;

public class TicketServletTest extends Mockito {
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@Mock
	RequestDispatcher dispatcher;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testDoGetCreate() throws ServletException, IOException {
		when(request.getParameter("action")).thenReturn("create");
		when(request.getRequestDispatcher("/WEB-INF/jsp/view/ticketForm.jsp")).thenReturn(dispatcher);
		
		//Response
		StringWriter out = new StringWriter();
		PrintWriter pw = new PrintWriter(out);
		when(response.getWriter()).thenReturn(pw);
		
		TicketServlet ts = new TicketServlet();
		ts.doGet(request, response);
		
		verify(dispatcher).forward(request, response);
		
	}
	
	@Test
	public void testDoGetView() throws IOException, ServletException {
		when(request.getParameter("action")).thenReturn("view");
		when(request.getParameter("ticketId")).thenReturn("1");
		when(request.getRequestDispatcher("/WEB-INF/jsp/view/viewTicket.jsp")).thenReturn(dispatcher);
		
		Ticket ticket = new Ticket();
		ticket.setBody("Test Body");
		ticket.setCustomerName("Amrish");
		ticket.setSubject("Test Subject");
		
		StringWriter out = new StringWriter();
		PrintWriter pw = new PrintWriter(out);
		when(response.getWriter()).thenReturn(pw);
		
		TicketServlet servlet = new TicketServlet();
		servlet.addTicket(ticket);
		servlet.doGet(request, response);
		verify(dispatcher).forward(request, response);
		//TestCase.assertEquals("1", request.getAttribute("ticketId").toString());
	}
	
	public void testDoGetDownload() {
		
	}
	
	public void testDoGetList() {
		
	}
 
}
