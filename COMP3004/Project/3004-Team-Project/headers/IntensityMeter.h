#ifndef INTENSITYMETER_H
#define INTENSITYMETER_H

class IntensityMeter {

public:
    IntensityMeter();
    int getIntensity();
    void changeIntensity();

protected:
    int intensity;
};
#endif