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
public class Find{
  
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
  
  public static int locateSequence(int[] sequence, int[] array){
	int location = -1;
	int counter = 0;
	for (int i = 0; i<(array.length-sequence.length+1); i++){
		if (sequence[0] == array[i]){
			for (int j = 0; j<sequence.length; j++){
				if (sequence[j] == array[i+j]){
					counter++;
				}
			}
		if (counter == (sequence.length)){
			location = i;
		}
		}
		counter = 0;
	}
	
    return location;
  }
  
  public static void main(String[] args){
	int[] sequence = new int[3];
	int[] array = new int[10];
	sequence[0] = 1;
	sequence[1] = 4;
	sequence[2] = 3;
	array[0] = 3;
	array[1] = 2;
	array[2] = 1;
	array[3] = 4;
	array[4] = 0;
	array[5] = 1;
	array[6] = 4;
	array[7] = 3;
	array[8] = 1;
	array[9] = 1;

	System.out.println(locateSequence(sequence,array));
  }
}