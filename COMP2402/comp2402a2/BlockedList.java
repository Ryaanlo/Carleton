package comp2402a2;

import java.util.AbstractList;
//import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a copy of the JCF class ArrayList.  It implements the List
 * interface as a single array a.  Elements are stored at positions
 * a[0],...,a[size()-1].  Doubling/halving is used to resize the array
 * a when necessary.
 * @author morin
 *
 * @param <T> the type of objects stored in the List
 */
public class BlockedList<T> extends AbstractList<T> {
	/**
	 * keeps track of the class of objects we store
	 */
	Factory<T> f;

	/**
	 * The number of elements stored
	 */
	int n;

	/**
	 * The block size
	 */
	int b;

	/**
	 *	ArrayQueue as a Block Data Structure
	 */
	ArrayDeque<T> deque;
	
	/**
	 * Constructor
	 * @param t the type of objects that are stored in this list
	 * @param b the block size
	 */
	public BlockedList(Class<T> t, int b) {
		// Deque of lists?
		f = new Factory<T>(t);
		n = 0;

		deque = new ArrayDeque<T>(t);	
	}

	public int size() {
		return n;
	}

	public T get(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();


		return deque.get(i);
	}

	public T set(int i, T x) {
		// TODO: Implement this
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();

		return deque.set(i,x);
	}

	public void add(int i, T x) {
		// TODO: Implement this
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();

		deque.add(i,x);

		n ++;
	}

	public T remove(int i) {
		// TODO: Implement this
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();


		n --;
		return deque.remove(i);
	}

	public static void main(String[] args){

		//BlockedList<Integer> b = new BlockedList<Integer>(Integer.class, 3);
		//b.add(0, 3);
		//System.out.println("size is " + b.size());
	}
}
