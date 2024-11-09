#include "elevatorwindow.h"
#include "ui_elevatorwindow.h"

int elevatorwindow::id = 1;

elevatorwindow::elevatorwindow(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::elevatorwindow)
{
    ui->setupUi(this);

    id++;
    floor=1;

    ui->label->setText(QString::number(floor));

    // Timer for door closing
    doorTimer = new QTimer();
    connect(doorTimer, &QTimer::timeout, this, &elevatorwindow::changeFloor);

    // Floor 1-6 buttons
    connect(ui->pushButton, SIGNAL(released()), this, SLOT (floor1()));
    connect(ui->pushButton_2, SIGNAL(released()), this, SLOT (floor2()));
    connect(ui->pushButton_3, SIGNAL(released()), this, SLOT (floor3()));
    connect(ui->pushButton_9, SIGNAL(released()), this, SLOT (floor4()));
    connect(ui->pushButton_8, SIGNAL(released()), this, SLOT (floor5()));
    connect(ui->pushButton_7, SIGNAL(released()), this, SLOT (floor6()));

    // Open/close door buttons
    connect(ui->pushButton_4, SIGNAL(released()), this, SLOT (closeDoor())); //close
    connect(ui->pushButton_5, SIGNAL(released()), this, SLOT (openDoor())); //open

    // Fire and Power Out
    connect(ui->pushButton_10, SIGNAL(released()), this, SLOT (moveToSafeFloor()));
    connect(ui->pushButton_11, SIGNAL(released()), this, SLOT (moveToSafeFloor()));

    // Help button
    connect(ui->pushButton_6, SIGNAL(released()), this, SLOT (sendHelp()));
}

elevatorwindow::~elevatorwindow()
{
    delete ui;
}

void elevatorwindow::changeFloor() {
    ui->label->setText(QString::number(floor));
}

int elevatorwindow::getFloor() {
    return floor;
}

void elevatorwindow::floor1(){
    qInfo("Floor 1 button pressed");

    if (!ui->pushButton->isChecked()){
        ui->pushButton->setChecked(true);
    }

    floor = 1;
    changeFloor();
}

void elevatorwindow::floor2(){
    qInfo("Floor 2 button pressed");

    if (!ui->pushButton_2->isChecked()){
        ui->pushButton_2->setChecked(true);
    }

    floor = 2;
    changeFloor();
}

void elevatorwindow::floor3(){
    qInfo("Floor 3 button pressed");

    if (!ui->pushButton_3->isChecked()){
        ui->pushButton_3->setChecked(true);
    }

    floor = 3;
    changeFloor();
}

void elevatorwindow::floor4(){
    qInfo("Floor 4 button pressed");

    if (!ui->pushButton_9->isChecked()){
        ui->pushButton_9->setChecked(true);
    }

    floor = 4;
    changeFloor();
}

void elevatorwindow::floor5(){
    qInfo("Floor 5 button pressed");

    if (!ui->pushButton_8->isChecked()){
        ui->pushButton_8->setChecked(true);
    }

    floor = 5;
    changeFloor();
}

void elevatorwindow::floor6(){
    qInfo("Floor 6 button pressed");

    if (!ui->pushButton_7->isChecked()){
        ui->pushButton_7->setChecked(true);
    }

    floor = 6;
    changeFloor();
}

void elevatorwindow::openDoor(){
    qInfo("Open Door button pressed");
}

void elevatorwindow::closeDoor(){
    qInfo("Close door button pressed");
}

void elevatorwindow::moveToSafeFloor(){
    bell->ring();

    qInfo("Moving to safe floor");
    ui->label->setText("Safe Floor");

    floor = 1;
    //changeFloor();
}

void elevatorwindow::sendHelp(){
    qInfo("Help button pressed");

    bell->ring();

    ui->label->setText("Help");
}
