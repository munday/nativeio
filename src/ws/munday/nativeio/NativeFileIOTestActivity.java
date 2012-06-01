package ws.munday.nativeio;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class NativeFileIOTestActivity extends Activity {
    
	static {
        System.loadLibrary("fileio");
	}
	
	public native void WriteToFile(String filename, byte[] data, long arrayoffset, long fileoffset, long length);
	public native void ReadFromFile(String filename, byte[] data, long arrayoffset, long fileoffset, long length);
	public native void WriteToFileDirect(String filename, ByteBuffer data, long arrayoffset, long fileoffset, long length);
	public native void ReadFromFileDirect(String filename, ByteBuffer data, long arrayoffset, long fileoffset, long length);
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //byte[] bigbyte = new byte[30];
        byte[] refbyte = "HaHa!Oh hello there!".getBytes();
        byte[] refbyte2 = "Why hello there!".getBytes();
        
        ByteBuffer bb1 = ByteBuffer.allocateDirect(refbyte.length);
        ByteBuffer bb2 = ByteBuffer.allocateDirect(refbyte2.length);
        
        bb1.put(refbyte);
        bb2.put(refbyte2);
        
        bb1.flip();
        bb2.flip();
        
        ByteBuffer bb = ByteBuffer.allocate(refbyte.length+refbyte2.length);
        bb.put("YaYa?".getBytes());
        
        WriteToFileDirect( "/sdcard/testfilewrite", bb2 , 0, 20, refbyte2.length );
        WriteToFileDirect( "/sdcard/testfilewrite", bb1 , 0, 0, refbyte.length );
        
        ReadFromFile( "/sdcard/testfilewrite", bb.array(), 5, 5, 300);

        String ref = new String(bb.array());
        Toast.makeText(this, "returned:" + ref, Toast.LENGTH_LONG).show();
       
       
	}
}