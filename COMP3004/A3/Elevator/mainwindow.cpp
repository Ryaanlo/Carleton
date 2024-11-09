#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    // Add the floors
    for (int i = 0; i < 5; i++){
        floors.push_back(new floor());
    }

    ui->label->setText("Floor 1");

    // Init variables
    newElevator = new elevator();
    newWindow = new elevatorwindow();
    timer = new QTimer();

    // Set up buttons
    connect(timer, &QTimer::timeout, this, &MainWindow::changeWindow);
    connect(ui->pushButton, SIGNAL(released()), this, SLOT (goUp()));
    connect(ui->pushButton_2, SIGNAL(released()), this, SLOT (goDown()));
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::changeWindow() {
    if (newWindow->isVisible()) {
        newWindow->hide();
        this->show();
    }else{
        this->hide();
        newWindow->show();
    }
    timer->stop();
    qInfo("Elevator arrived");
    qInfo("Doors opened");
}

void MainWindow::goUp() {
    //requestElevator();
    qInfo("Up button pressed");

    if (!ui->pushButton->isChecked()){
        ui->pushButton->setChecked(true);
    }

    timer->start(3000); // 3000 ms
}

void MainWindow::goDown() {
    //requestElevator();
    qInfo("Down button pressed");

    if (!ui->pushButton_2->isChecked()){
        ui->pushButton_2->setChecked(true);
    }

    timer->start(3000); // 3000 ms
}


