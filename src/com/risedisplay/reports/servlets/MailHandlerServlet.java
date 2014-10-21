package com.risedisplay.reports.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import com.risedisplay.reports.util.CsvUtil;

@SuppressWarnings("serial")
public class MailHandlerServlet extends HttpServlet {
	private Logger LOGGER = Logger.getLogger("MailHandlerServlet");

	// private boolean textIsHtml = false;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Properties props = new Properties();
		Session email = Session.getDefaultInstance(props, null);

		try {
			
			MimeMessage message = new MimeMessage(email, req.getInputStream());
			
			LOGGER.info("********************** Mail Recieved ********************");
			LOGGER.info("From:" + message.getFrom()[0].toString());
			LOGGER.info("Subject:" + message.getSubject());
			LOGGER.info("*********************************************************");
			
			/*if(message.getFrom().toString().equals("asc@risedisplay.com") || 
					message.getFrom().toString().equals("m.farooq2000@gmail.com")) {*/
			
			handleMessage(message);
			
			
			// Address[] addresses = message.getFrom();
			
			// User user = new User(addresses[0].toString(), "gmail.com");
			
			/*}
			else {
				LOGGER.info("Invalid from user.");
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the primary text content of the message.
	 */

	@SuppressWarnings("unused")
	private String getText(Part p) throws MessagingException, IOException {

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}
		return null;
	}

	// handling Simple texts and multipart messages
	public void handleMessage(Message message) throws IOException,
			MessagingException {
		Object content = message.getContent();
		if (content instanceof String) {
			// handle string
			// LOGGER.info( "Description: " + content.toString());
		} else if (content instanceof Multipart) {
			Multipart mp = (Multipart) content;
			handleMultipart(mp);
		}
	}

	public void handleMultipart(Multipart mp) throws IOException,
			MessagingException {
		int count = mp.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bp = mp.getBodyPart(i);
			Object content = bp.getContent();
			if (content instanceof String) {
				// handle string
				LOGGER.info("Description: " + content.toString());
			} else if (content instanceof InputStream) {		// GOT CSV
				// handle input stream
				try {
					// Assume a obj is an instance of
					// com.sun.mail.util.BASE64DecoderStream.
					String messageString = null;
					if (content instanceof InputStream) {
						InputStream is = (InputStream) content;
						messageString = IOUtils.toString(is, "UTF-8");
					}
					CsvUtil.parseCsv(messageString);
					// LOGGER.info( "CSV Description: " + messageString);
				} catch (Exception e) {
					LOGGER.info("Exception while reading csv file: " + e);
				}
			} else if (content instanceof Message) {
				Message message = (Message) content;
				handleMessage(message);
			} else if (content instanceof Multipart) {
				Multipart mp2 = (Multipart) content;
				handleMultipart(mp2);
			}
		}
	}

}
