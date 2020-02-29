package dijkstra;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dijkstra {
	    //n �ڵ�����������վ�ܸ����� s ��ʼվ��id e �յ�վ��id
		//n:total number of nodes or stations s: start station id e:end station id
	/*
	 * �õ����������յ���м�·��վ���վ��id
	 * obtaining the ID of start point,end point and intermediate station
	 */
	public int[][] getWayID(String startStationName) throws Exception{ 

	        ConnectDB con=new ConnectDB();
	        
	    	int s = con.searchStation_id(startStationName);//��ʼ��  id
	    	 /*
	    	  * �����ڽӾ���������dijkstra�㷨 
	    	  * build adjacent matrix, then calculate by Dijkstra algorithm 
	    	  */
	    	BuildPath builder=new BuildPath();
	    	int n=builder.getListSize();//�õ�����վ�ĸ���
	    	int[][] path=new int[n+1][n+1];
	    	path=builder.buildPath();//ͨ��BuildPath����ڽӾ���
	    	
	    	/*
	    	 * �����ڽӾ������dijkstra�㷨
	    	 * Appling Dijkstra algorithm to adjacent matrix
	    	 */
	        int S[] = new int[n+1];//����S         
	        int T[] = new int[n+1];//����T         //��ʼ������S��T�� 
	        for(int i=0; i<n+1; i++){ 
	            S[i] = 0;             
	            T[i] = i;         
	            } 
	        S[s] = s;         
	        T[s] = 0;  
	        
	        //��ʼ��·����land���ڼ�¼�����·�� 
	        //initiating the path,land was used to record and output the path
	        String[] land = new String[n+1]; 
	        for(int i=1; i<n+1; i++){             
	        	land[i] = "" + s;
	        	} 
	        land[s] = s + "->" + s;  
	        
	        
	        //Beginning of loop ѭ����ʼ 
	        int t = 0;
	        while(++t <= n){
	        	//calculating the shortest path from every point in T to that in S
	        	//����T�и��㵽S�и���ľ������Сֵ 
	            int min = Integer.MAX_VALUE; 
	            int min_x = 0;             
	            int min_y = 0; 
	            for(int i=1; i<n+1; i++){                 
	            	if(S[i] != 0){ //find the starting point �ҵ���ʼ��
	                    for(int j=1; j<n+1; j++){ 
	                        if(T[j] != 0 && path[S[i]][T[j]] != Integer.MAX_VALUE  && path[s][S[i]] + path[S[i]][T[j]] < min){
	                        	//�ж����������T�������е㣬�ҵ�S[i]�������ľ��벻Ϊ������Ҵ���ʼ�㵽S[i]�ľ��� ���� S[i]���õ�ľ��� ��С��������Сֵ   
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
	                        
	            //save the shortest path �������·�� 
	            path[s][min_y] = min;                    //weighted path ·��Ȩֵ 
	            if(min_x != s){//if min_x is not the starting point  min_x������ʼ�㣬������ʼ�㵽�õ�Ҫ�����м�� 
	                land[min_y] = land[min_x] + "->" + min_y;//·�� 
	            }else{ 
	                land[min_y] = land[min_y] + "->" + min_y;//·�� 
	            } 
	           
	            //move the shortest point from T to S  �Ѿ�����С�ĵ��T�Ƶ�S             
	            for(int i=1; i<n+1; i++) {                 
	            	if(S[i] ==0){                     
	            		S[i] = T[min_y];                     
	            		break;                 
	            		}             
	            	} 
	            
	            T[min_y] = 0;  
	        }  
	        //��� 
//	        for(int i=1;i<n+1;i++){ 
//	            System.out.println(i + ". " + land[i] + ":   " + path[s][i]);         
//	            }
	        /*
	         * ��ȡ��·���ַ�����������
	         * obtain the path from String to array
	         */
	        int landInt[][]=new int[n+1][n+1];//landInt[i][j] i��ʾ��ѡ�յ��id��jΪ��;����վ��id
	        for(int i=0;i<n+1;i++){
	        	for(int j=0;j<n+1;j++){
	        		Pattern p = Pattern.compile("\\d{1,}");//���2��ָ�������ֵ����ٸ��� 
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
	 * ͨ���õ�·��id��ת����Ϊ�ַ���
	 * obtain the id of path,transmit them to string
	 */
	public String getWay(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
    	
    	int e = con.searchStation_id(endStationName);//end point id �յ�վ id
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//obtain the number of station �õ�����վ�ĸ���
    	/*
         * ��վ��idͨ�����ݿ�ת����Ϊվ���������͵���·
         * obtain the route of station name from database via station id
         */
	    String way;
	    way=con.searchStationName(landInt,n,e);
	    return way;
	}
	
	/*
	 * �õ����˵�����·�Ĵ���
	 * obtain the exchange time
	 */
	public int getExchangeLineNumber(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
        /*
         * �������վ���id���յ�id
         * obtain the id of starting and end point
         */
    	int e = con.searchStation_id(endStationName);//end point id �յ�վ id
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//obtain the number of station �õ�����վ�ĸ���
    	
    	int[] judgeExchange=con.judgeExchange();//obtains the array of exchange station id �õ�����վid����
    	int exchangeLineNumber=0;
    	for(int i=0;i<n+1;i++){
    		for(int k=0;k<52;k++){
    			if(landInt[e][i]==judgeExchange[k]){
    				if(i>0&&i<100){
    					//judge=1��ʾ��תվǰ����վ��·һ����û�л��ˣ�0��ʾ������
    	    			//landInt[e][i-1]ǰһվ landInt[e][i+1]��һվ
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
	 * �õ����˵���վ�Ĵ���
	 */
	public int getExchangeStationNumber(String startStationName,String endStationName,int exchangLineNumber) throws Exception{ 

        ConnectDB con=new ConnectDB();
        /*
         * �������վ���id���յ�id
         * obtaining the exchanging time
         */
    	int s = con.searchStation_id(startStationName);//��ʼ��  id
    	int e = con.searchStation_id(endStationName);//�յ�վ id
    	 /*
    	  * �����ڽӾ���������dijkstra�㷨 
    	  * build adjacent matrix to apply Dijkstra algorithm 
    	  */
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//number of stations �õ�����վ�ĸ���
    	int[][] path=new int[n+1][n+1];
    	path=builder.buildPath();//gain adjacent matrix from BuildPath ͨ��BuildPath����ڽӾ���
    	
    	/*
    	 * �����ڽӾ������dijkstra�㷨
    	 * apply Dijkstra to adjacent matrix
    	 */
        int S[] = new int[n+1];//����S         
        int T[] = new int[n+1];//����T         //��ʼ������S��T�� 
        for(int i=0; i<n+1; i++){ 
            S[i] = 0;             
            T[i] = i;         
            } 
        S[s] = s;         
        T[s] = 0;  
        
        //��ʼ��·����land���ڼ�¼�����·�� 
        String[] land = new String[n+1]; 
        for(int i=1; i<n+1; i++){             
        	land[i] = "" + s;
        	} 
        land[s] = s + "->" + s;  
        
        //ѭ����ʼ 
        int t = 0;
        while(++t <= n){
        	//����T�и��㵽S�и���ľ������Сֵ 
            int min = Integer.MAX_VALUE; 
            int min_x = 0;             
            int min_y = 0; 
            for(int i=1; i<n+1; i++){                 
            	if(S[i] != 0){ //�ҵ���ʼ��
                    for(int j=1; j<n+1; j++){ 
                    	
                        if(T[j] != 0 && path[S[i]][T[j]] != Integer.MAX_VALUE  && path[s][S[i]] + path[S[i]][T[j]] < min){
                        	//�ж����������T�������е㣬�ҵ�S[i]�������ľ��벻Ϊ������Ҵ���ʼ�㵽S[i]�ľ��� ���� S[i]���õ�ľ��� ��С��������Сֵ                              
                        	min = path[s][S[i]] + path[S[i]][T[j]];                             
                        	min_x = S[i];                             
                        	min_y = T[j];                         
                        	}                     
                        }                 
                    }             
            	}  
                        
            //�������·�� 
            path[s][min_y] = min;                    //·��Ȩֵ 
            if(min_x != s){//min_x������ʼ�㣬������ʼ�㵽�õ�Ҫ�����м�� 
                land[min_y] = land[min_x] + "->" + min_y;//·�� 
            }else{ 
                land[min_y] = land[min_y] + "->" + min_y;//·�� 
            } 
           
            //�Ѿ�����С�ĵ��T�Ƶ�S             
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
	 * �õ�������·������
	 * get the name of exchange line.
	 */
	public String getExchangeLineName(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
		
        /*
         * �������վ���id���յ�id
         * get the id of input starting and end station 
         */
    	int e = con.searchStation_id(endStationName);//end point  id �յ�վ id
    	
    	String way=con.getExchangeLineName(landInt[e][0],landInt[e][1]);
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//gain the number of stations �õ�����վ�ĸ���
    	
    	int[] judgeExchange=con.judgeExchange();//obtain the array of exchange station �õ�����վid����
    	for(int i=0;i<n+1;i++){
    		for(int k=0;k<52;k++){
    			if(landInt[e][i]==judgeExchange[k]){
    				if(i>0&&i<100){
    					//judge=1��ʾ��תվǰ����վ��·һ����û�л��ˣ�0��ʾ������
    	    			//landInt[e][i-1]ǰһվ landInt[e][i+1]��һվ
    					//judge=1 represent there's no exchange��0 means there's exchange
    	    			//landInt[e][i-1]last station, landInt[e][i+1]next station
	    				int beforeStation_id=landInt[e][i-1];
	    				int behindStation_id=landInt[e][i+1];
	    				int judge=con.judgeSameLine(beforeStation_id,behindStation_id);
	    				if(judge==0){
	    					way=way+"->"+
	    				con.getExchangeLineName(landInt[e][i],landInt[e][i+1]);
	    				}
	    				if(beforeStation_id==63&&behindStation_id==50&&landInt[e][i]==49){
	    					way=way+"->"+"2����";
	    				}
	    				if(beforeStation_id==50&&behindStation_id==63&&landInt[e][i]==49){
	    					way=way+"->"+"6����";
	    				}
	    				if(beforeStation_id==22&&behindStation_id==24){
	    					way=way+"->"+"��ͨ��";
	    				}
	    				if(beforeStation_id==23&&behindStation_id==21){
	    					way=way+"->"+"1����";
	    				}
	    			}
    			}
    	
    		}
    	}
	    return way;
	}
	
	/*
	 * �õ�����վ����
	 * gain the name of exchange station
	 */
	public String getExchangeStationName(int[][] landInt,String endStationName) throws Exception{
		ConnectDB con=new ConnectDB();
		String way="";
		int exchangeStation_id[][]=new int[1][20];
        /*
         * �������վ���id���յ�id
         * gain the id of input start and end station
         */
    	int e = con.searchStation_id(endStationName);//end sttaion id �յ�վ id
    	
    	BuildPath builder=new BuildPath();
    	int n=builder.getListSize();//the number of stations �õ�����վ�ĸ���
    	
    	int[] judgeExchange=con.judgeExchange();//the array of exchange station id �õ�����վid����
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
