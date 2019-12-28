/************************************************/
/*                                              */
/* cards.c                                      */
/*                                              */
/* This program takes a trump suit              */
/* and 4 cards for 4 different players then     */
/* compares the cards to see which player wins. */
/*                                              */
/************************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/* Array of all ranks */
char rank[13] = {'2','3','4','5','6','7','8','9','T','J','Q','K','A'};
/* Array of all suits */
char suit[4] = {'H','D','S','C'};
/* Array to store player's cards */
char cards[4][3];

/* Checks if char is a valid rank             */
/* Returns 1 if char is valid and 0 otherwise */
char isValidRank(char c){
    for (int i = 0; i < sizeof(rank)/sizeof(char); i++){
        if (c == rank[i]){
            return 1;
        }
    }
    return 0;
}

/* Checks if char is a valid suit             */
/* Returns 1 if char is valid and 0 otherwise */
char isValidSuit(char c){
    for (int i = 0; i < sizeof(suit)/sizeof(char); i++){
        if (c == suit[i]){
            return 1;
        }
    }
    return 0;
}

/* Gets a trump suit from user */
/* Returns a valid trump suit  */
char getTrump(){
    char input[64] = "";
    char validTrump = 1;
    while (validTrump){
        printf("Please enter a Trump Suit: \n");
        scanf("%s", input);
        getchar();
        /* Check for input of one char */
        if (strlen(input) == 1){
            /* If valid then end loop */
            if(isValidSuit(input[0]) == 1){
                validTrump = 0;
            }else{
                printf("Invalid Suit, please try again.\n");
            }
        }else{
            printf("Too many or too little characters, please try again.\n");
        }
    }
    return input[0];
}
//scanf("%[^\n]", input)
/* Gets a valid card from user */
void validCard(int index, char player[]){
    char validCard = 1;
    char input[64] = "";
    while(validCard){
        printf("Enter card rank and suit for Player %s:\n", player);
        scanf("%s", input);
        getchar();
        /* Check for input of two chars */
        if (strlen(input) == 2){
            /* if valid rank and valid suit */
            if (isValidRank(input[0]) == 1 && isValidSuit(input[1]) == 1){
                strcpy(cards[index], input);
                break;
            }else{
                printf("Invalid card, please try again\n");
            }
        }else if(input[0] == '.'){
            exit(1);
        }else{
            printf("Invalid card, please try again\n");
        }
    }
}

/* Checks to see if card has the trump suit */
/* Returns 1 if trump suit and 0 otherwise  */
int isTrumpSuit(int index, char trump){
    if (cards[index][1] == trump){
        return 1;
    }else{
        return 0;
    }
}

/* Compares the rakn between 2 cards  */
/* Returns 1 if first card is greater */
/* and 0 if it is smaller             */
int compareRank(int cardOne, int cardTwo){
    int cardOneRank;
    int cardTwoRank;
    for (int i = 0; i < sizeof(rank)/sizeof(char); i ++){
        if (cards[cardOne][0] == rank[i]){
            cardOneRank = i;
        }
        if (cards[cardTwo][0] == rank[i]){
            cardTwoRank = i;
        }
    }
    /* Compares rank according to index from ranks array */
    if (cardOneRank > cardTwoRank){
        return 1;
    }else{
        return 0;
    }
}

/* Determines the winner */
/* Returns the index of  */
/* the winning player    */
int determineWinner(char trump){
    int trumpCards[4];
    int numOfTrump = 0;

    int highest = 0;
    int boolean = 0;
    char suitLed = cards[0][1];

    /* Array with values that indicates */
    /* whether trump card or not        */
    for (int i = 0; i < 4; i ++){
        if (isTrumpSuit(i, trump) == 1){
            trumpCards[i] = 1;
            numOfTrump ++;
        }else{
            trumpCards[i] = 0;
        }
    }
    
    /* If there is only one trump card then */
    /* get the index of the trump card      */
    if (numOfTrump == 1){
        for (int i = 0; i < 4; i ++){
            if (trumpCards[i] == 1){
                highest = i;
            }
        }
    /* If there are more than one trump card then */
    /* compare with other trump cards             */
    }else if(numOfTrump > 1){
         for (int i = 0; i < 4; i ++){
            if (trumpCards[i] == 1){
                if (boolean == 0){
                    highest = i;
                    boolean = 1;
                }else{
                    if (compareRank(i, highest) == 1){
                        highest = i;
                    }
                }
            }
        }
    /* If there are no trump cards then */
    /* check for matching suitled then  */
    /* compare all suitled cards        */       
    }else{
        for (int i = 1; i < 4; i ++){
            if (suitLed == cards[i][1]){
                if (compareRank(i, highest) == 1){
                    highest = i;
                }
            }
        }
    }
    return highest;
}

int main(){

    // infinite main loop char 
    char mainLoop = 1;

    /* Get trump suit */
    char trump = getTrump();

    // infinite loop
    while (mainLoop){

    /* Get all player cards */
    validCard(0, "One");
    validCard(1, "Two");
    validCard(2, "Three");
    validCard(3, "Four");

    /* Display all player cards */
    printf("The cards are %s, %s, %s, %s\n", cards[0], cards[1], cards[2], cards[3]);

    /* Determine winner */
    int highest = determineWinner(trump);

    /* Display winner */
    printf("Winner is Player %d!\n\n", highest + 1);

    }
}
