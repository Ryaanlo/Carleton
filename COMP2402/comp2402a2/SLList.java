package comp2402a2;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.Queue;
import java.util.NoSuchElementException;

/**
 * An implementation of a FIFO Queue as a singly-linked list.
 * This also includes the stack operations push and pop, which
 * operate on the head of the queue
 * @author morin
 *
 * @param <T> the class of objects stored in the queue
 */
public class SLList<T> extends AbstractList<T> implements Queue<T> {
	class Node {
		T x;
		Node next;
	}

	/**
	 * Front of the queue
	 */
	Node head;

	/**
	 * Tail of the queue
	 */
	Node tail;

	/**
	 * The number of elements in the queue
	 */
	int n;

	public T get(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		// if get at size
		Node node = head;
		if (i == 0){
			return head.x;
		}else{
			for (int j = 0; j < i; j ++){
				node = node.next;
			}
		}
		return node.x;
	}

	public T set(int i, T x) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();

		Node newNode = new Node();
		Node replaceNode;
		newNode.x = x;
		// If there's one element
		if (n == 1){
			replaceNode = head;
			head = newNode;
			tail = newNode;
		// If there's more than one element
		}else{
			// If it's setting the head
			if (i == 0){
				replaceNode = head;
				newNode.next = head.next;
				head = newNode;
			// If setting after head
			}else{
				Node node = head;
				// Loop till before the node that is being set
				for (int j = 0; j < (i - 1); j ++){
					node = node.next;
				}
				replaceNode = node.next;

				if(i == (n - 1)){
					tail = newNode;
					node.next = tail;
				}else{
					newNode.next = replaceNode.next;
					node.next = newNode;
				}
			}
		}
		return replaceNode.x;
	}

	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();

		Node newNode = new Node();
		newNode.x = x;

		// If adding with no elements or adding at last position
		if ((n == 0) || (i == n)){
			add(x);
		}else{
			// If adding at the front
			if (i == 0){
				push(x);
			// If adding in the middle
			}else{
				Node node = head;
				// Loop till one element 
				for (int j = 0; j < (i - 1); j ++){
					node = node.next;
				}

				newNode.next = node.next;
				node.next = newNode;
				n ++;
			}
		}
	}

	public T remove(int i) {
		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
		Node removed;

		// If only one element
		if (n == 1){
			removed = head;
			head = null;
			tail = null;
		}else{
			if (i == 0){
				return pop();
			}else{
				Node node = head;

				// Loop till before the node to be removed
				for (int j = 0; j < (i - 1); j ++){
					node = node.next;
				}

				if (i == (n - 1)){
					removed = tail;
					tail = node;
					tail.next = null;
				}else{
					removed = node.next;
					node.next = node.next.next;
				}
			}
		}
		n --;
		return removed.x;
	}

	public void reverse() {
		
		if (n > 1){
			Node prev = null;
			Node next = null;

			Node h = this.head;
			Node currentNode = this.head;
			while (currentNode != null){
				next = currentNode.next;
				currentNode.next = prev;
				prev = currentNode;
				currentNode = next;
			}

			this.head = prev;
			this.tail = h;
		}
	}

	public Iterator<T> iterator() {
		class SLIterator implements Iterator<T> {
			protected Node p;

			public SLIterator() {
				p = head;
			}
			public boolean hasNext() {
				return p != null;
			}
			public T next() {
				T x = p.x;
				p = p.next;
				return x;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new SLIterator();
	}

	public int size() {
		return n;
	}

	public boolean add(T x) {
		Node u = new Node();
		u.x = x;
		if (n == 0) {
			head = u;
		} else {
			tail.next = u;
		}
		tail = u;
		n++;
		return true;
	}

	public boolean offer(T x) {
		return add(x);
	}

	public T peek() {
		if (n == 0) return null;
		return head.x;
	}

	public T element() {
		if (n == 0) throw new NoSuchElementException();
		return head.x;
	}

	public T poll() {
		if (n == 0)
			return null;
		T x = head.x;
		head = head.next;
		if (--n == 0)
			tail = null;
		return x;
	}

	/**
	 * Stack push operation - push x onto the head of the list
	 * @param x the element to push onto the stack
	 * @return x
	 */
	public T push(T x) {
		Node u = new Node();
		u.x = x;
		u.next = head;
		head = u;
		if (n == 0)
			tail = u;
		n++;
		return x;
	}

	protected void deleteNext(Node u) {
		if (u.next == tail)
			tail = u;
		u.next = u.next.next;
	}

	protected void addAfter(Node u, Node v) {
		v.next = u.next;
		u.next = v;
		if (u == tail)
			tail = v;
	}

	protected Node getNode(int i) {
		Node u = head;
		for (int j = 0; j < i; j++)
			u = u.next;
		return u;
	}

	/**
	 * Stack pop operation - pop off the head of the list
	 * @return the element popped off
	 */
	public T remove() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}

	public T pop() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}


	public static void main(String[] args) {

	}
}
