package searchtrees;

import java.util.Random;

//SplayTree class
//
//CONSTRUCTION: with no initializer
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//void remove( x )       --> Remove x
//boolean contains( x )  --> Return true if x is found
//Comparable findMin( )  --> Return smallest item
//Comparable findMax( )  --> Return largest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//void printTree( )      --> Print tree in sorted order
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
* Implements a top-down splay tree.
* Note that all "matching" is based on the compareTo method.
* @author Mark Allen Weiss
*/
public class SplayTree<AnyType extends Comparable<? super AnyType>>
{
 /**
  * Construct the tree.
  */
 public SplayTree( )
 {
     nullNode = new BinaryNode<AnyType>( null );
     nullNode.left = nullNode.right = nullNode;
     root = nullNode;
 }

 private BinaryNode<AnyType> newNode = null;  // Used between different inserts
 
 /**
  * Insert into the tree.
  * @param x the item to insert.
  */
 public void insert( AnyType x )
 {
     if( newNode == null )
         newNode = new BinaryNode<AnyType>( null );
     newNode.element = x;

     if( root == nullNode )
     {
         newNode.left = newNode.right = nullNode;
         root = newNode;
     }
     else
     {
         root = splay( x, root );
			
			int compareResult = x.compareTo( root.element );
			
         if( compareResult < 0 )
         {
             newNode.left = root.left;
             newNode.right = root;
             root.left = nullNode;
             root = newNode;
         }
         else
         if( compareResult > 0 )
         {
             newNode.right = root.right;
             newNode.left = root;
             root.right = nullNode;
             root = newNode;
         }
         else
             return;   // No duplicates
     }
     newNode = null;   // So next insert will call new
 }

 /**
  * Remove from the tree.
  * @param x the item to remove.
  */
 public void remove( AnyType x )
 {
     if( !contains( x ) )
         return;

     BinaryNode<AnyType> newTree;

         // If x is found, it will be splayed to the root by contains
     if( root.left == nullNode )
         newTree = root.right;
     else
     {
         // Find the maximum in the left subtree
         // Splay it to the root; and then attach right child
         newTree = root.left;
         newTree = splay( x, newTree );
         newTree.right = root.right;
     }
     root = newTree;
 }

 /**
  * Find the smallest item in the tree.
  * Not the most efficient implementation (uses two passes), but has correct
  *     amortized behavior.
  * A good alternative is to first call find with parameter
  *     smaller than any item in the tree, then call findMin.
  * @return the smallest item or throw UnderflowException if empty.
  */
 public AnyType findMin( )
 {
     if( isEmpty( ) )
        return null;

     BinaryNode<AnyType> ptr = root;

     while( ptr.left != nullNode )
         ptr = ptr.left;

     root = splay( ptr.element, root );
     return ptr.element;
 }

 /**
  * Find the largest item in the tree.
  * Not the most efficient implementation (uses two passes), but has correct
  *     amortized behavior.
  * A good alternative is to first call find with parameter
  *     larger than any item in the tree, then call findMax.
  * @return the largest item or throw UnderflowException if empty.
  */
 public AnyType findMax( )
 {
     if( isEmpty( ) )
        return null;

     BinaryNode<AnyType> ptr = root;

     while( ptr.right != nullNode )
         ptr = ptr.right;

     root = splay( ptr.element, root );
     return ptr.element;
 }

 /**
  * Find an item in the tree.
  * @param x the item to search for.
  * @return true if x is found; otherwise false.
  */
 public boolean contains( AnyType x )
 {
     if( isEmpty( ) )
         return false;
			
     root = splay( x, root );

     return root.element.compareTo( x ) == 0;
 }

 /**
  * Make the tree logically empty.
  */
 public void makeEmpty( )
 {
     root = nullNode;
 }

 /**
  * Test if the tree is logically empty.
  * @return true if empty, false otherwise.
  */
 public boolean isEmpty( )
 {
     return root == nullNode;
 }

 private BinaryNode<AnyType> header = new BinaryNode<AnyType>( null ); // For splay
 
 /**
  * Internal method to perform a top-down splay.
  * The last accessed node becomes the new root.
  * @param x the target item to splay around.
  * @param t the root of the subtree to splay.
  * @return the subtree after the splay.
  */
 private BinaryNode<AnyType> splay( AnyType x, BinaryNode<AnyType> t )
 {
     BinaryNode<AnyType> leftTreeMax, rightTreeMin;

     header.left = header.right = nullNode;
     leftTreeMax = rightTreeMin = header;

     nullNode.element = x;   // Guarantee a match

     for( ; ; )
     {
			int compareResult = x.compareTo( t.element );
			
         if( compareResult < 0 )
         {
             if( x.compareTo( t.left.element ) < 0 )
                 t = rotateWithLeftChild( t );
             if( t.left == nullNode )
                 break;
             // Link Right
             rightTreeMin.left = t;
             rightTreeMin = t;
             t = t.left;
         }
         else if( compareResult > 0 )
         {
             if( x.compareTo( t.right.element ) > 0 )
                 t = rotateWithRightChild( t );
             if( t.right == nullNode )
                 break;
             // Link Left
             leftTreeMax.right = t;
             leftTreeMax = t;
             t = t.right;
         }
         else
             break;
     }	

     leftTreeMax.right = t.left;
     rightTreeMin.left = t.right;
     t.left = header.right;
     t.right = header.left;
     return t;
 }

 /**
  * Rotate binary tree node with left child.
  * For AVL trees, this is a single rotation for case 1.
  */
 private static <AnyType> BinaryNode<AnyType> rotateWithLeftChild( BinaryNode<AnyType> k2 )
 {
     BinaryNode<AnyType> k1 = k2.left;
     k2.left = k1.right;
     k1.right = k2;
     return k1;
 }

 /**
  * Rotate binary tree node with right child.
  * For AVL trees, this is a single rotation for case 4.
  */
 private static <AnyType> BinaryNode<AnyType> rotateWithRightChild( BinaryNode<AnyType> k1 )
 {
     BinaryNode<AnyType> k2 = k1.right;
     k1.right = k2.left;
     k2.left = k1;
     return k2;
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

 public void printTree(BinaryNode t)
 {
     if (t != nullNode) {
   		printTree(t.left);
     	System.out.println(t.element);
   		printTree(t.right);
     }
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

 private BinaryNode<AnyType> root;
 private BinaryNode<AnyType> nullNode;
 
 //Q4a: Insert 100,000 integer keys, from 1 to 100,000 (in that order). Find the average time for each insertion. 
 private static SplayTree<Integer> a = new SplayTree<Integer>();
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

  // Test program; should print min and max and nothing else
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

91