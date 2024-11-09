#ifndef FREQUENCYSELECTION_H
#define FREQUENCYSELECTION_H

class FrequencySelection {

public:
    FrequencySelection();
    int getFrequency();
    void changeFrequency();

protected:
    int frequency;
};
#endif