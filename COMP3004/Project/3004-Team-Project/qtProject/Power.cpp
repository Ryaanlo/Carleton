#include "Power.h"

Power::Power(){ }

void Power::powerOn(){
    poweredOn = true;
}

void Power::powerOff(){
    poweredOn = false;
}

bool Power::getPower(){
    return poweredOn;
}
