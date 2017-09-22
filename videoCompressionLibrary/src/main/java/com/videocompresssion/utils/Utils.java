package com.videocompresssion.utils;

import android.util.Base64;

public class Utils {

	public  String decodeProjectPath(String project){
		// encode data on your side using BASE64
		//String str = "ZmZtcGVn";
		/*byte[]   bytesEncoded = Base64.decode(str.getBytes(),Base64.DEFAULT);
		System.out.println("ecncoded value is " + new String(bytesEncoded ));
		 */
		// Decode data on other side, by processing encoded data
		byte[] valueDecoded= Base64.decode(project.getBytes(),Base64.DEFAULT);
		return new String(valueDecoded);

		/*// Receiving side
		byte[] data = Base64.decode(str, Base64.DEFAULT);
		String text = new String(data, "UTF-8");*/
	}
	
	public static String decodeProjectPathStatic(String project){
		// encode data on your side using BASE64
		//String str = "ZmZtcGVn";
		/*byte[]   bytesEncoded = Base64.decode(str.getBytes(),Base64.DEFAULT);
		System.out.println("ecncoded value is " + new String(bytesEncoded ));
		 */
		// Decode data on other side, by processing encoded data
		byte[] valueDecoded= Base64.decode(project.getBytes(),Base64.DEFAULT);
		return new String(valueDecoded);

		/*// Receiving side
		byte[] data = Base64.decode(str, Base64.DEFAULT);
		String text = new String(data, "UTF-8");*/
	}
}
