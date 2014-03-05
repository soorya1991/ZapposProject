import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;


public class Zappos {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int n,cost;
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the Number of Products");
		n=input.nextInt();
		System.out.println("Enter the total cost");
		cost=input.nextInt();
		List<Double> price = new ArrayList<Double>();
		List<String> products = new ArrayList<String>();
		List<Double> price1 = new ArrayList<Double>();
		
		
		URL urlMy=new URL("http://api.zappos.com/Product/7515478?includes=[%22styles%22]&key=52ddafbe3ee659bad97fcce7c53592916a6bfd73");
	    URLConnection tc = urlMy.openConnection();
	    BufferedReader in = new BufferedReader(new InputStreamReader(
	            tc.getInputStream()));
	    String line=in.readLine();
	    System.out.println(line);
	    
	    if (line != null) {                 
	    	try {                     
	    		JSONObject jsonObj = new JSONObject(line); 
	    		JSONArray product = jsonObj.getJSONArray("product");
	    		
	    		
	    		for (int i = 0; i < product.length(); i++) {
	    			JSONObject obj = product.getJSONObject(i);
	    			
	    			JSONArray styles = obj.getJSONArray("styles");
	    			
	    			for (int j = 0; j < styles.length(); j++) {
	    				JSONObject sty = styles.getJSONObject(j);
		    			products.add(sty.get("styleId").toString());
		    			String a = sty.get("price").toString();
		    		    price.add(Math.ceil(Double.parseDouble(a.substring(1))));
		    		    price1.add(Double.parseDouble(a.substring(1)));
	    			}
	    			
	    			
					
				}
	    		
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    
		
	    
		double[] totCost = new double[price.size() + 1];
		totCost[0] = Integer.MIN_VALUE;
		for (int i = 0; i < price.size(); i++) {
			totCost[i+1] = price.get(i);
		}
		int total = price.size();
		
		
		int[][][]  dp = new int[cost+1][total+1][n+1];
		int[][][]  keepArray = new int[cost+1][total+1][n+1];
		
		for (int i = 0; i <= total; i++) {
			for (int j = 0; j <= n; j++) {
				dp[0][i][j] = 0;
			}
		}
		
		for (int i = 0; i <= cost; i++) {
			for (int j = 0; j <= n; j++) {
				dp[i][0][j] = 0;
			}
		}
		
		for (int i = 0; i <= cost; i++) {
			for (int j = 0; j <= total; j++) {
				dp[i][j][0] = 0;
			}
		}
		
		
			
			for (int j = 1; j <= total; j++) {
				for (int k = 1; k <= n; k++) {
							
					for (int i = 1; i <= cost; i++) {

					
					if (j < k) {
						dp[i][j][k] = dp[i][j][k-j];
					} else {
						int itemCost = (int) totCost[j];
						if (i >= itemCost) {
							int curr = dp[i-itemCost][j-1][k-1] + itemCost;
							dp[i][j][k] = closeToCost(dp[i][j-1][k], curr, i);
							if(dp[i][j][k] == curr) {
								keepArray[i][j][k] = 1;
							}
						} else {

							dp[i][j][k] = dp[i][j-1][k];
						
						}
		}
					
				}
				
			}
			
		}
		
		System.out.println("Total Cost = " + dp[cost][total][n]);
		double cost1 = cost;
		for (int i = total; i >= 1; i--) { // need to go in the reverse order
		    if (keepArray[cost][i][n] == 1) {
		      cost1 = cost1 - totCost[i];
		      n = n-1;
		      System.out.println("Style Id,Cost: " + products.get(i-1) + "  " + price1.get(i-1));
		    }
		  }

	}
	
	
	private static int closeToCost(int prev, int curr, int currentCost) {
		int first = currentCost - prev;
		int second = currentCost - curr;
		if (first < 0) {
			first = first * -1;
		}
		if (second < 0) {
			second = second * -1;
		}
		
		if (first <second) {
			return prev;
		} else {
			return curr;
		}
	}

}
