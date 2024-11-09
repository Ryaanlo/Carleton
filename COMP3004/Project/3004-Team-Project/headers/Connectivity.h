#ifndef CONNECTIVITY_H
#define CONNECTIVITY_H
#include <iostream>
#include <stdlib.h>
#include <string>
using namespace std;

class Connectivity {
public:
    Connectivity(int level);

    //setters
    void setConnectionLevel(int l){connectionLevel=l;}
    //getters
    int getConnectionLevel(){return connectionLevel;}
protected:
    int connectionLevel;

};
#endif