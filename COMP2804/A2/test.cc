#include <iostream>

using namespace std;

int count = 0;

void countthirds(int n){
    if (n == 1){
        return;
    }
    for (int i = 0; i < n; i++){
        count++;
    }
    countthirds(n/3);
}

int main(){

    countthirds(6);

    cout << "count: " <<  count << endl;
    return 0;
}

