#ifndef DATE_H
#define DATE_H

class ECS {
    void requestFloor();
    void requestElevator();
};

class Passenger {
    void pressButton();
};

class Door {
    bool closed;

    void open();
    void close();
};

class Buttons {
    bool illuminate;
    int floorNum;

    void pressed();
};

class Floor {
    int floorNum;
    bool occupied;
};

class Display {
    int floorNum;

    void update();
};

class Sensors {
    bool activated;

    void keepDoorOpen();
};

class Bell {
    bool ringing;

    void ring();
};

class Safety {
    bool active;

    void help();
};

class Elevator {
    int floor;
    int elevatorNum;

    void openClose();
    void update();
};



#endif