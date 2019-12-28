#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

#define DATA_BITS    8
#define PARITY_BITS  4
#define CHAR_LIMIT   100




// Given a brokenSequence of bits representing a corrupted 12-bit (i.e., DATA_BITS + PARITY_BITS)
// hamming code sequence, determine which bit had been flipped and then flip it back and store
// the repaired sequence in fixedSequence.    Note that both bit sequences may be very large,
// but this function should only examine the first 12 bits of the sequence (i.e., the first
// 12 characters in the incoming and outgoing char array).
void fix(char brokenSequence[], char fixedSequence[]) {
  /* Variable for adding the error corresponding to the position */
  int error = 0;
  /* Array to store parity bits */
  char parityBits[4];
  /* Array to store data bits */
  char bitArray[8];
  /* Index for copying parity bits */
  int index1 = 0;
  /* Index for copying data bits */
  int index2 = 0;
  /* Array of indexes of parity bits according to value */
  int parityIndex[] = {1,2,4,8};

  /* Loop of the broken sequence */
  for (int i = 0; i < PARITY_BITS + DATA_BITS; i ++){
    /* If it is in the position of the parity bits */
    if (i == 0 | i == 1 | i == 3 | i == 7 ){
      /* Copy them over */
      parityBits[index1] = brokenSequence[i];
      index1 ++;
    /* If it is position of data bits */
    }else{
      /* Copy them over */
      bitArray[index2] = brokenSequence[i];
      index2 ++;
    }
  }

  /* Arrays for index of data bits that correspond its parity bit */
  /* Variable for total amount of data bits of value one */
  int parityDataBit1[] = {0,1,3,4,6};
  int dataBit1 = 0;
  int parityDataBit2[] = {0,2,3,5,6};
  int dataBit2 = 0;
  int parityDataBit3[] = {1,2,3,7};
  int dataBit3 = 0;
  int parityDataBit4[] = {4,5,6,7};
  int dataBit4 = 0;

  /* Loop over the amount of data bits that correspond to its parity bits */
  for (int i = 0; i < 5; i ++){
    /* If it is equalled to 1 then add to total data bit value */
    if (bitArray[parityDataBit1[i]] == '1'){
      dataBit1 += 1;
    }
    /* If it is equalled to 1 then add to total data bit value */
    if (bitArray[parityDataBit2[i]] == '1'){
      dataBit2 += 1;
    }
  }
  for (int i = 0; i < 4; i ++){
    /* If it is equalled to 1 then add to total data bit value */
    if (bitArray[parityDataBit3[i]] == '1'){
      dataBit3 += 1;
    }
    /* If it is equalled to 1 then add to total data bit value */
    if (bitArray[parityDataBit4[i]] == '1'){
      dataBit4 += 1;
    }
  }

  /* Array for storing total data bits for corresponding parity bit */
  int dataBit[4] = {};
  /* Copying over the data bit into the array */
  dataBit[0] = dataBit1;
  dataBit[1] = dataBit2;
  dataBit[2] = dataBit3;
  dataBit[3] = dataBit4;

  /* Loop over every parity bit */
  for (int i = 0; i < PARITY_BITS; i ++){
    /* If the parity bit is 0 */
    if (parityBits[i] == '0'){
      /* Check if the odd, that means it's wrong */
      if ((dataBit[i] % 2) == 1){
        /* Add the error in terms of the parity bit location */
        error += parityIndex[i];
      }
    /* If the parity bit is 1 */
    }else{
      /* Check if it is even, that means there's an error */
      if ((dataBit[i] % 2) == 0){
        /* Add the error in terms of the parity bit location */
        error += parityIndex[i];
      }
    }
  }

  /* Copy over the broken sequence */
  for (int i = 0; i < PARITY_BITS + DATA_BITS; i ++){
    fixedSequence[i] = brokenSequence[i];
  }

  /* Flip the value of the bit at the location of the error*/
  if (fixedSequence[error - 1] == '0'){
    fixedSequence[error - 1] = '1';
  }else{
    fixedSequence[error - 1] = '0';
  }
}






// This program takes in a corrupted 12-bit hamming code sequence from the user and then
// attempts to correct the corrupted bits.   It is assumed that exactly one bit in each
// of the 12-bit sequences has been flipped.   The input is a large array of characters
// which are all 1's or 0's.   The incoming sequence MUST be a multiple of 12 characters.
// For example, if a data string was entered originally as "ABCD" ... then each character
// in the string would be encoded as 12-bits.  So we would need 48 bits to represent the
// input string.   These 48 bits would be coded as 48 characters consisiting of 1's or
// 0's.   So, the expected input to this program would be a large 48 character string of
// '1' and '0' chars.
int main() {
  unsigned char   brokenString[12*CHAR_LIMIT+1];
  unsigned char   fixedString[12*CHAR_LIMIT+1];
  int             charCount;

  // Get the string to decode
  char formatString[10];
  sprintf(formatString, "%%%ds", 12*CHAR_LIMIT);
  scanf(formatString, (char *)brokenString);
  charCount = strlen(brokenString);

  // Make sure that the string size is a multiple of 12 characters, otherwise something is wrong
  if (charCount%12 != 0) {
    printf("The entered bit sequence must have a multiple of 12 bits.\nThis sequence has %d bits left over.\n", charCount%12);
    exit(-1);
  }

  // Fix the bits in the brokenString by identifying the single flipped bit in each of
  // the sequential sets of 12 bits
  for (int i=0; i<charCount/12; i++)
    fix(&brokenString[i*12], &fixedString[i*12]);
  fixedString[12*(charCount/12)] = '\0';
  printf("%s\n", fixedString);  
}
