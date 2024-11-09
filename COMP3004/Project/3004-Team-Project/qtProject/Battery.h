#ifndef BATTERY_H
#define BATTERY_H

#include <string>
#include <iostream>
#include <QObject>
#include <math.h>

using namespace std;

class Battery : public QObject{

    Q_OBJECT

    public:
        Battery();
        void depleteBattery(int len, int intensity); //TODO: Figure how to work in "Connection to skin" as an argument
        void criticalLow();
        void replace();

        int getLevel();

    public slots:

    signals:

    private:
        int level; //Levels will range from 1 to 8

};

#endif // BATTERY_H
