#include "mainwindow.h"
#include "ui_mainwindow.h"


MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    p = new Power();

}

MainWindow::~MainWindow()
{
    delete ui;
}


void MainWindow::on_powerBtn_clicked()
{
    if(p->getPower()){
        if(dur == 0){
            dur += 1;
            ui->min20lbl->setStyleSheet("QLabel { }");
            ui->min45lbl->setStyleSheet("QLabel { background-color : blue}");
        }
        else if(dur == 1){
            dur += 1;
            ui->min45lbl->setStyleSheet("QLabel { }");
            ui->userDesignedlbl->setStyleSheet("QLabel { background-color : blue}");
        }
        else{
            dur = 0;
            ui->userDesignedlbl->setStyleSheet("QLabel { }");
            ui->min20lbl->setStyleSheet("QLabel { background-color : blue}");
        }
    }
}

//TODO: Implement battery into func such that graph displays battery level
void MainWindow::powerGraph(){
    if(p->getPower()){
        ui->graph1->setStyleSheet("QLabel { background-color : green }");
        ui->graph2->setStyleSheet("QLabel { background-color : green}");
        ui->graph3->setStyleSheet("QLabel { background-color : green }");
        ui->graph4->setStyleSheet("QLabel { background-color : yellow}");
        ui->graph5->setStyleSheet("QLabel { background-color : yellow }");
        ui->graph6->setStyleSheet("QLabel { background-color : yellow}");
        ui->graph7->setStyleSheet("QLabel { background-color : red }");
        ui->graph8->setStyleSheet("QLabel { background-color : red}");
    }
    else{
        ui->graph1->setStyleSheet("QLabel { }");
        ui->graph2->setStyleSheet("QLabel { }");
        ui->graph3->setStyleSheet("QLabel { }");
        ui->graph4->setStyleSheet("QLabel { }");
        ui->graph5->setStyleSheet("QLabel { }");
        ui->graph6->setStyleSheet("QLabel { }");
        ui->graph7->setStyleSheet("QLabel { }");
        ui->graph8->setStyleSheet("QLabel { }");
    }
}

void MainWindow::powerOnOffUI(){
    if(p->getPower()){
        ui->lightlbl->setStyleSheet("QLabel { background-color : green }");
        ui->min20lbl->setStyleSheet("QLabel { background-color : blue}");
        ui->subDeltalbl->setStyleSheet("QLabel { background-color : yellow }");
        ui->upBtn->setEnabled(true);
        ui->downBtn->setEnabled(true);
        ui->selectBtn->setEnabled(true);
    }
    else{
        ui->lightlbl->setStyleSheet("QLabel { background-color : red }");
        ui->min20lbl->setStyleSheet("QLabel { }");
        ui->min45lbl->setStyleSheet("QLabel { }");
        ui->subDeltalbl->setStyleSheet("QLabel { }");
        ui->upBtn->setEnabled(false);
        ui->downBtn->setEnabled(false);
        ui->selectBtn->setEnabled(false);
    }
}

//TODO: Work out bug here if time
void MainWindow::on_powerBtn_pressed()
{
    powerHeld.start();
//    QTimer *timer = new QTimer(this);
//    if(p->getPower() == false){
//        connect(timer, SIGNAL(timeout()), this, SLOT(powerLightOn()));
//        timer->start(1500);
//    }
//    else{
//        connect(timer, SIGNAL(timeout()), this, SLOT(powerLightOff()));
//        timer->start(3200);
//    }
}

void MainWindow::on_powerBtn_released()
{
       if(p->getPower() == false){
           if(powerHeld.elapsed() > 1500){
               dur = -1;
               p->powerOn();
               powerOnOffUI();
               powerGraph();
           }
       }
       else if(p->getPower() == true){
           if(powerHeld.elapsed() > 3000){
               p->powerOff();
               powerOnOffUI();
               powerGraph();
           }
       }
}

void MainWindow::powerLightOn(){
    if(p->getPower() == false){
        ui->lightlbl->setStyleSheet("QLabel { background-color : darkCyan }");
    }
}

void MainWindow::powerLightOff(){
    if(p->getPower() == true){
        ui->lightlbl->setStyleSheet("QLabel { background-color : red }");
    }
}

void MainWindow::on_upBtn_clicked()
{
    if(!inSession){
        if(session == 0){
            session += 1;
            ui->subDeltalbl->setStyleSheet("QLabel { }");
            ui->deltalbl->setStyleSheet("QLabel { background-color : yellow}");
        }
        else if(session == 1){
            session += 1;
            ui->deltalbl->setStyleSheet("QLabel { }");
            ui->thetalbl->setStyleSheet("QLabel { background-color : yellow}");
        }
        else if(session == 2){
            session += 1;
            ui->thetalbl->setStyleSheet("QLabel { }");
            ui->alphalbl->setStyleSheet("QLabel { background-color : yellow}");
        }
        else{
            session = 0;
            ui->alphalbl->setStyleSheet("QLabel { }");
            ui->subDeltalbl->setStyleSheet("QLabel { background-color : yellow}");
        }
    }
}

void MainWindow::on_downBtn_clicked()
{
    if(!inSession){
        if(session == 0){
            session = 3;
            ui->alphalbl->setStyleSheet("QLabel { background-color : yellow }");
            ui->subDeltalbl->setStyleSheet("QLabel { }");
        }
        else if(session == 1){
            session -= 1;
            ui->subDeltalbl->setStyleSheet("QLabel { background-color : yellow }");
            ui->deltalbl->setStyleSheet("QLabel { }");
        }
        else if(session == 2){
            session -= 1;
            ui->deltalbl->setStyleSheet("QLabel { background-color : yellow }");
            ui->thetalbl->setStyleSheet("QLabel { }");
        }
        else{
            session -= 1;
            ui->thetalbl->setStyleSheet("QLabel { background-color : yellow }");
            ui->alphalbl->setStyleSheet("QLabel { }");
        }
    }
}
