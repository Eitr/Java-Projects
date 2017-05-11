package maze;

import java.awt.*;
import java.util.Vector;


public class Model {
	
	public static boolean nameAvailable(String name, RoomModel temp, Vector<RoomModel> rooms){
		for (int i = 0; i<rooms.size(); i++){
			if(rooms.get(i)!=temp && rooms.get(i).name.equals(name)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean canPlay(Vector<RoomModel> rooms){
		
		//Checks whether there is at least 1 portal in each room
		//and that there is a start and finish point for the maze
		boolean can = enoughPortals(rooms);
		boolean start = hasStart(rooms);
		boolean finish = hasFinish(rooms);
		
		return (can && start && finish);
	}
	
	public static RoomModel getEndRoom(Vector<RoomModel> rooms){
		
		for (int i = 0; i< rooms.size(); i++){
			if (rooms.get(i).finish != null) {
				return rooms.get(i);
			}
		}
		return null;
	}
	
	public static RoomModel getStartRoom(Vector<RoomModel> rooms){
		
		for (int i = 0; i< rooms.size(); i++){
			if (rooms.get(i).start != null) {
				return rooms.get(i);
			}
		}
		return null;
	}
	
	public static void setVisitedFalse(Vector<RoomModel> rooms){
		for (int j = 0; j< rooms.size(); j++){
			rooms.get(j).visited = false;
		}
	}

	
	//, Vector<RoomModel> rooms
	public static boolean canBeReached(RoomModel startRoom, RoomModel mod){
		boolean bob;
		bob = false;
		if (startRoom ==mod){
			return true;
		}
		Vector<PortalModel> portals = startRoom.portals;
		startRoom.visited = true;
		RoomModel rm2;
		for (int i = 0; i< portals.size(); i++){
			//rm1 = portals.get(i).room1;//should be startRoom
			rm2 = portals.get(i).room2;
			if(rm2 == mod){
				return true;
			}
			if(rm2.visited){
				continue;
			}
			else{
				
				bob =  (bob || canBeReached(rm2, mod));
			}
		}
		return false || bob;
	}
	
	//Checks to make sure each room has a portal 
	public static boolean enoughPortals(Vector<RoomModel> rooms){
		if(rooms.size() == 1)
			return true;
		boolean can = true;
		for(int i = 0; i< rooms.size(); i++){
			if (rooms.get(i).portals.size()<1)
				can = false;
		}
		return can;
	}
	
	//Checks for a start position
	public static boolean hasStart(Vector<RoomModel> rooms){
		boolean start = false;
		for(int i = 0; i< rooms.size(); i++){
			if(rooms.get(i).start != null)
				start = true;
		}
		return start;
	}
	
	//Checks for a finish position
	public static boolean hasFinish(Vector<RoomModel>  rooms){
		boolean finish = false;
		for(int i = 0; i< rooms.size(); i++){
			if(rooms.get(i).finish != null)
				finish = true;
		}
		return finish;
	}
	
	/**
	 * Steps through the RoomModel and deletes the 
	 * @param room
	 */
	public static void removeNewRooms(Vector<RoomModel> rooms) {
		
		for(int i = 0; i<rooms.size();i++){
			rooms.get(i).cancelPortals();
		}
	}
	
	public static void emptyTempPorts(Vector<RoomModel> rooms){
		for(int i = 0; i < rooms.size(); i++){
			rooms.get(i).tempPorts.removeAllElements();
		}
	}
	
}

class RoomModel{
	int currMinX;
	int currMinY;
	
	//Keep track of how small you can make the room
	int prevXsize;
	int prevYsize;
	Vector<PortalModel> tempPorts;
	boolean hadStart;
	boolean hadFinish;
	
	String name;
	Color clr;
	int sizeX;
	int sizeY;
	Vector<PortalModel> portals;
	int numItems;
	Point start;
	Point finish;
	boolean placed;
	//used for recursive check of reachability
	boolean visited;
	RoomPanel panel;
	
	public RoomModel(String str, Color col, RoomPanel p){
		
		//default
		visited = false;
		name = str;
		clr = col;
		currMinX = 132;
		currMinY = 132;
		
		//MINIMUM SET FOR DEFAULT
		sizeX = 11;
		sizeY = 11;
		
		portals = new Vector<PortalModel>();
		numItems = 1;
		start = null;
		finish = null;
		
		placed = false;
		panel = p;
		
		prevXsize = currMinX;
		prevYsize = currMinY;
		tempPorts = new Vector<PortalModel>();
	}
	
	
	//Mutator methods
	public void setName(String str){
		name = str;
	}
	public void setColor(Color col){
		clr = col;
	}
	public void setItemNum(int num){
		numItems = num;
	}
	
	public String toString() {
		return name;
	}
	public void setSizeX(int x){
		sizeX = x;
	}
	public void setSizeY(int y){
		sizeY = y;
	}
	public void cancelPortals(){
		for(int i = 0; i< tempPorts.size(); i++){
			portals.remove(tempPorts.get(i));
		}
		tempPorts.removeAllElements();
	}
	
	//	public boolean equals(RoomModel mod){
	//		if (name == mod.name)
	//			return true;
	//		
	//		return false;
	//	}
	
	
}

class PortalModel{
	RoomModel room1;
	//	Color clr1;
	int posX1;
	int posY1;
	RoomModel room2;
	//	Color clr2;
	
	
	public boolean equivalent(PortalModel p){
		if(p.room2.equals(room1) && p.room1.equals(room2)){
			return true;
		}
		return false;
	}
	
}