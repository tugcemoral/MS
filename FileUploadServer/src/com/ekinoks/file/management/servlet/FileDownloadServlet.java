package com.ekinoks.file.management.servlet;

import static com.ekinoks.file.management.util.FileManagementConstants.*;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class FileDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 3760329151287998252L;

	private static final String ZIP_FILE_NAME = "projeDosyalari.zip";

	private static final int BUFSIZE = 1024;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String[] fileNames = request.getParameterValues("fileName");
		String eiaID = request.getParameter("eiaID");
		String monitoringID = request.getParameter("monitoringID");

		if (fileNames.length == 0) {
			// TODO: check invalid request.
		}
		
		if (fileNames.length == 1) {
			if (eiaID != null && eiaID != "")
				this.downloadSingleFile(request, response,
						URLDecoder.decode(fileNames[0], "UTF-8"), "ced", eiaID);
			else {
				this.downloadSingleFile(request, response,
						URLDecoder.decode(fileNames[0], "UTF-8"), "izleme",
						monitoringID);
			}
			// this.downloadSingleFileRenderAsByteArray(request, response,
			// fileNames[0], fileNames[0], eiaID);
		} else {
			if (eiaID != null && eiaID != "")
				this.downloadZip(request, response, fileNames, "ced", eiaID);
			else
				this.downloadZip(request, response, fileNames, "izleme", monitoringID);
		}

	}

	private void downloadSingleFile(HttpServletRequest request,
			HttpServletResponse response, String fileName,
			String preFolderLocation, String ID) throws IOException {
		// create the file according to eiaID and file name...
		File file = new File(ROOT_REPOSITORY + preFolderLocation
				+ File.separator + ID + File.separator + fileName);

		ServletOutputStream sos = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();
		String mimetype = context.getMimeType(fileName);

		// Set the response and go!
		response.setContentType((mimetype != null) ? mimetype
				: "application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		// Stream to the requester.
		DataInputStream din = new DataInputStream(new FileInputStream(file));

		// write input stream through output stream....
		IOUtils.copy(din, sos);

		// close operations...
		IOUtils.closeQuietly(din);
		sos.flush();
		IOUtils.closeQuietly(sos);
	}

	private void downloadZip(HttpServletRequest request,
			HttpServletResponse response, String[] fileNames,
			String preFolderLocation, String ID) throws IOException {

		// set response properties...
		response.setContentType("application/zip");
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition", "attachment; filename=\""
				+ ZIP_FILE_NAME + "\"");

		OutputStream os = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;

		try {
			// get response's output stream and create zip output stream from
			// it.
			os = response.getOutputStream();
			bos = new BufferedOutputStream(os);

			zos = new ZipOutputStream(bos);
			zos.setLevel(ZipOutputStream.STORED);

			sendMultipleFiles(zos, fileNames, preFolderLocation, ID);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			if (zos != null) {
				zos.finish();
				zos.flush();
				IOUtils.closeQuietly(zos);
			}
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(os);
		}

	}

	private void sendMultipleFiles(ZipOutputStream zos, String[] fileNames,
			String preFolderLocation, String ID) throws IOException {
		for (String fileName : fileNames) {
			// decode file name...
			fileName = URLDecoder.decode(fileName, "UTF-8");
			InputStream inStream = null;
			ZipEntry ze = null;

			// get the current file...
			File file = new File(ROOT_REPOSITORY + preFolderLocation
					+ File.separator + ID + File.separator + fileName);
			try {
				inStream = new FileInputStream(file);
				ze = new ZipEntry(file.getName());
				// ze.setComment("Dummy file");

				// put file to zip.
				zos.putNextEntry(ze);
				// write input stream to zip output stream...
				IOUtils.copy(inStream, zos);
			} catch (IOException e) {
				System.out.println("Cannot find " + file.getAbsolutePath());
			} finally {
				IOUtils.closeQuietly(inStream);
				if (ze != null) {
					zos.closeEntry();
				}
			}
		}
	}

	/**
	 * Sends a file to the ServletResponse output stream. Typically you want the
	 * browser to receive a different name than the name the file has been saved
	 * in your local database, since your local names need to be unique.
	 * 
	 * @param request
	 *            The request
	 * @param response
	 *            The response
	 * @param filename
	 *            The name of the file you want to download.
	 * @param originalFileName
	 *            The name the browser should receive.
	 * @param fileName2
	 */
	@Deprecated
	private void downloadSingleFileRenderAsByteArray(
			HttpServletRequest request, HttpServletResponse response,
			String filename, String originalFileName, String eiaID)
			throws IOException {
		File f = new File(ROOT_REPOSITORY + eiaID + "/" + filename);
		int length = 0;
		ServletOutputStream op = response.getOutputStream();
		ServletContext context = getServletConfig().getServletContext();
		String mimetype = context.getMimeType(filename);

		// Set the response and go!
		response.setContentType((mimetype != null) ? mimetype
				: "application/octet-stream");
		response.setContentLength((int) f.length());
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ originalFileName + "\"");

		// Stream to the requester.
		byte[] bbuf = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(f));

		while ((in != null) && ((length = in.read(bbuf)) != -1)) {
			op.write(bbuf, 0, length);
		}

		in.close();
		op.flush();
		op.close();
	}
}
