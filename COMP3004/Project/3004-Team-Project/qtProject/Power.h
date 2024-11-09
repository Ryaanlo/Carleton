#ifndef POWER_H
#define POWER_H

#include <string>
#include <iostream>
#include <QObject>
#include <QTimer>

using namespace std;

class Power : public QObject{

    Q_OBJECT

    public:
        Power();

        void powerOn();
        void powerOff();

        void softOff(); //Ending a session ends with softOff

        bool getPower();
    public slots:

    signals:

    private:
        bool poweredOn = false;

};

#endif // POWER_H
