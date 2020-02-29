package dijkstra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



import dijkstra.Station;

public class ConnectDB {
	public static final String url = "jdbc:MySql://localhost:3306/subway?characterEncoding=utf8";  
    public static final String connectname = "com.mysql.jdbc.Driver";  
    public static final String user = "root";  
    public static final String password = "";  
    
    /*
     * 读取站点信息，存入Station类，用于建立邻接矩阵
     * read the station map and store them in the Station class to build adjacent matrix
     */
    public List<Station> readDB() throws Exception{
    	List<Station> list= new ArrayList<Station>();
    	Class.forName(connectname);
    	Connection con=DriverManager.getConnection(url,user,password);
    	Statement stmt=con.createStatement();
    	ResultSet rs=stmt.executeQuery("select * from subway.station");
    
    	while(rs.next()){
    		int station_id=rs.getInt("station_id");
    		String stationName=rs.getString("stationName");
    		int stationNext1=rs.getInt("stationNext1");
			int stationNext2 = 0;
			int stationNext3 = 0;
			int stationNext4 = 0;
			int stationNext5 = 0;
    		if("0".equals(String.valueOf(rs.getInt("stationNext2")))){
    			stationNext2=station_id;
    		}
    		else{
    			stationNext2=rs.getInt("stationNext2");
    		}
    		if(rs.getInt("stationNext3")==0){
    			stationNext3=station_id;
    		}
    		else{
    			stationNext3=rs.getInt("stationNext3");
    		}
    		if(rs.getInt("stationNext4")==0){
    			stationNext4=station_id;
    		}
    		else{
    			stationNext4=rs.getInt("stationNext4");
    		}
    		if(rs.getInt("stationNext5")==0){
    			stationNext5=station_id;
    		}
    		else{
    			stationNext5=rs.getInt("stationNext5");
    		}
    		String lineLocation1=rs.getString("lineLocation1");
    		String lineLocation2=rs.getString("lineLocation2");
    		String lineLocation3=rs.getString("lineLocation3");
    		
			Station a=new Station(station_id,stationName,stationNext1,
					stationNext2,stationNext3,stationNext4,stationNext5,
					lineLocation1,lineLocation2,lineLocation3);
    		list.add(a);
    	}
    	con.close();
    	return list;	
    }
    
    /*
     * 通过得到路线的站点id，输出路线的站点名
     * By receiving the ID of station in the line, output the name of station
     */
    public String searchStationName(int landInt[][],int n,int endStation) throws Exception{
    	String way="";
    	Class.forName(connectname);
    	Connection con=DriverManager.getConnection(url,user,password);
    	Statement stmt=con.createStatement();
		int first=0;
    	for(int i=0;i<n+1;i++){
    		if(landInt[endStation][i]!=0){
    			String sql="select stationName from subway.station where station_id="+landInt[endStation][i]+";";
    			ResultSet rs=stmt.executeQuery(sql);
    			while(rs.next()){
    				if(first==0){
    					way=rs.getString("stationName");
    					first++;
    				}
    				else{
    					way=way+"->"+rs.getString("stationName");
    				}
    			}
    		}
    	}
    	con.close();
    	return way;
    }
    
    /*
     * 通过得到客户端输入的站点名字，得到相应的站点id
     * Input the station name, achieve the corresponding station name
     */
    public int searchStation_id(String stationName)throws Exception{
    	int station_id=0;
    	Class.forName(connectname);
    	Connection con=DriverManager.getConnection(url,user,password);
    	Statement stmt=con.createStatement();
    	String sql="(SELECT adjacentStation_id FROM subway.attraction WHERE attractionName = '"
    				+stationName+"') UNION (SELECT station_id FROM subway.station WHERE "
    				+"stationName='"+stationName+"');";
    	ResultSet rs=stmt.executeQuery(sql);
    	while(rs.next()){
    		station_id=rs.getInt("adjacentStation_id");
    	}
    	con.close();
    	return station_id;
    }
    
    /*
     * 返回所有中转站id
     * return the ID of all exchange station
     */
    public int[] judgeExchange()throws Exception{
    	int[] station_id=new int[52];
    	Class.forName(connectname);
    	Connection con=DriverManager.getConnection(url,user,password);
    	Statement stmt=con.createStatement();
    	String sql="select station_id from subway.exchangestation;";
    	ResultSet rs=stmt.executeQuery(sql);
    	int i=0;
    	while(rs.next()){
    		station_id[i]=rs.getInt("station_id");
    		i++;
    	}
    	con.close();
    	return station_id;
    }
    
    /*
     * 通过判断换乘站的前一站和后一站所在线路，来判断出经过换乘站时有没有换地铁线
     * judging the line of the exchange station's last and next station can detect 
     *  if the line has changed while pass the exchange station
     */
    public int judgeSameLine(int beforeStation_id,int behindStation_id)throws Exception{
    	int judge=0;
    	String line1=null,line2=null,line3=null,line4=null,line5=null,line6=null;
    	Connection con=DriverManager.getConnection(url,user,password);
    	Statement stmt=con.createStatement();
    	String sql1="select lineLocation1,lineLocation2,lineLocation3 "
    			+ "from subway.station where station_id="
    			+ beforeStation_id+";";
    	ResultSet rs1=stmt.executeQuery(sql1);
    	while(rs1.next()){
    		 line1=rs1.getString("lineLocation1");
    		 line2=rs1.getString("lineLocation2");
    		 line3=rs1.getString("lineLocation3");
    		 if("".equals(rs1.getString("lineLocation2"))){
    			 line2=line1;
    		 }
    		 if("".equals(rs1.getString("lineLocation3"))){
    			 line3=line1;
    		 }
    		 
    	}
    	String sql2="select lineLocation1,lineLocation2,lineLocation3 "
    			+ "from subway.station where station_id="
    			+ behindStation_id+";";
    	ResultSet rs2=stmt.executeQuery(sql2);
    	while(rs2.next()){
    		 line4=rs2.getString("lineLocation1");
    		 line5=rs2.getString("lineLocation2");
    		 line6=rs2.getString("lineLocation3");
    		 if("".equals(rs2.getString("lineLocation2"))){
    			 line5=line4;
    		 }
    		 if("".equals(rs2.getString("lineLocation3"))){
    			 line6=line4;
    		 }
    	}
		if(line1.equals(line4)||line1.equals(line5)||line1.equals(line6)
		 ||line2.equals(line4)||line2.equals(line5)||line2.equals(line6)
		 ||line3.equals(line4)||line3.equals(line5)||line3.equals(line6)){
    		judge=1;
    	}
		
		if(line4==null){
			judge=1;
		}
		con.close();
		return judge;
    }
    
    /*
     * 得到换成路线名字
     * get the name of exchange line name.
     */
    	public String getExchangeLineName(int start,int next) throws SQLException{
    		String way="";
    		String line[]=new String[6];
        	Connection con=DriverManager.getConnection(url,user,password);
        	Statement stmt=con.createStatement();
        	String sql1="select lineLocation1,lineLocation2,lineLocation3 "
        			+ "from subway.station where station_id="
        			+ start+";";
        	ResultSet rs1=stmt.executeQuery(sql1);
        	while(rs1.next()){
        		 line[0]=rs1.getString("lineLocation1");
        		 line[1]=rs1.getString("lineLocation2");
        		 line[2]=rs1.getString("lineLocation3");
        	}
        	String sql2="select lineLocation1,lineLocation2,lineLocation3 "
        			+ "from subway.station where station_id="
        			+ next+";";
        	ResultSet rs2=stmt.executeQuery(sql2);
        	while(rs2.next()){
       		 line[3]=rs2.getString("lineLocation1");
       		 line[4]=rs2.getString("lineLocation2");
       		 line[5]=rs2.getString("lineLocation3");
        	}
        	for(int k=0;k<3;k++){
        		for(int j=3;j<6;j++){
        			if(line[k].equals(line[j])&&!"".equals(line[k])){
        				way=line[k];
        			}
        		}
        	}
        	
    		return way;
    	}
    
}
