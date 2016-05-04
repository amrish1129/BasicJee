package com.sh;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDoGetCreate() throws IOException, ServletException {
		// Mock Request with required fields.
		when(request.getParameter("action")).thenReturn("create");
		
		// Configuring PrintWriter for Response
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		when(response.getWriter()).thenReturn(pw);

		TicketServlet servlet = new TicketServlet();
		servlet.doGet(request, response);
		
		String result =  sw.getBuffer().toString().trim();
		//System.out.println(result);
		TestCase.assertEquals(createTicketForm(writeHeader(response)).trim(), result);
	}
	
	@Test
	public void testDoGetViewForNull() throws IOException, ServletException {
		when(request.getParameter("action")).thenReturn("view");
		when(request.getParameter("ticketId")).thenReturn("1");
		
		
		StringWriter out = new StringWriter();
		PrintWriter pw = new PrintWriter(out);
		when(response.getWriter()).thenReturn(pw);
		
		TicketServlet ts = new TicketServlet();
		ts.doGet(request, response);
		
		String result = out.getBuffer().toString().trim();
		
		TestCase.assertEquals("", result);
	}
	
	@Test
	public void testDoGetView() throws IOException, ServletException {
		when(request.getParameter("action")).thenReturn("view");
		when(request.getParameter("ticketId")).thenReturn("1");
		
		TicketServlet servlet = new TicketServlet();
		Ticket ticket = new Ticket();
		ticket.setCustomerName("Amrish");
		ticket.setSubject("Test Subject");
		ticket.setBody("Test Body");
		servlet.addTicket(ticket);
		
		StringWriter out = new StringWriter();
		PrintWriter pw = new PrintWriter(out);
		when(response.getWriter()).thenReturn(pw);
		
		servlet.doGet(request, response);
		
		String result = out.getBuffer().toString().trim();
		
		String expected = createViewPage(writeHeader(response), "1", ticket).trim();
		
		TestCase.assertEquals(expected, result);
		
	}
	
	public void testDoGetDownload() {
		
	}
	
	public void testDoGetList() {
		
	}
	
	private StringBuilder writeHeader(HttpServletResponse response) throws ServletException, IOException {
		when(response.getContentType()).thenReturn("text/html");
		when(response.getCharacterEncoding()).thenReturn("UTF-8");

		StringBuilder writer = new StringBuilder();
		writer.append("<!DOCTYPE html>\r\n").append("<html>\r\n").append("    <head>\r\n")
				.append("        <title>Customer Support</title>\r\n").append("    </head>\r\n")
				.append("    <body>\r\n");

		return writer;
	}
 	
	private String createTicketForm(StringBuilder writer) {
		
		writer.append("<h2>Create a Ticket</h2>\r\n");
		writer.append("<form method=\"POST\" action=\"tickets\" ").append("enctype=\"multipart/form-data\">\r\n");
		writer.append("<input type=\"hidden\" name=\"action\" ").append("value=\"create\"/>\r\n");
		writer.append("Your Name<br/>\r\n");
		writer.append("<input type=\"text\" name=\"customerName\"/><br/><br/>\r\n");
		writer.append("Subject<br/>\r\n");
		writer.append("<input type=\"text\" name=\"subject\"/><br/><br/>\r\n");
		writer.append("Body<br/>\r\n");
		writer.append("<textarea name=\"body\" rows=\"5\" cols=\"30\">").append("</textarea><br/><br/>\r\n");
		writer.append("<b>Attachments</b><br/>\r\n");
		writer.append("<input type=\"file\" name=\"file1\"/><br/><br/>\r\n");
		writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
		writer.append("</form>\r\n");
		writer.append("    </body>\r\n").append("</html>\r\n");

		return writer.toString();
	}
	
	private String createViewPage(StringBuilder writer, String idString, Ticket ticket) {
		writer.append("<h2>Ticket #").append(idString).append(": ").append(ticket.getSubject()).append("</h2>\r\n");
		writer.append("<i>Customer Name - ").append(ticket.getCustomerName()).append("</i><br/><br/>\r\n");
		writer.append(ticket.getBody()).append("<br/><br/>\r\n");

		if (ticket.getNumberOfAttachments() > 0) {
			writer.append("Attachments: ");
			int i = 0;
			for (Attachment attachment : ticket.getAttachments()) {
				if (i++ > 0)
					writer.append(", ");
				writer.append("<a href=\"tickets?action=download&ticketId=").append(idString).append("&attachment=")
						.append(attachment.getName()).append("\">").append(attachment.getName()).append("</a>");
			}
			writer.append("<br/><br/>\r\n");
		}

		writer.append("<a href=\"tickets\">Return to list tickets</a>\r\n");
		
		writer.append("    </body>\r\n").append("</html>\r\n");
		return writer.toString();
	}

	public void testDoPost() {

	}

}
