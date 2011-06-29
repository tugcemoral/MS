package com.ekinoks.file.management.servlet;

import static com.ekinoks.file.management.util.FileManagementConstants.ROOT_REPOSITORY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 2054384276242006847L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// parse the request and extract the sent data to save to file system...
		parseRequest(request, response);
	}

	public void parseRequest(HttpServletRequest request,
			HttpServletResponse response) {

		// Initialization for chunk management.
		boolean bLastChunk = false;
		int numChunk = 0;

		response.setContentType("text/plain");

		try {
			// Get URL Parameters.
			Enumeration<?> paraNames = request.getParameterNames();

			while (paraNames.hasMoreElements()) {
				String pname = (String) paraNames.nextElement();
				String pvalue = request.getParameter(pname);

				if (pname.equals("jufinal")) {
					bLastChunk = pvalue.equals("1");
				} else if (pname.equals("jupart")) {
					numChunk = Integer.parseInt(pvalue);
				}
			}
			int ourMaxMemorySize = 10000000;
			int ourMaxRequestSize = 2000000000;

			/*
			 * The code below is directly taken from the jakarta fileupload
			 * common classes All informations, and download, available here :
			 * http://jakarta.apache.org/commons/fileupload/
			 */
			// create the repository folder...
			File repository = new File(ROOT_REPOSITORY);
			if (!repository.exists()) {
				repository.mkdir();
			}

			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold(ourMaxMemorySize);
			factory.setRepository(repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Set overall request size constraint
			upload.setSizeMax(ourMaxRequestSize);

			// Parse the request
			if (!request.getContentType().startsWith("multipart/form-data")) {
				print(response, "No parsing of uploaded file: content type is "
						+ request.getContentType());
			} else {
				List<?> items = upload.parseRequest(request);
				// Process the uploaded items
				Iterator<?> iter = items.iterator();
				// traverse the sent data iterator.
				while (iter.hasNext()) {
					// get next file item...
					FileItem fileItem = (FileItem) iter.next();
					// if this file item is not a form field, save it to file
					// system.
					if (!fileItem.isFormField()) {
						saveFile(request, fileItem, ROOT_REPOSITORY, numChunk,
								bLastChunk);
					}
				}
			}

			Thread.sleep(250);
			print(response, "SUCCESS");
		} catch (Exception e) {
			print(response, "Exception Thrown: " + e.toString());
		}
	}

	private void print(HttpServletResponse response, String message) {
		try {
			response.getWriter().println(message);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Ok, we've got a file. Let's process it. Again, for all informations of
	 * what is exactly a FileItem, please have a look to
	 * http://jakarta.apache.org/commons/fileupload/
	 */
	private void saveFile(HttpServletRequest request, FileItem fileItem,
			String rootUploadDirectory, int numChunk, boolean bLastChunk)
			throws Exception {
		// the uploaded file name.
		String uploadedFilename = fileItem.getName()
				+ (numChunk > 0 ? ".part" + numChunk : "");

		// get the eiaID | monitoring parameter.
		String moduleID;
		boolean isMonitoringID = false;
		// attempt to get monitoringID...
		moduleID = request.getParameter("monitoringID");
		if (moduleID == null || moduleID == "") {
			moduleID = request.getParameter("eiaID") != null ? request
					.getParameter("eiaID") : "0";
			isMonitoringID = false;
		}else {
			isMonitoringID = true;
		}
		
		// get the root specific upload directory...
		File rootSpecificUploadDirectory = new File(rootUploadDirectory
				+ (isMonitoringID ? "izleme" : "ced") + File.separator
				+ moduleID);
		// if this directory not exists, create it...
		if (!rootSpecificUploadDirectory.exists()) {
			rootSpecificUploadDirectory.mkdir();
		}

		// create the file to write.
		File uploadedFile = new File(rootSpecificUploadDirectory
				+ File.separator + (new File(uploadedFilename)).getName());
		// write the file
		fileItem.write(uploadedFile);

		// Chunk management: if it was the last chunk, let's
		// recover the complete file
		// by concatenating all chunk parts.
		//
		if (bLastChunk) {
			// First: construct the final filename.
			FileInputStream fis;
			FileOutputStream fos = new FileOutputStream(uploadedFile.getPath()
					+ fileItem.getName());
			int nbBytes;
			byte[] byteBuff = new byte[1024];
			String filename;
			for (int i = 1; i <= numChunk; i += 1) {
				filename = fileItem.getName() + ".part" + i;
				// System.out.println("[parseRequest.jsp] "
				// + "  Concatenating " + filename);
				fis = new FileInputStream(uploadedFile.getPath() + filename);
				while ((nbBytes = fis.read(byteBuff)) >= 0) {
					// out.println("[parseRequest.jsp] " +
					// "     Nb bytes read: " + nbBytes);
					fos.write(byteBuff, 0, nbBytes);
				}
				fis.close();
			}
			fos.flush();
			fos.close();
		}
		// End of chunk management
		fileItem.delete();
	}

}
