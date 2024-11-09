#include <QObject>
#include <iostream>
#include <string>

using namespace std;

class Power : public QObject {

  Q_OBJECT

public:
  Power();

  void powerOn();
  void powerOff();

  void softOff(); // Ending a session ends with softOff

public slots:

signals:

private:
  bool poweredOn = false;
};