package ws.munday.nativeio;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
        
        TextView tv = (TextView) findViewById(R.id.text);
        String out = "";
        
        //byte[] bigbyte = new byte[30];
        byte[] refbyte = "HaHa!Oh hello there!".getBytes();
        byte[] refbyte2 = "Why hello there!".getBytes();
        
        ByteBuffer bb1 = ByteBuffer.allocateDirect(refbyte.length);
        ByteBuffer bb2 = ByteBuffer.allocateDirect(refbyte2.length);
        
        bb1.put(refbyte);
        out += "created ByteBuffer : 'HaHa!Oh hello there!' \n\n";
        
        bb2.put(refbyte2);
        out += "created ByteBuffer : 'Why hello there!' \n\n";

        bb1.flip();
        bb2.flip();
        
        ByteBuffer bb = ByteBuffer.allocate(refbyte.length+refbyte2.length);
        bb.put("YaYa?".getBytes());
        out += "created ByteBuffer : 'YaYa?' \n\n";

        WriteToFileDirect( "/sdcard/testfilewrite", bb2 , 0, 20, refbyte2.length );
        out += "wrote: 'Why hello there!' to /sdcard/testfilewrite at the 20th byte. \n\n";
        
        
        WriteToFileDirect( "/sdcard/testfilewrite", bb1 , 0, 0, refbyte.length );
        out += "wrote: 'HaHa!Oh hello there!' to /sdcard/testfilewrite at the begining (0th) byte. \n\n";
        
        ReadFromFile( "/sdcard/testfilewrite", bb.array(), 5, 5, 300);
        out += "read: 'HaHa!Oh hello there!' starting at 5th byte from /sdcard/testfilewrite into 'YaYa?' ByteBuffer. \n\n Result:";
        
        String ref = new String(bb.array());
       
        tv.setText(out + ref);
       
	}
}