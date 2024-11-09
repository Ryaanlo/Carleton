#ifndef ELEVATOR_H
#define ELEVATOR_H


class elevator
{
public:
    elevator();
    void changeFloor(int);
    int getFloor();

private:
    static int id;
    int floor;

};

#endif // ELEVATOR_H
