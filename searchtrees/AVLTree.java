package searchtrees;

import java.util.Random;

import javax.security.auth.x500.X500Principal;

//AvlTree class
//
//CONSTRUCTION: with no initializer
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//void remove( x )       --> Remove x (unimplemented)
//boolean contains( x )  --> Return true if x is present
//boolean remove( x )    --> Return true if x was present
//Comparable findMin( )  --> Return smallest item
//Comparable findMax( )  --> Return largest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//void printTree( )      --> Print tree in sorted order
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
* Implements an AVL tree.
* Note that all "matching" is based on the compareTo method.
* @author Mark Allen Weiss
*/
public class AVLTree<AnyType extends Comparable<? super AnyType>>
{
 /**
  * Construct the tree.
  */
 public AVLTree( )
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
  * Internal method to remove from a subtree.
  * @param x the item to remove.
  * @param t the node that roots the subtree.
  * @return the new root of the subtree.
  */
 private AvlNode<AnyType> remove( AnyType x, AvlNode<AnyType> t )
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
     return balance( t );
 }
 
 /**
  * Find the smallest item in the tree.
  * @return smallest item or null if empty.
  */
 public AnyType findMin( )
 {
     if( isEmpty( ) )
         return null; //throw new UnderflowException( );
     return findMin( root ).element;
 }

 /**
  * Find the largest item in the tree.
  * @return the largest item of null if empty.
  */
 public AnyType findMax( )
 {
     if( isEmpty( ) )
         return null; //throw new UnderflowException( );
     return findMax( root ).element;
 }

 /**
  * Find an item in the tree.
  * @param x the item to search for.
  * @return true if x is found.
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

 private static final int ALLOWED_IMBALANCE = 1;
 
 // Assume t is either balanced or within one of being balanced
 private AvlNode<AnyType> balance( AvlNode<AnyType> t )
 {
     if( t == null )
         return t;
     
     if( height( t.left ) - height( t.right ) > ALLOWED_IMBALANCE )
         if( height( t.left.left ) >= height( t.left.right ) )
             t = rotateWithLeftChild( t );
         else
             t = doubleWithLeftChild( t );
     else
     if( height( t.right ) - height( t.left ) > ALLOWED_IMBALANCE )
         if( height( t.right.right ) >= height( t.right.left ) )
             t = rotateWithRightChild( t );
         else
             t = doubleWithRightChild( t );

     t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
     return t;
 }
 
 public void checkBalance( )
 {
     checkBalance( root );
 }
 
 private int checkBalance( AvlNode<AnyType> t )
 {
     if( t == null )
         return -1;
     
     if( t != null )
     {
         int hl = checkBalance( t.left );
         int hr = checkBalance( t.right );
         if( Math.abs( height( t.left ) - height( t.right ) ) > 1 ||
                 height( t.left ) != hl || height( t.right ) != hr )
             System.out.println( "OOPS!!" );
     }
     
     return height( t );
 }
 
 
 /**
  * Internal method to insert into a subtree.
  * @param x the item to insert.
  * @param t the node that roots the subtree.
  * @return the new root of the subtree.
  */
 private AvlNode<AnyType> insert( AnyType x, AvlNode<AnyType> t )
 {
     if( t == null )
         return new AvlNode<>( x, null, null );
     
     int compareResult = x.compareTo( t.element );
     
     if( compareResult < 0 )
         t.left = insert( x, t.left );
     else if( compareResult > 0 )
         t.right = insert( x, t.right );
     else
         ;  // Duplicate; do nothing
     return balance( t );
 }

 /**
  * Internal method to find the smallest item in a subtree.
  * @param t the node that roots the tree.
  * @return node containing the smallest item.
  */
 private AvlNode<AnyType> findMin( AvlNode<AnyType> t )
 {
     if( t == null )
         return t;

     while( t.left != null )
         t = t.left;
     return t;
 }

 /**
  * Internal method to find the largest item in a subtree.
  * @param t the node that roots the tree.
  * @return node containing the largest item.
  */
 private AvlNode<AnyType> findMax( AvlNode<AnyType> t )
 {
     if( t == null )
         return t;

     while( t.right != null )
         t = t.right;
     return t;
 }

 /**
  * Internal method to find an item in a subtree.
  * @param x is item to search for.
  * @param t the node that roots the tree.
  * @return true if x is found in subtree.
  */
 private boolean contains( AnyType x, AvlNode<AnyType> t )
 {
     while( t != null )
     {
         int compareResult = x.compareTo( t.element );
         
         if( compareResult < 0 )
             t = t.left;
         else if( compareResult > 0 )
             t = t.right;
         else
             return true;    // Match
     }

     return false;   // No match
 }

 /**
  * Internal method to print a subtree in sorted order.
  * @param t the node that roots the tree.
  */
 private void printTree( AvlNode<AnyType> t )
 {
     if( t != null )
     {
         printTree( t.left );
         System.out.println( t.element );
         printTree( t.right );
     }
 }

 /**
  * Return the height of node t, or -1, if null.
  */
 private int height( AvlNode<AnyType> t )
 {
     return t == null ? -1 : t.height;
 }

 /**
  * Rotate binary tree node with left child.
  * For AVL trees, this is a single rotation for case 1.
  * Update heights, then return new root.
  */
 private AvlNode<AnyType> rotateWithLeftChild( AvlNode<AnyType> k2 )
 {
     AvlNode<AnyType> k1 = k2.left;
     k2.left = k1.right;
     k1.right = k2;
     k2.height = Math.max( height( k2.left ), height( k2.right ) ) + 1;
     k1.height = Math.max( height( k1.left ), k2.height ) + 1;
     return k1;
 }

 /**
  * Rotate binary tree node with right child.
  * For AVL trees, this is a single rotation for case 4.
  * Update heights, then return new root.
  */
 private AvlNode<AnyType> rotateWithRightChild( AvlNode<AnyType> k1 )
 {
     AvlNode<AnyType> k2 = k1.right;
     k1.right = k2.left;
     k2.left = k1;
     k1.height = Math.max( height( k1.left ), height( k1.right ) ) + 1;
     k2.height = Math.max( height( k2.right ), k1.height ) + 1;
     return k2;
 }

 /**
  * Double rotate binary tree node: first left child
  * with its right child; then node k3 with new left child.
  * For AVL trees, this is a double rotation for case 2.
  * Update heights, then return new root.
  */
 private AvlNode<AnyType> doubleWithLeftChild( AvlNode<AnyType> k3 )
 {
     k3.left = rotateWithRightChild( k3.left );
     return rotateWithLeftChild( k3 );
 }

 /**
  * Double rotate binary tree node: first right child
  * with its left child; then node k1 with new right child.
  * For AVL trees, this is a double rotation for case 3.
  * Update heights, then return new root.
  */
 private AvlNode<AnyType> doubleWithRightChild( AvlNode<AnyType> k1 )
 {
     k1.right = rotateWithLeftChild( k1.right );
     return rotateWithRightChild( k1 );
 }

 private static class AvlNode<AnyType>
 {
         // Constructors
     AvlNode( AnyType theElement )
     {
         this( theElement, null, null );
     }

     AvlNode( AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt )
     {
         element  = theElement;
         left     = lt;
         right    = rt;
         height   = 0;
     }

     AnyType           element;      // The data in the node
     AvlNode<AnyType>  left;         // Left child
     AvlNode<AnyType>  right;        // Right child
     int               height;       // Height
 }

   /** The tree root. */
 private AvlNode<AnyType> root;

 //Q4a: Insert 100,000 integer keys, from 1 to 100,000 (in that order). Find the average time for each insertion. 
 private static AVLTree<Integer> a = new AVLTree<Integer>();
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
		//	System.out.println("key " + i + " not found" );
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
     final int NUMS = 1000000;  // must be even

     System.out.println( "Create the tree..." );
     long x[] = new long[6];    x[3]=insertRandom_time(NUMS);
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
