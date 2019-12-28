/************************************/
/*                                  */
/*   fishingSimulator.c             */
/*                                  */
/*   This program simulates         */
/*   fishers fishing in a pond      */
/*                                  */
/************************************/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

/* Maximum fishes in a fisher's bucket */
#define MAX_CATCH 10
/* Maximum fishes in a pond */
#define MAX_FISH 15

/* Fish Struct */
/* Size in cm */
/* Species as the species name */
typedef struct {
    unsigned char size;
    char *species;
} Fish;

/* Fisher Struct */
/* Name as fisher's name */
/* KeepSize as minimum size want to keep */
/* Bucket to store fishes */
/* Numfish in number of fishes */
typedef struct {
    char *name;
    unsigned char keepSize;
    Fish bucket[MAX_CATCH];   // max 10
    unsigned char numFish;
} Fisher;

/* Pond Struct */
/* Fish to store fishes in pond */
/* Numfish for number of fishes */
typedef struct {
    Fish fish[MAX_FISH];     // max 15
    unsigned char numFish;
} Pond;

/* Function that adds a fish into a given pond */
int addFish(Pond *p, unsigned char size, char *species){
    if (p->numFish == MAX_FISH){
        return 0;
    }else{
        Fish f;
        f.size = size;
        f.species = species;
        p->fish[p->numFish] = f;
        p->numFish ++;
        return 1;
    }
}

/* Function that lists out all fishes in an array of fishes */
void listFish(Fish *arrayOfFish, int n){
    for (int i = 0; i < n; i ++){
        printf("%d cm %s\n", arrayOfFish->size, arrayOfFish->species);
        arrayOfFish ++;
    }
}

/* Function that returns char '1' if fish isn't a Sunfish and within keepsize of fisher */
char likes(Fisher *fisher, Fish *f){
    if (strcmp(f->species, "Sunfish") != 0){
        if (f->size >= fisher->keepSize){
            return '1';
        }
    }
    return '0';
}

/* Function that puts the fish into the fisher's bucket */
int keep(Fisher *fisher, Fish *f){
    if (fisher->numFish != MAX_CATCH){
        fisher->bucket[fisher->numFish] = *f;
        fisher->numFish ++;
        return 1;
    }
    return 0;
}

/* Function that simulates a cast */
/* Fish is added to fisher's bucket if */
/* Pond still has fish, fisher's bucket isn't full */
/* and if fisher likes the fish */
int castLine(Fisher *fisher, Pond *p){
    if ((p->numFish == 0) || (fisher->numFish == MAX_CATCH)){
        return 0;
    }else{
        int random = (rand() % p->numFish); // 0 to 14, if numFish == 15

        if (likes(fisher, &(p->fish[random])) == '1'){
            keep(fisher, &(p->fish[random]));
            p->numFish --;
            p->fish[random] = p->fish[p->numFish];
        }else{
            return 0;
        }
    }
    return 1;
}

/* Fisher1 gives all their fishes to fisher2 */
/* If fisher2 doesn't like it or bucket is full then */
/* fishes goes back into the pond */
void giveAwayFish(Fisher *fisher1, Fisher *fisher2, Pond *p){
    /* While fisher2 still has space */
    while (fisher1->numFish > 0 && (fisher2->numFish < MAX_CATCH)){
        /* If fisher2 likes the last fish in fisher1's bucket */
        if (likes(fisher2, &(fisher1->bucket[fisher1->numFish - 1])) == '1'){
            /* fisher2 keeps the fish */
            keep(fisher2, &(fisher1->bucket[fisher1->numFish - 1]));
        }else{
            p->fish[p->numFish] = fisher1->bucket[fisher1->numFish - 1];
            p->numFish ++;
        }
        /* Decrease numfish for fisher1 */
        fisher1->numFish --;        
    }
    /* If fisher2's bucket is full and cannot keep anymore fishes */
    /* Dump the rest of the fishes into the pond */
    while (fisher1->numFish > 0){
        p->fish[p->numFish] = fisher1->bucket[fisher1->numFish - 1];
        fisher1->numFish --;
        p->numFish ++;
    }
}

int main(){
    srand(time(NULL));
    Fisher fred;
    fred.name = "Fred";
    fred.keepSize = 15;
    fred.numFish = 0;
    Fisher suzy;
    suzy.name = "Suzy";
    suzy.keepSize = 10;
    suzy.numFish = 0;

    Pond p;
    p.numFish = 0;
    addFish(&p, 4, "Sunfish");
    addFish(&p, 25, "Pike");
    addFish(&p, 20, "Bass");
    addFish(&p, 30, "Perch");
    addFish(&p, 14, "Sunfish");
    addFish(&p, 15, "Pike");
    addFish(&p, 9, "Pike");
    addFish(&p, 12, "Bass");
    addFish(&p, 5, "Sunfish");
    addFish(&p, 12, "Sunfish");
    addFish(&p, 10, "Bass");
    addFish(&p, 2, "Bass");
    addFish(&p, 16, "Perch");
    addFish(&p, 30, "Sunfish");
    addFish(&p, 7, "Perch");
    printf("Pond:\n");
    listFish(p.fish, p.numFish);
    printf("\nFred's Bucket\n");
    for (int i = 0; i < 10; i ++){
        castLine(&fred, &p);
    }
    listFish(fred.bucket, fred.numFish);

    printf("\nSuzy's Bucket\n");
    for (int i = 0; i < 10; i ++){
        castLine(&suzy, &p);
    }

    listFish(suzy.bucket, suzy.numFish);
    printf("\n");

    printf("Pond:\n");
    listFish(p.fish, p.numFish);

    giveAwayFish(&suzy, &fred, &p);
    printf("\nFred's Bucket\n");
    listFish(fred.bucket, fred.numFish);
    printf("\nSuzy's Bucket\n");
    listFish(suzy.bucket, suzy.numFish);
    printf("\n");

    printf("Pond:\n");
    listFish(p.fish, p.numFish);

    return 1;
}