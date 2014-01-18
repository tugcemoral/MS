package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileWriterUtils {
	
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd_HH:mm:ss";

	public static final String RESOURCE = "/resource/";
	
	public static final String STATE_FILES = RESOURCE + "states/";

	public static final String G_RHS_DIFF_FILES = RESOURCE + "g_rhs_diffs/";
	
	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}


}
