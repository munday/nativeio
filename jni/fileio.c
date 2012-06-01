#include <jni.h>
#include <fcntl.h>
//#include <android/log.h>

JNIEXPORT void JNICALL
Java_ws_munday_nativeio_NativeFileIOTestActivity_WriteToFile(JNIEnv * env, jobject  obj, jstring filename, jbyteArray data, jlong arrayoffset, jlong fileoffset, jlong length)
{
 	jlong size = (*env)->GetArrayLength( env, data )-arrayoffset;
	jlong ln=length;
  	
  	if(ln>size)
  		ln = size;
  	
  	jboolean isCopy;
	const char* fn = (*env)->GetStringUTFChars(env, filename, &isCopy);
	
	char * b = (char*) malloc(ln);
	
	(*env)->GetByteArrayRegion(env, data, arrayoffset, ln, b);
	
	size_t filedesc = open(fn, O_RDWR | O_CREAT | O_LARGEFILE);
    
    if(filedesc < 0)
        return;
    
	if(fileoffset>0)
		lseek64(filedesc, fileoffset ,SEEK_SET);
	
	jlong written = write(filedesc, b, ln);	
	  
  	close(filedesc);

	(*env)->ReleaseStringUTFChars(env, filename, fn);

	free(b);
  	
}

JNIEXPORT void JNICALL
Java_ws_munday_nativeio_NativeFileIOTestActivity_ReadFromFile(JNIEnv * env, jobject  obj, jstring filename, jbyteArray data, jlong arrayoffset, jlong fileoffset, jlong length)
{
  
	jlong size = (*env)->GetArrayLength( env, data )-arrayoffset;
	jlong ln = length;
  	
  	if(ln>size)
  		ln = size;
  		
	jboolean isCopy;
	const char* fn = (*env)->GetStringUTFChars(env, filename, &isCopy);
	
	char* b = (char*) malloc(ln);
	
	size_t filedesc = open(fn, O_RDONLY | O_LARGEFILE);
    
    if(filedesc < 0){
    	(*env)->ReleaseStringUTFChars(env, filename, fn);
		free(b);
        return;
    }
    
	if(fileoffset>0)
		lseek64(filedesc, fileoffset, SEEK_SET);
	
	jlong lread = read(filedesc, b, ln);

	close(filedesc);
  		
  	(*env)->SetByteArrayRegion(env, data, arrayoffset, ln, b);

	(*env)->ReleaseStringUTFChars(env, filename, fn);
	
	free(b);
  
}

JNIEXPORT void JNICALL
Java_ws_munday_nativeio_NativeFileIOTestActivity_WriteToFileDirect(JNIEnv * env, jobject  obj, jstring filename, jobject data, jlong arrayoffset, jlong fileoffset, jlong length)
{
  
	jlong ln=length;
  	long size = (*env)->GetDirectBufferCapacity(env, data);
	
  	if(ln>size)
  		ln = size;
  		
	jboolean isCopy;
	const char* fn = (*env)->GetStringUTFChars(env, filename, &isCopy);
	
	char *bb = (*env)->GetDirectBufferAddress(env, data);
	
	jbyte * start = bb+arrayoffset;
	
	size_t filedesc = open(fn, O_RDWR | O_CREAT | O_LARGEFILE);
    
    if(filedesc < 0)
        return;
    
    if(fileoffset>0)
		lseek64(filedesc, fileoffset ,SEEK_SET);
	
	jlong written = write(filedesc, start, ln);	
	
	close(filedesc);

	(*env)->ReleaseStringUTFChars(env, filename, fn);
	
}

JNIEXPORT void JNICALL
Java_ws_munday_nativeio_NativeFileIOTestActivity_ReadFromFileDirect(JNIEnv * env, jobject  obj, jstring filename, jobject data, jlong arrayoffset, jlong fileoffset, jlong length)
{
  
	jlong ln=length;
  	long size = (*env)->GetDirectBufferCapacity(env, data);
	
  	if(ln>size)
  		ln = size;
  	
  	long slen = (*env)->GetStringUTFLength(env,filename); 
  	
  	jboolean isCopy;
	const char* fn = (*env)->GetStringUTFChars(env, filename, &isCopy);

	char * bb = (*env)->GetDirectBufferAddress(env, data);
	
	jbyte * start = bb+arrayoffset;
	
	size_t filedesc = open(fn, O_RDONLY | O_LARGEFILE);
    
    if(filedesc < 0)
        return;
    
	if(fileoffset>0)
		lseek64(filedesc, fileoffset, SEEK_SET);
	
	jlong lread = read(filedesc, start, ln);

	close(filedesc);
  	
  	(*env)->ReleaseStringUTFChars(env, filename, fn);
  	
}
