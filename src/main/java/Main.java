import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;

class MyListener extends Listener 
{
	// When the leap motion device is connected, enable the tey kapping
	// gesture
	public void onConnect(Controller controller) 
	{
		System.out.println("Connected.");
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}
	
	// Whenever a frame is processed, determine if it's a key tap and play the 
	// sounds accordingly
	public void onFrame(Controller controller) {
		Clip currentMusic;
		Frame frame = controller.frame();
		
		if(frame.gestures().count() > 0) {
			// Get the audio system ready
			try {
				currentMusic = AudioSystem.getClip();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			
			AudioInputStream in = null;
			
			// Determine which finger made the tapping gesture and play the sound
			try {
				KeyTapGesture tap = new KeyTapGesture(frame.gesture(0));
				Pointable point = tap.pointable();
				
				// Problem below: Always thinks the finger is a thumb. Maybe this is the 
				// wrong way to do this? Not too sure at this time.
				Finger finger = new Finger(point);
				Finger.Type fingerType = finger.type();
				//System.out.println(fingerType.toString());
				
				// Try and determine what type of finger it is that made the gesture. It's 
				// always going into the thumb case right now for some reason
				switch(fingerType) {
				case TYPE_THUMB:
					//System.out.println("Thumb");
					in = AudioSystem.getAudioInputStream(new File("D:\\Documents\\programming projects\\VirtualInstrument\\b.wav"));
					break;
				case TYPE_INDEX:
					//System.out.println("Index");
					in = AudioSystem.getAudioInputStream(new File("D:\\Documents\\programming projects\\VirtualInstrument\\c.wav"));
					break;
				case TYPE_MIDDLE:
					//System.out.println("Middle");
					in = AudioSystem.getAudioInputStream(new File("D:\\Documents\\programming projects\\VirtualInstrument\\d.wav"));
					break;
				case TYPE_RING:
					//System.out.println("Ring");
					in = AudioSystem.getAudioInputStream(new File("D:\\Documents\\programming projects\\VirtualInstrument\\e.wav"));
					break;
				case TYPE_PINKY:
					//System.out.println("Pinky");
					in = AudioSystem.getAudioInputStream(new File("D:\\Documents\\programming projects\\VirtualInstrument\\f.wav"));
					break;
				default:
					break;
				}
				
				Clip clip;
				
				// Play the audio clip
				try {
					clip = AudioSystem.getClip();
					clip.open(in);
					clip.start();
				} catch (LineUnavailableException | IOException e) {
					System.out.println("\nError: Sound effect failed.\n");
				}
			} catch (UnsupportedAudioFileException | IOException e) {
				System.out.println("\nError: objFailed sound effect failed to play.\n");
			}
		}
		
	}
}

public class Main 
{

	public static void main(String[] args) 
	{
		MyListener listener = new MyListener();
		Controller controller = new Controller();
		
		controller.addListener(listener);
		
		System.out.println("Press Enter to quit...");
		
		try 
		{
			System.in.read(); 
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		controller.removeListener(listener);
	}

}






