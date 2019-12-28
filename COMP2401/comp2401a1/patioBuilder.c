/*************************************************/
/*                                               */
/* patioBuilder.c                                */
/*                                               */
/* This program takes a length and a width       */
/* and computes how many curbs and pavers needed */
/* as well as the total cost for all of          */
/* the curbs and pavers                          */
/*                                               */
/*************************************************/

#include <stdio.h>
#include <math.h>

int main(){
	/* User input size */
    float width; // feet
    float length; // feet

	/* Ammount of curbs and pavers needed */
    float curb;
    float paver;
    
	/* Prices */
    float curbcost;
    float pavercost;
    float hst;
    float totalcost;

	/* Get length and width from user */
    printf("Enter the width of the patio (in feet): ");
    scanf("%f", &width);

    printf("Enter the length of the patio (in feet): ");
    scanf("%f", &length);

	/* Convert to inches */
    width = width * 12;
    length = length * 12;
    
	/* Check if size if too small */
    if (width < 24 || length < 24){
    	printf("Patio dimentions are not big enough :(\n");
    }else{
    	curb = ((int)ceil(length / 11.8) * 2) + (((int)ceil((width - (4.3 * 2)) / 11.8)) * 2); // get amount for top then multiply by 2 then get one side and multiply by 2
    	paver = ((int)ceil((width - (4.3 * 2)) / 7.75) * ((int)ceil((length - (4.3 * 2)) / 7.75))); // get amount for both length and width then multiply together
    	printf("Number of curbs needed is %d\n", (int)curb);
    	printf("Number of pavers needed is %d\n", (int)paver);

    	/* Price Calculations */
    	curbcost = curb * 2.48;
        pavercost = paver * 3.90;
        totalcost = (curbcost + pavercost) * 1.13;
        hst = totalcost - curbcost - pavercost;
		
		/* Display price */
    	printf("Cost for curbs: is $%.2f\nCost or pavers: is $%.2f\nHST: $%.2f\nTotal: $%.2f\n", curbcost, pavercost, hst, totalcost);
    }
    
}
