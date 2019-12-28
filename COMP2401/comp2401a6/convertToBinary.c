#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "CDD.h"

int main(){
    FILE *fd;
    FILE *bin;
    char buffer[3000];
    char *read;
    char *data;
    unsigned short int disasters = 0;
    char earthquake = 0;
    float magnitude;
    int zero = 0;
    int number;
    char subgroup[] = {'G','M','B'};

    // Open file for reading
    fd = fopen("CDD2.csv", "r");
    if (!fd){
        printf("ERROR: Could not open file\n");
        exit(1);
    }

    // Open file for writing
    bin = fopen("CDD2.bin", "w");
    if (!bin){
        printf("ERROR: Could not create/open binary file");
        exit(1);
    }

    // Read the first line
    read = fgets(buffer, 3000, fd);

    // Check for amount of disasters
    while (read){
      // Split the buffer, ex. get the first word
      data = strtok(buffer, ",");
      // Check if it's a Disaster
      if (strcmp(data, "Disaster") == 0){
          disasters ++;
      }
      // Get next line
      read = fgets(buffer, 3000, fd);
    }

    printf("Number of Disasters: %hu\n", disasters);
    // Write the number of disasters
    fwrite(&disasters, sizeof(unsigned short int), 1, bin);

    // Go back to the beginning of the file
    rewind(fd);

    // Read the first line
    read = fgets(buffer, 3000, fd);

    // Writing to binary file while not end of file
    while (!feof(fd)){
      // Reset earthquake boolean
      earthquake = 0;

      // Read Event Categories
      fscanf(fd, "%[^,]s", buffer);
      fscanf(fd, ",");

      // If it's a Disaster
      if (strcmp(buffer, "Disaster") == 0){

        // Read over Event Group
        fscanf(fd, "%[^,]s", buffer);
        fscanf(fd, ",");
        // Read Event SubGroup
        fscanf(fd, "%[^,]s", buffer);
        fscanf(fd, ",");

        // Check the SubGroup
        if (strcmp(buffer, "Geological") == 0){
            fwrite(&subgroup[0], sizeof(char), 1, bin);
        }else if(strcmp(buffer, "Meteorological - Hydrological") == 0){
            fwrite(&subgroup[1], sizeof(char), 1, bin);
          }else if(strcmp(buffer, "Biological") == 0){
            fwrite(&subgroup[2], sizeof(char), 1, bin);
        }

        // Event Types
        fscanf(fd, "%[^,]s", buffer);
        fscanf(fd, ",");

        // Loop over all EventTypes
        for (int i = 1; i < 21; i ++){
          // If the read EventType matches
          if (strcmp(buffer, EventTypes[i-1]) == 0){
            // If Earthquake
            if (strcmp(buffer, "Earthquake") == 0){
              earthquake = 1;
            }
            // Write the EventType
            fwrite(&i, sizeof(char), 1, bin);
            break;
          }
        }

        // Place
        fscanf(fd, "%[^,]s", buffer);
        fscanf(fd, ",");

        // Event Start Date
        fscanf(fd, "%[^,]s", buffer);
        fscanf(fd, ",");

        // Fatalities
        // Check if it's valid
        if (fscanf(fd, "%[^,]s", buffer)){
          // Convert to int and write it
          number = atoi(buffer);
          fwrite(&number, sizeof(int), 1, bin);
        }else{
          // Write zero
          fwrite(&zero, sizeof(int), 1, bin);
        }
        fscanf(fd, ",");


        // Injured/Infected
        if (fscanf(fd, "%[^,]s", buffer)){
          // Convert to int and write it
          number = atoi(buffer);
          fwrite(&number, sizeof(int), 1, bin);
        }else{
          // Write zero
          fwrite(&zero, sizeof(int), 1, bin);
        }
        fscanf(fd, ",");

        // Evacuated
        if (fscanf(fd, "%[^,]s", buffer)){
          // Convert to int and write it
          number = atoi(buffer);
          fwrite(&number, sizeof(int), 1, bin);
        }else{
          // Write zero
          fwrite(&zero, sizeof(int), 1, bin);
        }
        fscanf(fd, ",");

        // Skip 10
        for (int i = 0; i < 10; i ++){
          fscanf(fd, "%[^,]s", buffer);
          fscanf(fd, ",");
        }

        // Utility
        if (fscanf(fd, "%[^,]s", buffer)){
          // Convert to int and write it
          number = atoi(buffer);
          fwrite(&number, sizeof(int), 1, bin);
        }else{
          // Write zero
          fwrite(&zero, sizeof(int), 1, bin);
        }
        fscanf(fd, ",");

        // Magnitude
        // If it was an earthquake
        if (earthquake){
          // Read, Convert to float and write it
          fscanf(fd, "%[^\n]s", buffer);
          magnitude = atof(buffer);
          fwrite(&magnitude, sizeof(float), 1, bin);
        }
      }

      // Go to next line
      fscanf(fd, "%[^\n]s", buffer);
      fscanf(fd, "\n");
    }

    printf("Finished Reading and Writing\n");

    // Close all files
    fclose(fd);
    fclose(bin);
}
