#include <string>
#include <iostream>
#include <QObject>
#include "User.h"

using namespace std;

class Therapy : public QObject{

    Q_OBJECT

    public:
        Therapy();
        void start(string user, int session, int intenLvl);
        void stop();
        
        int getUserID();

    public slots:

    signals:

    private:
        User u;
        int session; //0 for 20 minutes, 1 for 45 minutes, 2 for 3 hours, 3 for user designated
        int duration;
        int intensityLvl;

};
