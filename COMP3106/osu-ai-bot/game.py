# train_ai.py
from pynput.mouse import Button, Controller as MouseController
from pynput.keyboard import Key, Controller as KeyboardController
import keyboard
import time
from urllib.request import urlopen
import json
from collections import namedtuple

playfield_top = 12
playfield_left = 288
playfield_width = 1342
playfield_height = 1062

mouse = MouseController()
keyboard_controller = KeyboardController()

# Define the region for osu! screen capture
playfield_region = {"top": playfield_top, "left": playfield_left, "width": playfield_width, "height": playfield_height}

Point = namedtuple('Point', 'x, y')

def fetch_json_data():
    """
    Fetches a JSON object from a given URL.

    Parameters:
    None

    Returns:
    dict: A JSON object parsed from the response.
    """
    url = "http://localhost:20727/json/"
    response = urlopen(url)
    json_data = json.loads(response.read())
    return json_data

def get_json_value(key):
    json_data = fetch_json_data()
    return json_data[key]

class OsuGameAI:
    def __init__(self, w=playfield_width-playfield_left, h=playfield_height-playfield_top):
        self.w = w
        self.h = h
        self.retry_val = False
        self.retry()
        
    def game_over(self):
        return int(get_json_value("playerHp")) == 0 and int(get_json_value("score")) > 0

    def set_retry(self, value):
        self.retry_val = value

    def get_retry(self):
        return self.retry_val
    
    def play_step(self, action):
        self.move(action)
        mouse.position = (self.cursor.x, self.cursor.y)
        mouse.click(Button.left)

        if keyboard.is_pressed('q'):
            quit()

        if keyboard.is_pressed('p'):
            self.set_retry(True)

        reward = 0
        game_over = False
        
        if self.game_over():
            game_over = True
            reward = -10
            return reward, game_over, self.score
        
        if self.click_circle() == True:
            self.score += int(get_json_value("score"))
            reward = 10

        return reward, game_over, self.score

    def move(self, action):
        self.cursor = Point(action[0], action[1])

    def click_circle(self):
        return int(get_json_value("score")) > self.score

    def retry(self):
        self.score = 0
        self.cursor = Point(self.w/2, self.h/2)