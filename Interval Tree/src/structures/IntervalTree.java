package structures;

import java.util.*;


/*QUESTIONS:
 * 
 * What exactly do I have to do for each method?
 * Do I use the IntervalTree constructor at all? YES
 * What to do for, " for each point p in Points do
              create a tree T with a single node containing p
              set split value of this node to p
              enqueue T in queue Q
           endfor"
           
   Is the assignment basically just coding the steps? YES
   Ask about how the tree is constructed in general?
   Does the root start at the bottom or is it always the top node?
   
   
   
   USE HELPER METHOD FOR QUERY METHOD TO USE RECURSION
 */


/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		Sorter.sortIntervals(intervalsLeft, 'l');
		Sorter.sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = Sorter.getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		
		Queue<IntervalTreeNode> tree = new Queue<IntervalTreeNode>();
		float value;
		
		for(int i = 0; i < endPoints.size(); i ++){
			
			value = endPoints.get(i);
				
			IntervalTreeNode treeNode = new IntervalTreeNode(value,value,value);
			treeNode.leftIntervals = new ArrayList<Interval>();
			treeNode.rightIntervals = new ArrayList<Interval>();

			tree.enqueue(treeNode);

			
		}
		
		
		IntervalTreeNode result = null;
		
		int treeSize = tree.size;
		
		while(treeSize > 0){
			
			if(treeSize == 1){
				
				result = tree.dequeue();
				return result;
				
			}
			else{
				int tempSize = treeSize;
				while(tempSize > 1){
					IntervalTreeNode t1 = tree.dequeue();
					IntervalTreeNode t2 = tree.dequeue();
					float v1 = t1.maxSplitValue;
					float v2 = t2.minSplitValue;
					float x = (v1+v2)/(2);
					IntervalTreeNode N = new IntervalTreeNode(x, t1.minSplitValue, t2.maxSplitValue);
					N.leftIntervals = new ArrayList<Interval>();
					N.rightIntervals = new ArrayList<Interval>();
					N.leftChild = t1;
					N.rightChild = t2;
					tree.enqueue(N);
					tempSize = tempSize-2;
					
					
				}
				if (tempSize == 1){
					IntervalTreeNode single = tree.dequeue();
					tree.enqueue(single);
				}
				treeSize = tree.size;
			}
				
			
		}
		
		result = tree.dequeue();
		return result;
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	private void treeFill(Interval interval, IntervalTreeNode node, boolean left){
		
		if (interval.contains(node.splitValue)){
			if (left){
				node.leftIntervals.add(interval);
			}
			else {
				node.rightIntervals.add(interval);
			}
			return;
		}

		if (node.splitValue < interval.leftEndPoint){
			treeFill(interval, node.rightChild, left);
		}
		else {
			treeFill(interval, node.leftChild, left);
		}

	}
	
	
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		
		
		for (Interval curr : leftSortedIntervals){
			treeFill(curr, root, true);
		}
		
		for (Interval curr : rightSortedIntervals){
			treeFill(curr, root, false);
		}
		
		
		
		
		
	}
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		
		return getIntersections(root, q);
	}
	
	private ArrayList<Interval> getIntersections(IntervalTreeNode node, Interval interval){
		ArrayList<Interval> intersections = new ArrayList<Interval>();

		if (node == null){
			return intersections;
		}

		float nodeSplitVal = node.splitValue;
		ArrayList<Interval> rightIntervals = node.rightIntervals;
		ArrayList<Interval> leftIntervals = node.leftIntervals;
		IntervalTreeNode leftChild = node.leftChild;
		IntervalTreeNode rightChild = node.rightChild;

		if (interval.contains(nodeSplitVal)){//if the interval has the splitvalue of the root
			
			for (Interval i : leftIntervals){//adds all of the leftsorted intervals
				intersections.add(i);
			}
			
			intersections.addAll(getIntersections(rightChild, interval));
			
			intersections.addAll(getIntersections(leftChild, interval));
			
		}

		else if (nodeSplitVal < interval.leftEndPoint){
			
			int i = rightIntervals.size()-1;
			
			while (i >= 0 && (rightIntervals.get(i).intersects(interval))){
				intersections.add(rightIntervals.get(i));
				i--;
			}

			intersections.addAll(getIntersections(rightChild, interval));
		}

		else if (nodeSplitVal > interval.rightEndPoint){
			
			int i = 0;
			
			while ((i < leftIntervals.size()) && (leftIntervals.get(i).intersects(interval))){
				
				intersections.add(leftIntervals.get(i));						
				i++;
			}
			
			intersections.addAll(getIntersections(leftChild, interval));
		}
	
		return intersections;
	}
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
}

