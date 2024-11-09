#ifndef SESSION_H
#define SESSION_H

const int SESSION_TWENTY = 20;
const int SESSION_FORTY_FIVE = 45;
const int SESSION_THREE_HOURS = 180;

enum SessionType {
  MET,
  SubDelta,
  Delta,
  Theta,
  Alpha,
  SMR,
  Beta,
  OneHundredHz
};

class Session {
private:
  int length;
  SessionType type;

public:
  Session(int length, SessionType type);
  ~Session();
  int GetLength();
  SessionType GetType();
};

#endif