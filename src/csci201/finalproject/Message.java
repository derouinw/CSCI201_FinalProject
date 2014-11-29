package csci201.finalproject;

import java.io.Serializable;
import java.util.ArrayList;

// This class holds the data for a single message
// by a simple TVL protocol
//Type: 0 = string, 1 = ArrayList<Shot>, 2 = Board
// Length: int of length of message
// Value: the actual message (be it string or object)
public class Message implements Serializable {
	final static int TYPE_STRING = 0, TYPE_SHOTS = 1, TYPE_BOARD = 2;
	
	int type;
	int length;
	String source;
	Object value;
	
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
	
	public Message(ArrayList<Shot> msg, String username) {
		type = TYPE_SHOTS;
		length = 0;
		value = msg;
		source = username;
	}
	
	public Message(Board msg) {
		type = TYPE_BOARD;
		length = 0;
		value = msg;
	}
}
