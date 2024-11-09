#ifndef USER_H
#define USER_H
#include <iostream>
#include <stdlib.h>
#include <string>
#include <vector>
using namespace std;

class User {
public:
    User(int id);

    //setters
    void setID(int i){ID = i;}
    //getters
    int getID(){return ID;}
private:
    int ID;
    //treatment history


};
#endif