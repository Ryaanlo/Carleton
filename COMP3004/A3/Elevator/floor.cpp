#include "floor.h"

int floor::num = 1;

floor::floor()
{
    num++;
    occupied = false;
}

int floor::getFloorNum() {
    return num;
}

bool floor::isOccupied() {
    return occupied;
}

void floor::changeOccupied(){
    if (occupied){
        occupied = false;
    }else{
        occupied = true;
    }
}
