package com.adstringo.video4androidRemoteServiceBridge;

interface IFRemoteServiceBridge {

	
	void runTranscoding();
	void setSimpleCommand(String command);
	void fexit();
	void setTranscodingProgress(int transcodingProgress);
	void setNotificationMessage(String notificationMessage);
	void setNotificationTitle(String notificationTitle);
	void setComplexCommand(in String[] command);
	void setWorkingFolder(String workingFolder);
	
}