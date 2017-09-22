package libs;

import android.os.Environment;

/**
 * 
 * @author tushar : interface has all constants for this app
 * 
 */

public interface Constants {

	public static final String CURRENT_LOCATION_MARKER_ID = "CURRENT_LOCATION_MARKER_ID";

	public static final int DEVICE_TYPE_GSM			= 1;
	public static final int DEVICE_TYPE_CDMA		= 2;
	public static final int DEVICE_TYPE_WIFI		= 3;

	public static String rootFolder = Environment.getExternalStorageDirectory()+ "/OrganicParking/";
	public static String userdata_path = rootFolder + "OrganicParkingProfile/";
	public static String derrimut_gym_notification = Environment.getExternalStorageDirectory()+ "/Derrimut_Gym_notification/";

	public static String DerrimuGymNotifyFile = "DerrimuGymNotify.txt";
	public static final String DerrimuGymNotifyFilePath = Constants.derrimut_gym_notification + Constants.DerrimuGymNotifyFile;



	interface VideoRecordingParams{

		public static final int ORIENTATION = 90;
		public static final int RECORDING_DURATION = 900000;
		public static final int VIDEO_WIDTH = 640;
		public static final int VIDEO_HEIGHT = 480;
		public static final int FRAME_RATE = 30;
		public static final int AUDIO_CHANNELS = 1;
		public static final int VIDEOENCODINGBITRATE = 1000000;
		public static final int MAXFILESIZE = 52428800;

	}
	interface VideoCompressionRecordedPath{
		public static final String path = "path";
	}
}
