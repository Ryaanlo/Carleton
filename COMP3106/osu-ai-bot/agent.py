import torch
import random
import keyboard
import numpy as np
from collections import deque
from game import OsuGameAI, Point
from model import Linear_QNet, QTrainer
from displayplot import plot

MAX_MEMORY = 100_000
BATCH_SIZE = 1000
LR = 0.001

playfield_tl_x = 12
playfield_tl_y = 288
playfield_br_x = 1342
playfield_br_y = 1062

class Agent:

    def __init__(self):
        self.n_games = 0
        self.epsilon = 0 # randomness
        self.gamma = 0.9 # discount rate
        self.memory = deque(maxlen=MAX_MEMORY) # popleft()
        self.model = Linear_QNet(2, 256, 2)
        self.trainer = QTrainer(self.model, lr=LR, gamma=self.gamma)

    def get_state(self, game):
        is_over = game.game_over()
        point = game.cursor

        # Extracting x and y coordinates from the Point object
        point_x = point.x
        point_y = point.y

        # Flatten the state to only include scalar values
        state = [
            point_x,
            point_y
        ]
        # print(f"get_state state: {np.array(state, dtype=float)}")
        return np.array(state, dtype=int)

    def remember(self, state, action, reward, next_state, done):
        self.memory.append((state, action, reward, next_state, done)) # popleft if MAX_MEMORY is reached

    def train_long_memory(self):
        if len(self.memory) > BATCH_SIZE:
            mini_sample = random.sample(self.memory, BATCH_SIZE) # list of tuples
        else:
            mini_sample = self.memory

        states, actions, rewards, next_states, dones = zip(*mini_sample)
        self.trainer.train_step(states, actions, rewards, next_states, dones)
        # for state, action, reward, next_state, done in mini_sample:
        #    self.trainer.train_step(state, action, reward, next_state, done)

    def train_short_memory(self, state, action, reward, next_state, done):
        self.trainer.train_step(state, action, reward, next_state, done)

    def get_action(self, state):
        self.epsilon = 80 - self.n_games

        # TODO action mouse click
        final_move = Point(playfield_tl_x, playfield_tl_y)

        if random.randint(0, 200) < self.epsilon:
            x = random.randint(playfield_tl_x, playfield_br_x)
            y = random.randint(playfield_tl_y, playfield_br_y)
            final_move = Point(x,y)
        else:
            state0 = torch.tensor(state, dtype=torch.float)
            prediction = self.model(state0)
            x, y = torch.argmax(prediction[0]).item(), torch.argmax(prediction[1]).item()
            final_move = Point(x + playfield_br_x, y + playfield_br_y)
            print(f"final move: {final_move}")

        return final_move


def train():
    plot_scores = []
    plot_mean_scores = []
    total_score = 0
    record = 0
    agent = Agent()
    game = OsuGameAI()
    while True:
        # get old state
        state_old = agent.get_state(game)

        # get move
        final_move = agent.get_action(state_old)

        # perform move and get new state
        reward, done, score = game.play_step(final_move)
        state_new = agent.get_state(game)

        # train short memory
        agent.train_short_memory(state_old, final_move, reward, state_new, done)

        # remember
        agent.remember(state_old, final_move, reward, state_new, done)
        if game.get_retry():
            # train long memory, plot result
            game.set_retry(False)
            game.retry()
            agent.n_games += 1
            agent.train_long_memory()

            if score > record:
                record = score
                agent.model.save()

            print('Game', agent.n_games, 'Score', score, 'Record:', record)

            plot_scores.append(score)
            total_score += score
            mean_score = total_score / agent.n_games
            plot_mean_scores.append(mean_score)
            # plot(plot_scores, plot_mean_scores)


if __name__ == '__main__':
    train()