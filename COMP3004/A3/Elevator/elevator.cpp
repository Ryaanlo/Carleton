#include "elevator.h"

int elevator::id = 0;

elevator::elevator()
{
    id++;
    floor=1;
}

void elevator::changeFloor(int i) {
    floor = i;
}

int elevator::getFloor() {
    return floor;
}
