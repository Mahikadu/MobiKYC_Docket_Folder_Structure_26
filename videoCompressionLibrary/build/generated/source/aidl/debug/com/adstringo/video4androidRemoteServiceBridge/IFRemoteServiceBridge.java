/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\MobiKYC_Docket_Folder_Structure_26\\videoCompressionLibrary\\src\\main\\aidl\\com\\adstringo\\video4androidRemoteServiceBridge\\IFRemoteServiceBridge.aidl
 */
package com.adstringo.video4androidRemoteServiceBridge;
public interface IFRemoteServiceBridge extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge
{
private static final java.lang.String DESCRIPTOR = "com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge interface,
 * generating a proxy if needed.
 */
public static com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge))) {
return ((com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge)iin);
}
return new com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_runTranscoding:
{
data.enforceInterface(DESCRIPTOR);
this.runTranscoding();
reply.writeNoException();
return true;
}
case TRANSACTION_setSimpleCommand:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setSimpleCommand(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_fexit:
{
data.enforceInterface(DESCRIPTOR);
this.fexit();
reply.writeNoException();
return true;
}
case TRANSACTION_setTranscodingProgress:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setTranscodingProgress(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setNotificationMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setNotificationMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setNotificationTitle:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setNotificationTitle(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setComplexCommand:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _arg0;
_arg0 = data.createStringArray();
this.setComplexCommand(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setWorkingFolder:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setWorkingFolder(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.adstringo.video4androidRemoteServiceBridge.IFRemoteServiceBridge
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void runTranscoding() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_runTranscoding, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setSimpleCommand(java.lang.String command) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(command);
mRemote.transact(Stub.TRANSACTION_setSimpleCommand, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void fexit() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_fexit, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setTranscodingProgress(int transcodingProgress) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(transcodingProgress);
mRemote.transact(Stub.TRANSACTION_setTranscodingProgress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setNotificationMessage(java.lang.String notificationMessage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(notificationMessage);
mRemote.transact(Stub.TRANSACTION_setNotificationMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setNotificationTitle(java.lang.String notificationTitle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(notificationTitle);
mRemote.transact(Stub.TRANSACTION_setNotificationTitle, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setComplexCommand(java.lang.String[] command) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringArray(command);
mRemote.transact(Stub.TRANSACTION_setComplexCommand, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setWorkingFolder(java.lang.String workingFolder) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(workingFolder);
mRemote.transact(Stub.TRANSACTION_setWorkingFolder, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_runTranscoding = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setSimpleCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_fexit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setTranscodingProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_setNotificationMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setNotificationTitle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setComplexCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setWorkingFolder = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
public void runTranscoding() throws android.os.RemoteException;
public void setSimpleCommand(java.lang.String command) throws android.os.RemoteException;
public void fexit() throws android.os.RemoteException;
public void setTranscodingProgress(int transcodingProgress) throws android.os.RemoteException;
public void setNotificationMessage(java.lang.String notificationMessage) throws android.os.RemoteException;
public void setNotificationTitle(java.lang.String notificationTitle) throws android.os.RemoteException;
public void setComplexCommand(java.lang.String[] command) throws android.os.RemoteException;
public void setWorkingFolder(java.lang.String workingFolder) throws android.os.RemoteException;
}
