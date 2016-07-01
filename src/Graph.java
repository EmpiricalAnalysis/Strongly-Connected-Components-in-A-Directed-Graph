import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Graph {
	
	private List<Graph> children = new ArrayList<>();
	private List<Graph> parent = new ArrayList<>();
	private Graph pi;
	private byte color;			//white = 0, gray = 1, or black = 2
	private int dt;				//time of discovery
	private int ft;				//time of finishing
	private int self;
	private byte Tcolor;		//color of the transpose of given graph; white = 0, gray = 1, or black = 2
	
	public List<Graph> getChildren() {
		return children;
	}

	public List<Graph> getParent() {
		return parent;
	}
	
	public Graph getPi() {
		return pi;
	}
	
	public byte getColor() {
		return color;
	}
	
	public int getDT() {
		return dt;
	}
	
	public int getFT() {
		return ft;
	}
	
	public int getSelf() {
		return self;
	}
	
	public int getTcolor() {
		return Tcolor;
	}
	
	public void setChildren(Graph child_vertex) {
		this.children.add(child_vertex);
	}

	public void setParent(Graph parent_vertex) {
		this.parent.add(parent_vertex);
	}
	
	public void setPi(Graph pi) {
		this.pi = pi;
	}

	public void setColor(byte color) {
		this.color = color;
	}

	public void setDT(int dt) {
		this.dt = dt;
	}
	
	public void setFT(int ft) {
		this.ft = ft;
	}
	
	public void setSelf(int idx) {
		this.self = idx;
	}
	
	public void setTcolor(byte color) {
		this.Tcolor = color;
	}
	
	public Graph() {
		this.color = 0;
		this.dt = 0;
		this.ft = 0;
		this.Tcolor = 0;
	}
	
	public static void DFS (List<Graph> LG, int[] forder) {					//forder = finishing order
        int[] Time_Order = new int[2];										//the first element is the timer, the 2nd element is the finishing order
        
		for (int i = 0; i < LG.size(); i++) {
			if (LG.get(i).getColor() == 0) {
				//System.out.println("i = "+i);
				//System.out.println("Beginning color"+LG.get(i).getColor());
				DFSvisit(LG, LG.get(i), Time_Order, forder);
			}
		}
	}
	
	public static void DFSvisit (List<Graph> LG, Graph u, int[] Time_Order, int[] forder) {			//

		Time_Order[0]++;
		
		u.setDT(Time_Order[0]);
		//System.out.println("before color"+u.getColor());
		u.setColor((byte) 1);
		//System.out.println("during color"+u.getColor());
		//System.out.println("node "+(LG.indexOf(u)+1)+" has "+u.getChildren().size()+" children");
		for (int j = 0; j < u.getChildren().size(); j++) {
			//System.out.println("j = "+j);
			if (u.getChildren().get(j).getColor() == (byte) 0) {
				u.getChildren().get(j).setPi(u);
				DFSvisit(LG, u.getChildren().get(j), Time_Order, forder);		//
			}
		}
		u.setColor((byte) 2);
		//System.out.println("after color"+u.getColor());
		//System.out.println("----------------------------------");		
		
		Time_Order[0]++;
		
		u.setFT(Time_Order[0]);	
		
		forder[forder.length-1-Time_Order[1]] = LG.indexOf(u);
		Time_Order[1]++;
		
		//System.out.println("node "+u.getSelf()+" is done!");
		
	}
	
//====================================================================================================
	
	public static void DFS_T (List<Graph> LG, int[] forder, int[] ncomp) {			//DFS for the transposed graph
        
		int icomp = 0;											// the i-th component
		
		for (int i = 0; i < LG.size(); i++) {
			
			
			if (LG.get(forder[i]).getTcolor() == 0) {
				
				icomp++;
				
				ncomp[forder[i]] = icomp;

				DFSvisit_T(LG, LG.get(forder[i]), icomp, ncomp);
				
				
			}
			
		}
	}
	
	public static void DFSvisit_T (List<Graph> LG, Graph u, int icomp, int[] ncomp) {			//
		
		u.setTcolor((byte) 1);

		/*
        System.out.print("node "+(LG.indexOf(u)+1)+" has parents: ");
        for (int j = 0; j < u.getParent().size(); j++) {
        	System.out.print((LG.indexOf(u.getParent().get(j))+1)+" ");
        }
        System.out.println();
		*/

		
		for (int j = 0; j < u.getParent().size(); j++) {

			if (u.getParent().get(j).getTcolor() == (byte) 0) {
				
				ncomp[LG.indexOf(u.getParent().get(j))] = icomp;
				
				DFSvisit_T(LG, u.getParent().get(j), icomp, ncomp);		 
			}
		}
		u.setTcolor((byte) 2);

	}
	

	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		long startTime = System.nanoTime();    
		   
		
		List<Graph> lg = new ArrayList<>();			//list of graphs
		
		int N = 875714;									//# of vertices
		
		int[] forder = new int[N];			//for 1st DFS
		int[] ncomp = new int[N];
		
		for (int i = 0; i < N; i++) {
			lg.add(new Graph());
			lg.get(i).setSelf(i);
		}
		
        File inputFile = new File("SCC.txt");		//SCC
        Scanner input = new Scanner(inputFile); 
        Scanner line;        
   
        while (input.hasNextLine()) {  
        	
            String inputEdges = input.nextLine();   
            line = new Scanner(inputEdges); 
            
            int col1 = line.nextInt()-1;
            int col2 = line.nextInt()-1;
            
            
            lg.get(col1).setChildren(lg.get(col2));
            lg.get(col2).setParent(lg.get(col1));            
            
         }  
        
        input.close(); 

        /*
        for (int k = 0; k < lg.size(); k++) {
        	System.out.print("node "+(k+1)+" has children ");
        	for (int j = 0; j < lg.get(k).getChildren().size(); j++) {
        		System.out.print((lg.indexOf(lg.get(k).getChildren().get(j))+1) + " ");
        	}
        	System.out.println();
        }
        
        
        for (int k = 0; k < lg.size(); k++) {
        	System.out.print("node "+(k+1)+" has parents ");
        	for (int j = 0; j < lg.get(k).getParent().size(); j++) {
        		System.out.print((lg.indexOf(lg.get(k).getParent().get(j))+1) +" ");
        	}
        	System.out.println();
        }
        */
        
        System.out.println("time for reading in vertices is: "+((System.nanoTime() - startTime)/Math.pow(10.,9.)/60.0));
        
        
        startTime = System.nanoTime();
        
        DFS(lg, forder);
        
        System.out.println("time for 1st DFS is: "+(System.nanoTime() - startTime)/Math.pow(10.,9.)/60.0);
        
        /*
        for (int k = 0; k < lg.size(); k++) {
        	System.out.println("node "+(k)+" was dicovered at "+lg.get(k).getDT()+" ; and finished at "+lg.get(k).getFT()+"; finishing_order["+k+"] = "+forder[k]);
        }
        
        
        for (int k = 0; k < lg.size(); k++) {
        	System.out.print("node "+(k+1)+" has parents ");
        	for (int j = 0; j < lg.get(k).getParent().size(); j++) {
        		System.out.print((lg.indexOf(lg.get(k).getParent().get(j))+1)+" ");
        	}
        	System.out.println();
        }
        */
        
        startTime = System.nanoTime();
        
        DFS_T(lg, forder, ncomp);
        
        System.out.println("time for 2nd DFS is: "+(System.nanoTime() - startTime)/Math.pow(10.,9.)/60.0);
        
        /*
        for (int k = 0; k < lg.size(); k++) {
        	System.out.println("node "+(k+1)+" is in component "+ncomp[k]);
        }
        */
        
        startTime = System.nanoTime();
        
        Integer[] tale = new Integer[N];
        for (int k = 0; k < N; k++) {
        	tale[k] = 0;
        }
        for (int k = 0; k < N; k++) {
        	tale[ncomp[k]]++;
        }
        
        Arrays.sort(tale, Collections.reverseOrder());
        
        System.out.println("time for grouping components is: "+(System.nanoTime() - startTime)/Math.pow(10.,9.));
        
        
        for (int k = 0; k < 5; k++) {			//lg.size()
        	System.out.println("component "+(k)+" has "+tale[k]+" elements");
        }

	}

}
