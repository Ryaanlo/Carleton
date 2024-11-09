#include "Battery.h"

Battery::Battery(){
    level = 10;
}

void Battery::replace(){
    level = 10;
}

void Battery::depleteBattery(int len, int intensity){
    float sub = .1;

    if(len < 20){
        sub += 1;
    }
    else if(len == 20){
        sub += .2;
    }
    else if(len >= 30 && len < 45){
        sub += .25;
    }
    else if(len == 45){
        sub += .3;
    }
    else{
        sub += 3.5;
    }

    if(intensity <= 2){
        sub += .7;
    }
    else if(intensity <= 4){
        sub += .9;
    }
    else if(intensity <= 6){
        sub += 1.1;
    }
    else{
        sub += 1.3;
    }

    level -= round(sub);
}

int Battery::getLevel(){
    return level;
}
