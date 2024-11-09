import numpy
import os
import random
import sys

FROG_SQUARES = ["S", "E", "B", "G"]
NUM_CAR_SQUARES = 6

ITERATION_INTERVAL = 100
THRESHOLD = 1e-2

GAMMA = 0.9
ALPHA = 0.1

def reward(state):
  if state[0] == "G":
    return 100
  if state[0] == "B" and state[2] == "1":
    return -100
  if state[0] == "E" and state[5] == "1":
    return -100

  return -1

def get_possible_states():
  possible_states = []
  for frog in FROG_SQUARES:
    for car_config in range(2 ** NUM_CAR_SQUARES):
      car_string = format(car_config, "0" + str(NUM_CAR_SQUARES) + "b") # Trick to get a binary representation of the car configuration
      possible_states.append(frog + car_string)
  return possible_states

def get_possible_actions(state):
  if state[0] == "S":
    return ["N", "U"]
  if state[0] == "G":
    return ["N", "D"]
  
  return ["N", "U", "D"]

def q_values_equal(q_a, q_b):
  if q_a is None or q_b is None:
    return False
  for state in q_a.keys():
    for action in q_a[state].keys():
      if 2 * abs(q_a[state][action] - q_b[state][action]) / abs(q_a[state][action] + q_b[state][action]) > THRESHOLD:
        return False
  return True

def q_deep_copy(q_from):
  q_to = dict()
  for state in q_from.keys():
    q_to[state] = dict()
    for action in q_from[state].keys():
      q_to[state][action] = q_from[state][action]
  return q_to


class td_qlearning:
  def __init__(self, directory):
    # Read the trial in as a list of state-action pairs
    trial_list = []
    for filename in os.listdir(directory):
      filepath = os.path.join(directory, filename)
      trial_list.append(numpy.loadtxt(filepath, dtype="str", delimiter=","))

    # Initialize the Q-function
    self.q = dict()
    possible_states = get_possible_states()
    for curr_state in possible_states:
      self.q[curr_state] = dict()
      for curr_action in get_possible_actions(curr_state):
        self.q[curr_state][curr_action] = reward(curr_state)

    # Update the Q-function
    old_q = None
    while(not q_values_equal(self.q, old_q)):
      old_q = q_deep_copy(self.q)
      for itr in range(ITERATION_INTERVAL):
        #random.shuffle(trial_list)
        #trial_list.reverse()
        for trial in trial_list:
          for i in range(trial.shape[0] - 1): # Need -1 because we cannot do anything with the last state
            curr_state = trial[i, 0]
            curr_action = trial[i, 1]
            next_state = trial[i + 1, 0]
            possible_next_actions = get_possible_actions(next_state)
            next_q = []
            for next_action in possible_next_actions:
              next_q.append(self.q[next_state][next_action])
            max_q = max(next_q)
            self.q[curr_state][curr_action] = self.q[curr_state][curr_action] + ALPHA * (reward(curr_state) + GAMMA * max_q - self.q[curr_state][curr_action])


  def qvalue(self, state, action):
    return self.q[state][action]

  def policy(self, state):
    max_q = max(self.q[state].values())
    max_a = ""
    for action in self.q[state].keys():
      if self.q[state][action] == max_q:
        max_a = max_a + action # Append to handle case when multiple actions are correct
    return max_a


if __name__ == "__main__":
  input_filepath = str(sys.argv[1])

  agent = td_qlearning(input_filepath)
  print(agent.qvalue("B101111", "U"))
  print(agent.policy("E101101"))