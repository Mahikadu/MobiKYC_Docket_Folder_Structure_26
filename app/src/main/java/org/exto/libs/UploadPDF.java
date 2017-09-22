package org.exto.libs;

import java.io.File;

public class UploadPDF {
	
	static SimpleFTP ftp;
	
	public static void uploadpdf(File file)
	{
		try {
			ftp = new SimpleFTP();
			
			ftp.connect("118.139.173.227", 21, "kycupload", "Kyc@1234");
			
			ftp.bin();
			
			ftp.stor(file);
			
			ftp.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
