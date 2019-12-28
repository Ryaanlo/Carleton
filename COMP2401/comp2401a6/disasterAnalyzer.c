#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "CDD.h"

int compareInt(const void *a, const void *b);

typedef struct{
  char subGroup;
  int eventType;
  int info[4]; // [0] - Fatalities, [1] - Injured/Infected, [2] - Evacuated, [3] - Utilities
  float magnitude;
} Disaster;

int main(){
    FILE *bin;
    // Total number of disasters
    unsigned short int numOfDisasters;
    // Used for storing read contents
    char subGroup;
    char eventType;
    int people;
    float magnitude;
    int total[4]; // [0] - Fatalities, [1] - Injured/Infected, [2] - Evacuated, [3] - Utilities
    // Fatalities per disasters
    int fatalities[20];
    // Evacuations per disasters
    int evacuated[20];
    // Strongest Earthquake
    Disaster strongestEarthquake;
    // For number of time event occurs
    int totalEvent[20];
    // For number of time event occurs
    int totalEventCopy[20];

    // Total and Index
    int mostFatalities;
    int mostEvacuations;
    int fIndex;
    int eIndex;

    // Set values
    for (int i = 0; i < 4; i++){
      total[i] = 0;
    }
    for (int i = 0; i < 20; i ++){
      fatalities[i] = 0;
      evacuated[i] = 0;
      totalEvent[i] = 0;
    }

    // Open file
    bin = fopen("CDD2.bin", "r");
    if (!bin){
        printf("ERROR: Could not open binary file");
        exit(1);
    }

    // Read number of disasters
    fread(&numOfDisasters, sizeof(unsigned short int), 1, bin);
    printf("%d Disasters\n", numOfDisasters);

    // Allocate for array
    Disaster *disastersArray = malloc(sizeof(Disaster) * numOfDisasters);

    // Loop over all Disasters
    for (int i = 0; i < numOfDisasters; i ++){
      // Create Disaster Struct
      Disaster d;

      // Read Event subgroup and set it
      fread(&subGroup, sizeof(char), 1, bin);
      d.subGroup = subGroup;

      // Read Event Type and set it
      fread(&eventType, sizeof(char), 1, bin);
      d.eventType = (int)eventType;

      // Loop over Fatalities, Injured/Infected, Evacuated, Utilities
      for (int j = 0; j < 4; j ++){
        // Read and write it
        fread(&people, sizeof(int), 1, bin);
        d.info[j] = people;
      }

      // If it was an earthquake
      if (eventType == EARTHQUAKE){
        // Read and write it
        fread(&magnitude, sizeof(float), 1, bin);
        d.magnitude = magnitude;
      }else{
        // If not an earthquake just set it to 0
        d.magnitude = 0.0;
      }

      // Add to array
      disastersArray[i] = d;
    }

    // Loop over all Disasters
    for (int i = 0; i < numOfDisasters; i ++){
      // Add to the total sum for Fatalities, Injured/Infected, Evacuated, Utilities
      total[0] += disastersArray[i].info[0];
      total[1] += disastersArray[i].info[1];
      total[2] += disastersArray[i].info[2];
      total[3] += disastersArray[i].info[3];

      // Add the number of fatalities/evacuated to total corresponding to the eventType
      fatalities[disastersArray[i].eventType - 1] += disastersArray[i].info[0];
      evacuated[disastersArray[i].eventType - 1] += disastersArray[i].info[2];

      // Comparing for strongestEarthquake
      if (i == 0){
        strongestEarthquake = disastersArray[i];
      }else{
        if (disastersArray[i].magnitude > strongestEarthquake.magnitude){
          strongestEarthquake = disastersArray[i];
        }
      }

      // Increment counter for every eventType
      totalEvent[disastersArray[i].eventType - 1] ++;
    }

    // Find the mostFatalities & mostEvacuations
    for (int i = 0; i < 20; i ++){
      if (i == 0){
        mostFatalities = fatalities[i];
        mostEvacuations = evacuated[i];
        fIndex = i;
        eIndex = i;
      }else{
        if (fatalities[i] > mostFatalities){
          mostFatalities = fatalities[i];
          fIndex = i;
        }
        if (evacuated[i] > mostEvacuations){
          mostEvacuations = evacuated[i];
          eIndex = i;
        }
      }
    }

    // Print the results
    printf("Total Fatalities: %d\n", total[0]);
    printf("Total Injured/Infected: %d\n", total[1]);
    printf("Total Evacuated: %d\n", total[2]);
    printf("Total Utilities: %d\n", total[3]);
    printf("Most Fatalities is %s with %d fatalities\n", EventTypes[fIndex], mostFatalities);
    printf("Most Evacuations is %s with %d evacuated\n", EventTypes[eIndex], mostEvacuations);
    printf("Strongest Earthquake had a magnitude of %f\n\n", strongestEarthquake.magnitude);

    // Duplicate the Array
    for (int i = 0; i < 20; i ++){
      totalEventCopy[i] = totalEvent[i];
    }

    // Sort the original array
    qsort(totalEvent, 20, sizeof(int), compareInt);

    // Loop over amount of EventTypes twice
    for (int i = 0; i < 20; i ++){
      for (int j = 0; j < 20; j ++){
        // If the sorted array = copied array and if it's not -1
        if (totalEvent[i] == totalEventCopy[j] && totalEventCopy[j] != -1){
          printf("%s happened %d times\n", EventTypes[j], totalEvent[i]);
          // Set the copied array to -1 to indicate empty
          totalEventCopy[j] = -1;
          break;
        }
      }
    }

    // Free the Array
    free(disastersArray);
    // Close the file
    fclose(bin);
}

// For sorting ints
int compareInt(const void *a, const void *b){
  return (*(int *)b - *(int *)a);
}
