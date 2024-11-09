#ifndef FLOOR_H
#define FLOOR_H


class floor
{
public:
    floor();
    int getFloorNum();
    bool isOccupied();
    void changeOccupied();

private:
    static int num;
    bool occupied;

};

#endif // FLOOR_H
