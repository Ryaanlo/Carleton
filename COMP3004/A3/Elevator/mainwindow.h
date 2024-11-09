#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <vector>
#include <QMainWindow>
#include "elevatorwindow.h"
#include <QTimer>
#include "elevator.h"
#include "floor.h"

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    elevatorwindow *newWindow;
    QTimer *timer;
    elevator *newElevator;
    QVector<floor*> floors;

private slots:
    void changeWindow();
    void goUp();
    void goDown();

};
#endif // MAINWINDOW_H
