package dijkstra;

import java.util.List;

public class BuildPath {
	
	/*
	 * �õ�����վ����
	 * obtaining the number of subway station
	 */
	public int getListSize() throws Exception{
		ConnectDB connect=new ConnectDB();
    	List<Station> list=connect.readDB();
    	int n=list.size();
    	return n;
	}
	/*
	 * ���ݵ���վ������������ڽӾ���
	 * build the adjacent matrix according to the topology of subway
	 */
	public int[][] buildPath() throws Exception{
		ConnectDB connect=new ConnectDB();
    	List<Station> list=connect.readDB();
        //initiate the path to the longest path ��ʼ��·������Ϊ���ֵ�� 
    	int n=list.size();
        int path[][] = new int[n+1][n+1]; 
        for(int i=1; i<n+1; i++){             
        	for(int j=1; j<n+1; j++){ 
        
                path[i][j]=Integer.MAX_VALUE;
                } 
            path[i][i] = 0;//set the distance to itself is 0  ������ľ���Ϊ0         
        }         
        for(Station st:list){
        	int id1=st.getStation_id();
        	int id2=st.getStationNext1();
        	int id3=st.getStationNext2();
        	int id4=st.getStationNext3();
        	int id5=st.getStationNext4();
        	int id6=st.getStationNext5();
        	path[id1][id2] = 1;
        	path[id1][id3] = 1;
        	path[id1][id4] = 1;
        	path[id1][id5] = 1;
        	path[id1][id6] = 1;
        }
        return path;
	}
}
