package searchtrees;

import java.util.Random;

import hashTable.CuckooHashTable;
import hashTable.StringHashFamily;

//BinarySearchTree class
//
//CONSTRUCTION: with no initializer
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//void remove( x )       --> Remove x
//boolean contains( x )  --> Return true if x is present
//Comparable findMin( )  --> Return smallest item
//Comparable findMax( )  --> Return largest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//void printTree( )      --> Print tree in sorted order
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
* Implements an unbalanced binary search tree.
* Note that all "matching" is based on the compareTo method.
* @author Mark Allen Weiss
*/
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>
{
 /**
  * Construct the tree.
  */
 public BinarySearchTree( )
 {
     root = null;
 }

 /**
  * Insert into the tree; duplicates are ignored.
  * @param x the item to insert.
  */
 public void insert( AnyType x )
 {
     root = insert( x, root );
 }

 /**
  * Remove from the tree. Nothing is done if x is not found.
  * @param x the item to remove.
  */
 public void remove( AnyType x )
 {
     root = remove( x, root );
 }

 /**
  * Find the smallest item in the tree.
  * @return smallest item or null if empty.
  */
 public AnyType findMin( )
 {
     if( isEmpty( ) )
         return null;
     return findMin( root ).element;
 }

 /**
  * Find the largest item in the tree.
  * @return the largest item of null if empty.
  */
 public AnyType findMax( )
 {
     if( isEmpty( ) )
         return null;
     return findMax( root ).element;
 }

 /**
  * Find an item in the tree.
  * @param x the item to search for.
  * @return true if not found.
  */
 public boolean contains( AnyType x )
 {
     return contains( x, root );
 }

 /**
  * Make the tree logically empty.
  */
 public void makeEmpty( )
 {
     root = null;
 }

 /**
  * Test if the tree is logically empty.
  * @return true if empty, false otherwise.
  */
 public boolean isEmpty( )
 {
     return root == null;
 }

 /**
  * Print the tree contents in sorted order.
  */
 public void printTree( )
 {
     if( isEmpty( ) )
         System.out.println( "Empty tree" );
     else
         printTree( root );
 }

 /**
  * Internal method to insert into a subtree.
  * @param x the item to insert.
  * @param t the node that roots the subtree.
  * @return the new root of the subtree.
*/
 private BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
 {
     if( t == null )
         return new BinaryNode<>( x, null, null );
     
     int compareResult = x.compareTo( t.element );
         
     if( compareResult < 0 )
         t.left = insert( x, t.left );
     else if( compareResult > 0 )
         t.right = insert( x, t.right );
     else
         ;  // Duplicate; do nothing
     return t;
 }

 /**
  * Non recursive method, created by LR - 29-092014
  
 private BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
 {
     if( t == null )
         return new BinaryNode<>( x, null, null );
     
     while (t != null) {     
    	 int compareResult = x.compareTo( t.element );
         
    	 if( compareResult < 0 )
    		 t = t.left;
    	 else if( compareResult > 0 )
    		 t = t.right;
    	 else
    		 ;  // Duplicate; do nothing
     }
    	 return t;
 }*/
 
 /**
  * Internal method to remove from a subtree.
  * @param x the item to remove.
  * @param t the node that roots the subtree.
  * @return the new root of the subtree.
  */
 private BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
 {
     if( t == null )
         return t;   // Item not found; do nothing
         
     int compareResult = x.compareTo( t.element );
         
     if( compareResult < 0 )
         t.left = remove( x, t.left );
     else if( compareResult > 0 )
         t.right = remove( x, t.right );
     else if( t.left != null && t.right != null ) // Two children
     {
         t.element = findMin( t.right ).element;
         t.right = remove( t.element, t.right );
     }
     else
         t = ( t.left != null ) ? t.left : t.right;
     return t;
 }

 /**
  * Internal method to find the smallest item in a subtree.
  * @param t the node that roots the subtree.
  * @return node containing the smallest item.
  */
 private BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
 {
     if( t == null )
         return null;
     else if( t.left == null )
         return t;
     return findMin( t.left );
 }

 /**
  * Internal method to find the largest item in a subtree.
  * @param t the node that roots the subtree.
  * @return node containing the largest item.
  */
 private BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
 {
     if( t != null )
         while( t.right != null )
             t = t.right;

     return t;
 }

 /**
  * Internal method to find an item in a subtree.
  * @param x is item to search for.
  * @param t the node that roots the subtree.
  * @return node containing the matched item.
  */
 private boolean contains( AnyType x, BinaryNode<AnyType> t )
 {
     if( t == null )
         return false;
         
     int compareResult = x.compareTo( t.element );
         
     if( compareResult < 0 )
         return contains( x, t.left );
     else if( compareResult > 0 )
         return contains( x, t.right );
     else
         return true;    // Match
 }

 /**
  * Internal method to print a subtree in sorted order.
  * @param t the node that roots the subtree.
  */
 private void printTree( BinaryNode<AnyType> t )
 {
     if( t != null )
     {
         printTree( t.left );
         System.out.println( t.element );
         printTree( t.right );
     }
 }

 /**
  * Internal method to compute height of a subtree.
  * @param t the node that roots the subtree.
  */
 private int height( BinaryNode<AnyType> t )
 {
     if( t == null )
         return -1;
     else
         return 1 + Math.max( height( t.left ), height( t.right ) );    
 }
 
 // Basic node stored in unbalanced binary search trees
 private static class BinaryNode<AnyType>
 {
         // Constructors
     BinaryNode( AnyType theElement )
     {
         this( theElement, null, null );
     }

     BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
     {
         element  = theElement;
         left     = lt;
         right    = rt;
     }

     AnyType element;            // The data in the node
     BinaryNode<AnyType> left;   // Left child
     BinaryNode<AnyType> right;  // Right child
 }


   /** The tree root. */
 private BinaryNode<AnyType> root;

 //Q4a: Insert 100,000 integer keys, from 1 to 100,000 (in that order). Find the average time for each insertion. 
 private static BinarySearchTree<Integer> a = new BinarySearchTree<Integer>();
 public static long insert_time(int NUMS)
 {
	 	 
	 long start, total_time = 0;
	 
	 for( int i = 1; i <= NUMS; i++)
     {
		 start = System.nanoTime();
		 a.insert( i );
		 total_time += System.nanoTime() - start;
     }
	 
	 return total_time / NUMS;
	 
 }
 
 //Q4b: Do 100,000 searches of random integer keys between 1 and 100,000. Find the average time of each search. 
 public static long search_time(int NUMS)
 {
	 long start, total_time = 0;
	 
     Random r = new Random( );
	 
	 for( int i = 1; i <= NUMS; i++)
     {
		 start =  System.nanoTime();
		 if (!a.contains( r.nextInt(NUMS-1)+1 ))
		 {
			// System.out.println("key " + i + " not found" );
		 }
		 total_time += System.nanoTime()-start;
     }
	 
	 return total_time / NUMS;
	 
 }
 
//Q4c: Delete all the keys in the trees, starting from 100,000 down to 1 (in that order). Find the average time of each deletion. 
 public static long delete_time(int NUMS)
 {
	 
	 long start, total_time = 0;
	 
	 for( int i = NUMS; i >= 1; i--)
     {
		 start = System.nanoTime();
         a.remove( i );
		 total_time += System.nanoTime() - start;
     }
	 
	 return total_time / NUMS;
	 
}
 //Q5a: Insert 100,000 of random keys between 1 and 100,000. Find the average time of each search.
 public static long insertRandom_time(int NUMS)
 {
	 long start, total_time = 0;
	 Random r = new Random();
	 
	 for ( int i = 1; i <= NUMS; i++)
	 {
		 start = System.nanoTime();
		 a.insert(r.nextInt(NUMS-1)+1);
		 total_time += System.nanoTime() - start;
	 }
	 return total_time / NUMS;
	 
 }
 // Q5c: Delete all the keys in the trees, with random keys between 1 and 100,000. Find the average time of each deletion. 
 // Note that not all the keys may be found in the tree. 
 public static long deleteRandom_time(int NUMS)
 {
	 
	 long start, total_time = 0;
	 
	 Random r = new Random( );
	 
	 for( int i = NUMS; i >= 1; i--)
     {
		 start = System.nanoTime();
         a.remove(r.nextInt(NUMS-1)+1);
         //H_temp.checkBalance( );
		 total_time += System.nanoTime() - start;
     }
	 
	 return total_time / NUMS;
	 
} 
     // Test program
 public static void main( String [ ] args )
 {
 //    AVLTree<Integer> t = new AVLTree<>( );
     final int NUMS = 100000;  // must be even

     System.out.println( "Create the tree..." );
     long x[] = new long[6];    
     x[3]=insertRandom_time(NUMS);
     x[4]=search_time(NUMS);
     x[5]=delete_time(NUMS);
     x[0]=insert_time(NUMS);
     x[1]=search_time(NUMS);
     x[2]=delete_time(NUMS);
 
     
     System.out.println( "The time cost for insert is " + x[0]);  
    
     System.out.println( "The time cost for search is " + x[1]);
        
     System.out.println( "The time cost for delete is " + x[2] );
     System.out.println( "Tree after removals:" );
     a.printTree( );
  
     System.out.println( "The time cost for random insert is " + x[3] );
     
 //    System.out.println( "Tree after insertions:" );
     
     System.out.println( "Second searching...");
     System.out.println( "The time cost for second search is " + x[4]);
     
     
     System.out.println( "The time cost for random delete is " + x[5]);
 }
}
