#ifndef BATTERY_H
#define BATTERY_H
#include <iostream>
#include <stdlib.h>
#include <string>
using namespace std;

class Battery {
public:
  // constructor
  Battery(int level);
  void replaceBattery();
  void decreaseBatteryLevel(); // should handle battery depletion as a function
                               // of length of therapy, intensity, and
                               // connection to skin
  void newBattery(); //user inserts new battery
  void lowBatteryWarning();
  // getters
  int getBatteryLevel() { return batteryLevel; }

  // setters
  void setBatteryLevel(int level) { batteryLevel = level; }

protected:
  int batteryLevel;
};
#endif // BATTERY_H
