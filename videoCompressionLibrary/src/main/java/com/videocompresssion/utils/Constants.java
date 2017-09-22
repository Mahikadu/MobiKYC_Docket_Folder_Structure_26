package com.videocompresssion.utils;

public class Constants {

	public static String COMPRESSED_VIDEO_FILE_PATH = "";

	public class InputFileSizeParams {
		//video is good unable to see watermark
		/*public	static final String RESOLUTION = "320x160";//
		public static final String BITRATE = "300k";//
		public static final int FILE_SIZE = 150000000;
		public static final int FILE_DURATION_SEC = 60;*/

		/*public	static final String RESOLUTION = "320x160";//
		public static final String BITRATE = "200k";//
		public static final int FILE_SIZE_BYTES = 150000000;//150 mb
		public static final int FILE_DURATION_SEC = 60;
		public static final String CODEC = "mpeg4";*/

		/*public	static final String RESOLUTION = "160x120";//
		public static final String BITRATE = "350k";//
		public static final int FILE_SIZE_BYTES = 150000000;//150 mb
		public static final int FILE_DURATION_SEC = 60;
		public static final String CODEC = "mpeg4";
		 */
		//good quality and watermark visible
		/*public	static final String RESOLUTION = "480x360";//
		public static final String BITRATE = "350k";//
		public static final int FILE_SIZE_BYTES = 150000000;//150 mb
		public static final int FILE_DURATION_SEC = 60;
		public static final String CODEC = "mpeg4";*/

		//current
		/*public	static final String RESOLUTION = "320x160";//
		public static final String BITRATE = "250k";//
		public static final int FILE_SIZE_BYTES = 150000000;//150 mb
		public static final int FILE_DURATION_SEC = 60;
		public static final String CODEC = "mpeg4";*/
		
		public	static final String RESOLUTION = "480x360";//
		public static final String BITRATE = "512k";//
		public static final int FILE_SIZE_BYTES = 150000000;//150 mb
		public static final int FILE_DURATION_SEC = 900;
		public static final String CODEC = "mpeg4";
		public static final String FPS = "25";
		//public static final String ASPECT_RATIO = "6:5";

	}

	public class IntentFilterService{

		public static final String INTENT_FILTER = "com.adstringo.video4androidRemoteServiceBridge";
	}


	public class VideoRecordingParams{

		public static final int ORIENTATION = 90;
		public static final int RECORDING_DURATION = 900000;
		public static final int VIDEO_WIDTH = 640;
		public static final int VIDEO_HEIGHT = 480;
		public static final int FRAME_RATE = 30;
		public static final int AUDIO_CHANNELS = 1;
		public static final int VIDEOENCODINGBITRATE = 1000000;
		public static final int MAXFILESIZE = 52428800;

	}
	public class VideoCompressionRecordedPath{
		public static final String path = "path";
	}
}
