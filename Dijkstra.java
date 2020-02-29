package dijkstra;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dijkstra {
	    //n 节点总数（地铁站总个数） s 起始站点id e 终点站点id
		//n:total number of nodes or stations s: start station id e:end station id
	/*
	 * 得到包括起点和终点和中间路过站点的站点id
	 * obtaining the ID of start point,end point and intermediate station
	 */
	public int[][] getWayID(String startStationName) throws Exception{ 

	        ConnectDB con=new ConnectDB();
	        
	    	int s = con.searchStation_id(startStationName);//起始点  id
	    	 /*
	    	  * 建立邻接矩阵，来进行dijkstra算法 
	    	  * build adjacent matrix, then calculate by Dijkstra algorithm 
	    	  */
	    	BuildPath builder=new BuildPath();
	    	int n=builder.getListSize();//得到地铁站的个数
	    	int[][] path=new int[n+1][n+1];
	    	path=builder.buildPath();//通过BuildPath获得邻接矩阵
	    	
	    	/*
	    	 * 利用邻接矩阵进行dijkstra算法
	    	 * Appling Dijkstra algorithm to adjacent matrix
	    	 */
	        int S[] = new int[n+1];//集合S         
	        int T[] = new int[n+1];//集合T         //初始化集合S、T， 
	        for(int i=0; i<n+1; i++){ 
	            S[i] = 0;             
	            T[i] = i;         
	            } 
	        S[s] = s;         
	        T[s] = 0;  
	        
	        //初始化路径，land用于记录并输出路径 
	        //initiating the path,land was used to record and output the path
	        String[] land = new String[n+1]; 
	        for(int i=1; i<n+1; i++){             
	        	land[i] = "" + s;
	        	} 
	        land[s] = s + "->" + s;  
	        
	        
	        //Beginning of loop 循环开始 
	        int t = 0;
	        while(++t <= n){
	        	//calculating the shortest path from every point in T to that in S
	        	//计算T中各点到S中各点的距离的最小值 
	            int min = Integer.MAX_VALUE; 
	            int min_x = 0;             
	            int min_y = 0; 
	            for(int i=1; i<n+1; i++){                 
	            	if(S[i] != 0){ //find the starting point 找到起始点
	                    for(int j=1; j<n+1; j++){ 
	                        if(T[j] != 0 && path[S[i]][T[j]] != Integer.MAX_VALUE  && path[s][S[i]] + path[S[i]][T[j]] < min){
	                        	//判断条件：如果T集合里有点，且点S[i]与这个点的距离不为无穷大，且从起始点到S[i]的距离 加上 S[i]到该点的距离 还小于现有最小值   
	                        	//judge condition: If there's any point in T,
	                        	//and the distance from s[i] to is is not infinite,
	                        	//and the distance from starting point to s[i] plus the distance from s[i] to it 
	                        	//is smaller than the current value	                           
	                        	min = path[s][S[i]] + path[S[i]][T[j]];                             
	                        	min_x = S[i];                             
	                        	min_y = T[j];                         
	                        	}                     
	                        }                 
	                    }             
	            	}  
	                        
	            //save the shortest path 保存最短路径 
	            path[s][min_y] = min;                    //weighted path 路径权值 
	            if(min_x != s){//if min_x is not the starting point  min_x不是起始点，即从起始点到该点要经过中间点 
	                land[min_y] = land[min_x] + "->" + min_y;//路径 
	            }else{ 
	                land[min_y] = land[min_y] + "->" + min_y;//路径 
	            } 
	           
	            //move the shortest point from T to S  把距离最小的点从T移到S             
	            for(int i=1; i<n+1; i++) {                 
	            	if(S[i] ==0){                     
	            		S[i] = T[min_y];                     
	            		break;                 
	            		}             
	            	} 
	            
	            T[min_y] = 0;  
	        }  
	        //输出 
//	        for(int i=1;i<n+1;i++){ 
//	            System.out.println(i + ". " + land[i] + ":   " + path[s][i]);         
//	            }
	        /*
	         * 提取线路从字符串到数组中
	         * obtain the path from String to array
	         */
	        int landInt[][]=new int[n+1][n+1];//landInt[i][j] i表示所选终点的id，j为中途经过站的id
	        for(int i=0;i<n+1;i++){
	        	for(int j=0;j<n+1;j++){
	        		Pattern p = Pattern.compile("\\d{1,}");//这个2是指连续数字的最少个数 
	                Matcher m = p.matcher(land[i]); 
	                int k = 0; 
	                while (m.find()) { 
	                    landInt[i][k]=Integer.valueOf(m.group());
	                    k++; 
	                } 
	        	}
	        } 
	        return landInt;
	}
	
	/*
	 * 通过得到路径id，转换成为字符串
	 * obtain the id of path,transmit them to string
	 */
	public String getWay(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
    	
    	int e = con.searchStation_id(endStationName);//end point id 终点站 id
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//obtain the number of station 得到地铁站的个数
    	/*
         * 把站点id通过数据库转换成为站点名字类型的线路
         * obtain the route of station name from database via station id
         */
	    String way;
	    way=con.searchStationName(landInt,n,e);
	    return way;
	}
	
	/*
	 * 得到换乘地铁线路的次数
	 * obtain the exchange time
	 */
	public int getExchangeLineNumber(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
        /*
         * 获得输入站起点id，终点id
         * obtain the id of starting and end point
         */
    	int e = con.searchStation_id(endStationName);//end point id 终点站 id
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//obtain the number of station 得到地铁站的个数
    	
    	int[] judgeExchange=con.judgeExchange();//obtains the array of exchange station id 得到换乘站id数组
    	int exchangeLineNumber=0;
    	for(int i=0;i<n+1;i++){
    		for(int k=0;k<52;k++){
    			if(landInt[e][i]==judgeExchange[k]){
    				if(i>0&&i<100){
    					//judge=1表示中转站前后两站线路一样，没有换乘，0表示换线了
    	    			//landInt[e][i-1]前一站 landInt[e][i+1]后一站
    					//judge=1 represent the last and next station is the same,
    					//there's no exchange,0 means there's exchange
    	    			//landInt[e][i-1]last station landInt[e][i+1]next station
	    				int beforeStation_id=landInt[e][i-1];
	    				int behindStation_id=landInt[e][i+1];
	    				int judge=con.judgeSameLine(beforeStation_id,behindStation_id);
	    				if(beforeStation_id==22&&behindStation_id==24||
	    						beforeStation_id==23&&behindStation_id==21){
	    					exchangeLineNumber++;
	    				}
	    				if(beforeStation_id==63&&behindStation_id==50&&landInt[e][i]==49){
	    					exchangeLineNumber++;
	    				}
	    				if(judge==0){
	    					exchangeLineNumber++;
	    				}
	    			}
    			}
    	
    		}
    	}
	    return exchangeLineNumber;
	}
	
	/*
	 * 得到换乘地铁站的次数
	 */
	public int getExchangeStationNumber(String startStationName,String endStationName,int exchangLineNumber) throws Exception{ 

        ConnectDB con=new ConnectDB();
        /*
         * 获得输入站起点id，终点id
         * obtaining the exchanging time
         */
    	int s = con.searchStation_id(startStationName);//起始点  id
    	int e = con.searchStation_id(endStationName);//终点站 id
    	 /*
    	  * 建立邻接矩阵，来进行dijkstra算法 
    	  * build adjacent matrix to apply Dijkstra algorithm 
    	  */
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//number of stations 得到地铁站的个数
    	int[][] path=new int[n+1][n+1];
    	path=builder.buildPath();//gain adjacent matrix from BuildPath 通过BuildPath获得邻接矩阵
    	
    	/*
    	 * 利用邻接矩阵进行dijkstra算法
    	 * apply Dijkstra to adjacent matrix
    	 */
        int S[] = new int[n+1];//集合S         
        int T[] = new int[n+1];//集合T         //初始化集合S、T， 
        for(int i=0; i<n+1; i++){ 
            S[i] = 0;             
            T[i] = i;         
            } 
        S[s] = s;         
        T[s] = 0;  
        
        //初始化路径，land用于记录并输出路径 
        String[] land = new String[n+1]; 
        for(int i=1; i<n+1; i++){             
        	land[i] = "" + s;
        	} 
        land[s] = s + "->" + s;  
        
        //循环开始 
        int t = 0;
        while(++t <= n){
        	//计算T中各点到S中各点的距离的最小值 
            int min = Integer.MAX_VALUE; 
            int min_x = 0;             
            int min_y = 0; 
            for(int i=1; i<n+1; i++){                 
            	if(S[i] != 0){ //找到起始点
                    for(int j=1; j<n+1; j++){ 
                    	
                        if(T[j] != 0 && path[S[i]][T[j]] != Integer.MAX_VALUE  && path[s][S[i]] + path[S[i]][T[j]] < min){
                        	//判断条件：如果T集合里有点，且点S[i]与这个点的距离不为无穷大，且从起始点到S[i]的距离 加上 S[i]到该点的距离 还小于现有最小值                              
                        	min = path[s][S[i]] + path[S[i]][T[j]];                             
                        	min_x = S[i];                             
                        	min_y = T[j];                         
                        	}                     
                        }                 
                    }             
            	}  
                        
            //保存最短路径 
            path[s][min_y] = min;                    //路径权值 
            if(min_x != s){//min_x不是起始点，即从起始点到该点要经过中间点 
                land[min_y] = land[min_x] + "->" + min_y;//路径 
            }else{ 
                land[min_y] = land[min_y] + "->" + min_y;//路径 
            } 
           
            //把距离最小的点从T移到S             
            for(int i=1; i<n+1; i++) {                 
            	if(S[i] ==0){                     
            		S[i] = T[min_y];                     
            		break;                 
            		}             
            	} 
            
            T[min_y] = 0;  
        }  
        path[s][e]=path[s][e]+exchangLineNumber;
        int number=path[s][e];
        return number;
	}
	
	/*
	 * 得到换成线路的名字
	 * get the name of exchange line.
	 */
	public String getExchangeLineName(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
		
        /*
         * 获得输入站起点id，终点id
         * get the id of input starting and end station 
         */
    	int e = con.searchStation_id(endStationName);//end point  id 终点站 id
    	
    	String way=con.getExchangeLineName(landInt[e][0],landInt[e][1]);
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//gain the number of stations 得到地铁站的个数
    	
    	int[] judgeExchange=con.judgeExchange();//obtain the array of exchange station 得到换乘站id数组
    	for(int i=0;i<n+1;i++){
    		for(int k=0;k<52;k++){
    			if(landInt[e][i]==judgeExchange[k]){
    				if(i>0&&i<100){
    					//judge=1表示中转站前后两站线路一样，没有换乘，0表示换线了
    	    			//landInt[e][i-1]前一站 landInt[e][i+1]后一站
    					//judge=1 represent there's no exchange，0 means there's exchange
    	    			//landInt[e][i-1]last station, landInt[e][i+1]next station
	    				int beforeStation_id=landInt[e][i-1];
	    				int behindStation_id=landInt[e][i+1];
	    				int judge=con.judgeSameLine(beforeStation_id,behindStation_id);
	    				if(judge==0){
	    					way=way+"->"+
	    				con.getExchangeLineName(landInt[e][i],landInt[e][i+1]);
	    				}
	    				if(beforeStation_id==63&&behindStation_id==50&&landInt[e][i]==49){
	    					way=way+"->"+"2号线";
	    				}
	    				if(beforeStation_id==50&&behindStation_id==63&&landInt[e][i]==49){
	    					way=way+"->"+"6号线";
	    				}
	    				if(beforeStation_id==22&&behindStation_id==24){
	    					way=way+"->"+"八通线";
	    				}
	    				if(beforeStation_id==23&&behindStation_id==21){
	    					way=way+"->"+"1号线";
	    				}
	    			}
    			}
    	
    		}
    	}
	    return way;
	}
	
	/*
	 * 得到换乘站名字
	 * gain the name of exchange station
	 */
	public String getExchangeStationName(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
		String way="";
		int exchangeStation_id[][]=new int[1][20];
        /*
         * 获得输入站起点id，终点id
         * gain the id of input start and end station
         */
    	int e = con.searchStation_id(endStationName);//end sttaion id 终点站 id
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//the number of stations 得到地铁站的个数
    	
    	int[] judgeExchange=con.judgeExchange();//the array of exchange station id 得到换乘站id数组
    	int j=0;
    	for(int i=0;i<n+1;i++){
    		for(int k=0;k<52;k++){
    			if(landInt[e][i]==judgeExchange[k]){
    				if(i>0&&i<100){
	    				int beforeStation_id=landInt[e][i-1];
	    				int behindStation_id=landInt[e][i+1];
	    				int judge=con.judgeSameLine(beforeStation_id,behindStation_id);
	    				if(judge==0){
	    					exchangeStation_id[0][j]=landInt[e][i];
	    					j++;
	    				}
	    				if(beforeStation_id==22&&behindStation_id==24||
	    						beforeStation_id==23&&behindStation_id==21){
	    					exchangeStation_id[0][j]=landInt[e][i];
	    					j++;
	    				}
	    				if(beforeStation_id==63&&behindStation_id==50&&landInt[e][i]==49
	    					||beforeStation_id==50&&behindStation_id==63&&landInt[e][i]==49){
	    					exchangeStation_id[0][j]=landInt[e][i];
	    					j++;
	    				}
	    			}
    			}
    	
    		}
    	}
    	way=con.searchStationName(exchangeStation_id, 19, 0);
	    return way;
	}
}
