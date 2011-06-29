package com.ekinoks.file.management.servlet;

import static com.ekinoks.file.management.util.FileManagementConstants.ROOT_REPOSITORY;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileRemoveServlet extends HttpServlet {

	private static final String SUCCESS = "SUCCESS";

	private static final long serialVersionUID = -8651191781763162126L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// set content type of response.
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		try {
			// get fileName and eiaID parameters to of removal files...
			String[] fileNames = request.getParameterValues("fileName");
			String eiaID = request.getParameter("eiaID");
			String monitoringID = request.getParameter("monitoringID");
			// get recursive deletion flag.
			boolean deleteRecursive = Boolean.valueOf(request
					.getParameter("recursive"));

			String rootPath;
			if (eiaID != null && eiaID != "") {
				rootPath = ROOT_REPOSITORY + "ced" + File.separator + eiaID
						+ File.separator;
			} else {
				rootPath = ROOT_REPOSITORY + "izleme" + File.separator
						+ monitoringID + File.separator;
			}

			if (deleteRecursive) {
				// get the corresponding folder with eia ID and delete rec.
				deleteFolder(new File(rootPath));
			} else {
				if (fileNames.length == 0) {
					// TODO: handle situation.
				} else {
					for (String fileName : fileNames) {
						// get the file according to eia ID and name...
						String firstDecoded = URLDecoder.decode(fileName,
								"UTF-8");
						File file = new File(rootPath
								+ URLDecoder.decode(firstDecoded, "UTF-8"));
						// if file exists, delete it.
						if (file.exists()) {
							file.delete();
						}

						File parentFile = file.getParentFile();
						// check that folder is whether empty or not...
						if (parentFile.isDirectory()) {
							if (parentFile.list().length == 0) {
								parentFile.delete();
							}
						}
					}

					// response success to requester.
					response.getWriter().println(SUCCESS);
				}
			}
		} catch (Exception exc) {
			response.getWriter().println(exc.getMessage());
		}

	}

	private void deleteFolder(File rootFile) {
		if (rootFile.isDirectory()) {
			// get list of the files...
			File[] listFiles = rootFile.listFiles();
			for (File file : listFiles) {
				// delete each file in root.
				deleteFolder(file);
			}
			// finally delete folder...
			rootFile.delete();
		} else {
			rootFile.delete();
		}
	}
}
