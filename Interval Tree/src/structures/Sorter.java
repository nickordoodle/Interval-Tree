package structures;

import java.util.ArrayList;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 * 
 * @author runb-cs112
 */
public class Sorter {

	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		
		ArrayList<Integer> leftEndPoints = new ArrayList<Integer>();
		ArrayList<Integer> rightEndPoints = new ArrayList<Integer>();
		
		for(int i = 0; i < intervals.size(); i ++){
			
			leftEndPoints.add(intervals.get(i).leftEndPoint);
			
		}
		

		for(int i = 0; i < intervals.size(); i ++){
			
			rightEndPoints.add(intervals.get(i).rightEndPoint);
			
		}
		
		if(lr=='l'){
			
			insertionSort(leftEndPoints);
			
		}
		else if(lr == 'r'){
			
			insertionSort(rightEndPoints);

			
			
			
		
		}
							
		
	}
	
	private static void insertionSort(ArrayList<Integer> points){
		
		
	      int i, j, newValue;
	      
	      for (i = 1; i < points.size(); i++) {
	            newValue = points.get(i);
	            j = i;
	            while (j > 0 && points.get(j - 1) > newValue) {
	            	  points.set(j, points.get(j - 1));
	                  j--;
	            }
	            points.set(j, newValue);
	           
	      }
	
		
		
		
	}
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		
		ArrayList<Integer> sortedPoints = new ArrayList<Integer>();
	
		for (int i = 0; i < leftSortedIntervals.size(); i ++){
			
			sortedPoints.add(leftSortedIntervals.get(i).leftEndPoint);
			
			
		}
		for (int i = 0; i < rightSortedIntervals.size(); i ++){
			
			sortedPoints.add(rightSortedIntervals.get(i).rightEndPoint);
			
			
		}
		
		for(int i = 0; i < sortedPoints.size(); i ++){
			
			for(int j = i + 1; j < sortedPoints.size(); j++){
				
				if(sortedPoints.get(i) == sortedPoints.get(j)){
					sortedPoints.remove(j);
				}
				
			}
			
			
		}
		
		return sortedPoints;
		
		
	}
}
