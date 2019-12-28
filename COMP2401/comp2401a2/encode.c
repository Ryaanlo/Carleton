#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

#define DATA_BITS    8
#define PARITY_BITS  4
#define CHAR_LIMIT   100


// Encode the given character c into a bitSequence of size (DATA_BITS + PARITY_BITS).
// Each character in bitSequence must be a '1' or '0' character corresponding to one of the
// proper hamming code data bits or parity bits.  For example, if c='A', then the
// resulting string "100010010001" should be stored in bitSequence.
void encode(unsigned char c, char bitSequence[]) {
  /* Array to store data bits */
  char bitArray[8] = "";

  /* Loop over every bit in char c and read each one to set in the bitArray */
  for (int i = 8; i > 0; i --){
    if ((c & (1 << (i - 1))) >> (i - 1) == 1){
      bitArray[8 - i] = '1';
    }else{
      bitArray[8 - i] = '0';
    }
  }

  /* Index for adding data bit */
  int index = 0;
  /* Loop over length of encoded array */
  for (int i = 0; i<PARITY_BITS + DATA_BITS; i++){
    /* For first parity bit */
    if (i == 0){
      /* If the addition of data bit 0, 1, 3, 4, 6 is odd */
      if (((bitArray[0] + bitArray[1] + bitArray[3] + bitArray[4] + bitArray[6]) % 2) == 1){
        /* Parity bit is 1 */
        bitSequence[i] = '1';
      /* If even */
      }else{
        /* Parity bit is 0 */
        bitSequence[i] = '0';
      }
    /* Second parity bit */
    }else if(i == 1){
      /* If odd */
      if (((bitArray[0] + bitArray[2] + bitArray[3] + bitArray[5] + bitArray[6]) % 2) == 1){
        bitSequence[i] = '1';
      /* If even */
      }else{
        bitSequence[i] = '0';
      }
    /* Third parity bit */
    }else if(i == 3){
      /* If odd */
      if (((bitArray[1] + bitArray[2] + bitArray[3] + bitArray[7]) % 2) == 1){
        bitSequence[i] = '1';
      /* If even */
      }else{
        bitSequence[i] = '0';
      }
    /* Forth parity bit */  
    }else if(i == 7){
      /* If odd */
      if (((bitArray[4] + bitArray[5] + bitArray[6] + bitArray[7]) % 2) == 1){
        bitSequence[i] = '1';
      /* If even */
      }else{
        bitSequence[i] = '0';
      }
    /* Placing data bit */
    }else{
      bitSequence[i] = bitArray[index];
      index ++;
    }
  }

  bitSequence[12] = '\0';
}


// This program gets a string of ASCII characters from the user and then encodes each character
// using a 12-bit hamming code scheme, which uses 4 parity bits.  The output is displayed as a single
// string that represents a sequence of bits which has length of 12 times the number of characters
// entered.  The resulting string output consists solely of '1' and '0' characters.
int main() {
  unsigned char   transmitString[CHAR_LIMIT+1];
  unsigned char   encodedCharacters[(12+1)*(CHAR_LIMIT+1)];
  int             charCount;

  // Get the string to encode
  char formatString[10];
  sprintf(formatString, "%%%ds", CHAR_LIMIT);
  scanf(formatString, (char *)transmitString);
  charCount = strlen(transmitString);

  // Encode each character in the string by using a 12-bit hamming code
  for (int i=0; i<charCount; i++) {
    encode(transmitString[i], &encodedCharacters[i]);
    printf("%s", &encodedCharacters[i]);
  }
  printf("\n");  
}
