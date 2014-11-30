package csci201.finalproject;

import java.io.Serializable;
import java.util.ArrayList;

// This class holds the data for a single message
// by a simple TVL protocol
//Type: 0 = string, 1 = ArrayList<Shot>, 2 = Board
// Length: int of length of message
// Value: the actual message (be it string or object)
public class Message implements Serializable {
	final static int TYPE_STRING = 0, TYPE_SHOTS = 1, TYPE_BOARD = 2, TYPE_SHOT = 3;
	
	int type;
	int length;
	String source;
	Object value;
	boolean updated;
	
	public Message () {
		type = -1;
		length = -1;
		source = null;
		value = null;
	}
	
	public Message(String msg, String username) {
		type = TYPE_STRING;
		length = msg.length();
		value = msg;
		source = username;
	}
	
	public Message(ArrayList<Shot> msg) {
		type = TYPE_SHOTS;
		value = msg;
	}
	
	public Message(Board msg) {
		type = TYPE_BOARD;
		value = msg;
	}
	
	public Message(Shot s) {
		type = TYPE_SHOT;
		value = s;
		updated = false;
	}
	
	public Message(Shot s, boolean edited) {
		type = TYPE_SHOT;
		value = s;
		updated = edited;
	}
}
