import os
import csv

class td_qlearning:
    # Memeber variables
    q_values = []
    state = []
    action = []

    def __init__(self, trials_directory):
        # Alpha and Gamma Values
        self.alpha = 0.1
        self.gamma = 0.9

        # Loads the trials and compute q-values
        self.load_trials(trials_directory)

    def load_trials(self, trials_directory):
        """
        Load trials from a directory and use them to update Q-values
        """
        print(f"Working on {trials_directory}")
        for file_name in os.listdir(trials_directory):
            if file_name.endswith('.csv'):
                file_path = os.path.join(trials_directory, file_name)
                with open(file_path, 'r') as f:
                    reader = csv.reader(f)
                    for row in reader:
                        state, action = row[0], row[1]

                        # Initialize Q-function as Q(s,a) = r(s)
                        if state[0] == 'G':
                            self.q_values.append(100)
                        elif state[2] == '1' and state[0] == 'B' or state[5] == '1' and state[0] == 'E':
                            self.q_values.append(-100)
                        else:
                            self.q_values.append(-1)
                        
                        self.state.append(state)
                        self.action.append(action)

                    # Learning q-values (1000 times)
                    for _ in range(1000):
                        self.update_q_value()

    # Iterate over all states and update q_values
    def update_q_value(self):
        for i in range(len(self.q_values) - 1):
            old_q_value = self.q_values[i]
            if self.action[i] != '-':
                max_q_value = max(self.qvalue(self.state[i+1], 'N'), self.qvalue(self.state[i+1], 'U'), self.qvalue(self.state[i+1], 'D'), self.qvalue(self.state[i+1], '-'))
                self.q_values[i] = old_q_value + self.alpha * (self.get_reward(self.state[i]) + self.gamma * max_q_value - old_q_value)
        
    # Return the reward from given state
    def get_reward(self, state):
        if state[0] == 'G':
            return 100
        elif state[2] == '1' and state[0] == 'B' or state[5] == '1' and state[0] == 'E':
            return -100
        else:
            return -1
    
    # Returns the Q-value for the specified state-action pair
    def qvalue(self, state, action):
        max_q_value = -1
        for i in range(len(self.state)):
            if self.state[i] == state and self.action[i] == action:
                if self.q_values[i] > max_q_value:
                    max_q_value = self.q_values[i]
            
        return max_q_value
    # Determines the optimal action based on the highest Q-value for the given state.
    def policy(self, state):
        # Find the action with the highest Q-value for the given state
        highest_q_value = 0
        best_action = ''
        for i in range(len(self.state)):
            if state == self.state[i]:
                if highest_q_value == 0:
                    highest_q_value = self.q_values[i]
                    best_action = self.action[i]
                else:
                    if highest_q_value < self.q_values[i]:
                        highest_q_value = self.q_values[i]
                        best_action = self.action[i]

        return best_action
    
def main():
    examples_directory = './Examples'
    for example_name in os.listdir(examples_directory):
        example_path = os.path.join(examples_directory, example_name)
        if os.path.isdir(example_path):
            trials_directory = os.path.join(example_path, 'Trials')
            agent = td_qlearning(trials_directory)

            policy_test_file = os.path.join(example_path, 'policy_tests.csv')
            if os.path.isfile(policy_test_file):
                with open(policy_test_file, 'r') as file:
                    csv_reader = csv.reader(file)
                    for state, expected_action in csv_reader:
                        actual_action = agent.policy(state)
                        print(f"Policy Test - State: {state}, Expected: {expected_action}, Actual: {actual_action}")

            qvalue_test_file = os.path.join(example_path, 'qvalue_tests.csv')
            if os.path.isfile(qvalue_test_file):
                with open(qvalue_test_file, 'r') as file:
                    csv_reader = csv.reader(file)
                    for state, action, expected_qvalue in csv_reader:
                        actual_qvalue = agent.qvalue(state, action)
                        print(f"Q-value Test - State: {state}, Action: {action}, Expected: {expected_qvalue}, Actual: {actual_qvalue}")

if __name__ == "__main__":
    main()