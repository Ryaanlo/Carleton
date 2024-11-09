#include <string>
#include <iostream>
#include <QObject>
#include <vector>
#include "Therapy.h"

using namespace std;

class Record : public QObject{

    Q_OBJECT

    public:
        Record();
        void pushTherapy(Therapy* t);

        Therapy* getTherapy(string name, int session);
        int getNumRecords();


    public slots:

    signals:

    private:
        vector<Therapy*> records;

};
