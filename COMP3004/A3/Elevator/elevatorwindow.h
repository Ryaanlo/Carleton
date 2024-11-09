#ifndef ELEVATORWINDOW_H
#define ELEVATORWINDOW_H

#include <QDialog>
#include <QTimer>
#include "alarm.h"

namespace Ui {
class elevatorwindow;
}

class elevatorwindow : public QDialog
{
    Q_OBJECT

public:
    explicit elevatorwindow(QWidget *parent = nullptr);
    ~elevatorwindow();

    void changeFloor();
    int getFloor();

private:
    Ui::elevatorwindow *ui;
    QTimer *doorTimer;
    alarm *bell;

    static int id;
    int floor;

private slots:
    void floor1();
    void floor2();
    void floor3();
    void floor4();
    void floor5();
    void floor6();
    void openDoor();
    void closeDoor();
    void moveToSafeFloor();
    void sendHelp();

};

#endif // ELEVATORWINDOW_H
