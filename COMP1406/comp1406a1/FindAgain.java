package comp1406a1;

/** Assignment 1 - Winter 2019
  * <p>
  * Problem 1
  * <p>
  * In the provided Find.java file, complete the locateSequence method. 
  * For a given target sequence (non-empty array of integers), the method 
  * searches the input array (of integers) to find an occurrence of the 
  * target sequence if it is present. If the sequence is present, the 
  * method returns the array index position of where it starts in the array. 
  * If the sequence is not present, the method returns -1.
  */
public class FindAgain{
  
  /** Finds the last occurrence of the sequence in the array or indicates that 
    * the sequence is not present.
    * 
    * @param sequence is an array of one or more integers. 
    *        It is the target sequence we are looking for.
    * 
    * @param array is an array integers. 
    * 
    * @return the starting position of the last occurrence of the target sequence in the 
    *         array if it exists. Returns -1 otherwise.
    */
  
  public static int[] locateAllSequenceLocations(int[] target, int[] array){
	int total = 0;
	int count = 1;

	int counter = 0;
	for (int i = 0; i<(array.length-target.length+1); i++){
		if (target[0] == array[i]){
			for (int j = 0; j<target.length; j++){
				if (target[j] == array[i+j]){
					counter++;
				}
			}
		if (counter == (target.length)){
			total++;
		}
		}
		counter = 0;
	}
	
	int[] newArray = new int[total+1];
	newArray[0] = total; 
	
	counter = 0;
	for (int i = 0; i<(array.length-target.length+1); i++){
		if (target[0] == array[i]){
			for (int j = 0; j<target.length; j++){
				if (target[j] == array[i+j]){
					counter++;
				}
			}
		if (counter == (target.length)){
			newArray[count] = i;
			count++;
		}
		}
		counter = 0;
	}
	
    return newArray;
  }
  
  public static void main(String[] args){
	int[] target = new int[2];
	target[0] = 5;
	target[1] = 3;
	int[] array = new int[10];
	array[0] = 9;
	array[1] = 5;
	array[2] = 3;
	array[3] = 5;
	array[4] = 0;
	array[5] = 1;
	array[6] = 5;
	array[7] = 3;
	array[8] = 5;
	array[9] = 3;

  }
}