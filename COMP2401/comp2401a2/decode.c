#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

#define DATA_BITS    8
#define PARITY_BITS  4
#define CHAR_LIMIT   100





// Given a bitSequence[] of chars that represent bits of a 12-bit (i.e., DATA_BITS + PARITY_BITS) 
// hamming code sequence, determine the byte/character represented by the data bits of the 12-bit
// sequence and return this character.  If, for example, the incoming bitSequence is the string
// "100010010001", then the letter 'A' should be returned.
char decode(char bitSequence[]) {
  /* Array for data bits */
  char decode[8] = ""; 
  /* Variable for ascii decimal */
  int decimal = 0;
  /* Bits variable to be added */
  int bit = 128;
  /* Index for data bits */
  int index = 0;
  /* Loop over encoded array */
  for (int i = 0; i < PARITY_BITS + DATA_BITS; i ++){
    /* If it is not in the position of parity bits */
    if (i != 0 && i != 1 && i != 3 && i != 7){
      /* Set data array */
      decode[index] = bitSequence[i];
      index ++;
    }
  }

  /* Loop over data array */
  for (int i = 0; i < DATA_BITS; i ++){
    /* If it's a 1 */
    if (decode[i] == '1'){
      /* Add the corresponding bit value */
      decimal += bit;
    }
    /* Divide bit value by 2 each time */
    bit /= 2;
  }

  return (char) decimal;
}






// This program gets a bit sequence in the form of an array of '1' amd '0' characters that
// represent a string that has been encoded using a 12-bit hamming code scheme.   The incoming
// string must be a multiple of 12 bytes/characters in size, as each 12-byte sequence will
// represent a single encoded character from the original user string.  The output is displayed
// as the decoded string, which should match the original string that had been encoded.
int main() {
  unsigned char   recvString[12*CHAR_LIMIT+1];
  unsigned char   decodedCharacters[CHAR_LIMIT];
  int             charCount;

  // Get the string to decode
  char formatString[10];
  sprintf(formatString, "%%%ds", 12*CHAR_LIMIT);
  scanf(formatString, (char *)recvString);
  charCount = strlen(recvString);

  // Make sure that the string size is a multiple of 12 characters, otherwise something is wrong
  if (charCount%12 != 0) {
    printf("The entered bit sequence must have a multiple of 12 bits.\nThis sequence has %d bits left over.\n", charCount%12);
    exit(-1);
    }

  // Decode the bit sequence using a 12-bit hamming code and display the characters
  for (int i=0; i<charCount/12; i++) {
    decodedCharacters[i] = decode(&recvString[i*12]);
    printf("%c",decodedCharacters[i]);
  }
  printf("\n");  
}
