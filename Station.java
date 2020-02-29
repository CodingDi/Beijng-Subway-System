package dijkstra;

/*
 * 把从数据库中读取的每一站的站点信息存入Station这个类里面
 * 然后把每一个Station的类放入一个List里面
 * Put all the station information read from database to the Station class
 * put every Station class in the list
 */
public class Station {
	private int station_id;
	private String stationName;
	private int stationNext1;
	private int stationNext2;
	private int stationNext3;
	private int stationNext4;
	private int stationNext5;
	private String lineLocation1;
	private String lineLocation2;
	private String lineLocation3;
	
	Station(){}
	
	Station(int station_id,String stationName,int stationNext1,int stationNext2,int stationNext3,
			int stationNext4,int stationNext5,String lineLocation1,String lineLocation2,String lineLocation3 ){
		this.station_id=station_id;
		this.stationName=stationName;
		this.stationNext1=stationNext1;
		this.stationNext2=stationNext2;
		this.stationNext3=stationNext3;
		this.stationNext4=stationNext4;
		this.stationNext5=stationNext5;
		this.lineLocation1=lineLocation1;
		this.lineLocation2=lineLocation2;
		this.lineLocation3=lineLocation3;
	}
	
	public int getStation_id(){
		return station_id;
	}
	
	public String getStationName(){
		return stationName;
	}
	
	public int getStationNext1(){
		return stationNext1;
	}
	
	public int getStationNext2(){
		return stationNext2;
	}
	
	public int getStationNext3(){
		return stationNext3;
	}
	
	public int getStationNext4(){
		return stationNext4;
	}
	
	public int getStationNext5(){
		return stationNext5;
	}
	
	public String getLineLocation1(){
		return lineLocation1;
	}
	
	public String getLineLocation2(){
		return lineLocation2;
	}
	
	public String getLineLocation3(){
		return lineLocation3;
	}
}
