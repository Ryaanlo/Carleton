#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTimer>
#include <QElapsedTimer>
#include "Power.h"

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void powerOnOffUI();

    void powerGraph();
    void powerLightOn();
    void powerLightOff();

    void on_powerBtn_clicked();

    void on_powerBtn_pressed();

    void on_powerBtn_released();

    void on_upBtn_clicked();

    void on_downBtn_clicked();

private:
    Ui::MainWindow *ui;
    QElapsedTimer powerHeld;
    Power* p;
    bool inSession = false;
    int dur = 2;
    int session = 0;

};
#endif // MAINWINDOW_H
