#include "alarm.h"

alarm::alarm()
{

}

void alarm::ring(){
    qInfo("Alarm is ringing!");
    callSafety();
}

void alarm::callSafety(){
    qInfo("Contacting building safety");
}
