package com.risedisplay.reports.queue;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.risedisplay.reports.QueryParam;
import com.risedisplay.reports.queue.QueueTask;
import com.risedisplay.reports.util.CsvUtil;

public class QueueServlet extends HttpServlet {

	private static final long serialVersionUID = -1306790628538809438L;
	
	private Logger log = Logger.getAnonymousLogger();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)	throws IOException {

		String task = req.getParameter(QueryParam.TASK);

		if (task == null || task.isEmpty()) {
			log.severe("Task is not supplied, exiting.");
			return;
		}

		try {

			if (task.equals(QueueTask.ENQUEUE)) {
				
				String taskName = req.getParameter(QueryParam.TASK_NAME);
				
				QueueFactory.getDefaultQueue().add(withUrl("/queue")
						.param(QueryParam.TASK, taskName)
						.method(Method.GET));
				

			} else if (task.equals(QueueTask.GET_QUOTELIST)) {
				
				String codes = req.getParameter(QueryParam.SYMBOL_CODES);
				
				URLCodec decoder = new URLCodec();
				CsvUtil.Execute(decoder.decode(codes));
				
				
			} else {

				log.warning("Task " + task + " is not recognized, exiting.");
				return;
			}

		} catch (Exception e) {
			log.severe("Error: " + e.toString());


			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}

}
