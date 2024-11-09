import heapq
import numpy
import sys


MAX_OBSTACLES_TRAVERSABLE = 1

class Square:
  def __init__(self, row, column):
    self.row = row
    self.column = column

  def __eq__(self, other):
    return self.row == other.row and self.column == other.column
  

class State:

  def __init__(self, obstacles_traversed, square, goal_square, parent):
    self.obstacles_traversed = obstacles_traversed
    self.square = square

    self.parent = None

    self.is_goal = (square == goal_square)

    self.g = 0
    self.update_heuristic(goal_square)

    self.valid = True # Turn to false if we want to ignore an item on the priority queue

  def update_heuristic(self, goal_square):
    self.h = abs(self.square.row - goal_square.row) + abs(self.square.column - goal_square.column)

  def __eq__(self, other):
    return self.square == other.square and self.obstacles_traversed == other.obstacles_traversed

  def __gt__(self, other):
    self_f = self.g + self.h
    other_f = other.g + other.h
    return self_f > other_f

  def __lt__(self, other):
    self_f = self.g + self.h
    other_f = other.g + other.h
    return self_f < other_f

  def __str__(self):
    s = "<Obstacles traversed: " + str(self.obstacles_traversed) + ", "
    s = s + "Row: " + str(self.square.row) + ", "
    s = s + "Column: " + str(self.square.column) + ", "
    s = s + "g: " + str(self.g) + ", "
    s = s + "h: " + str(self.h) + ", "
    s = s + "f: " + str(self.g + self.h) + ">"
    return s

  def __repr__(self):
    return str(self)


def pathfinding(filepath):
  # Read the grid to file
  grid = numpy.loadtxt(filepath, dtype="str", delimiter=",")
  # Create a list of the locations of treasures; will use this to update the state
  goal_square = None
  start_square = None
  for row in range(grid.shape[0]):
    for column in range(grid.shape[1]):
      if grid[row, column] == "S":
        start_square = Square(row, column)
      if grid[row, column] == "G":
        goal_square = Square(row, column)


  initial_state = State(0, start_square, goal_square, None)
  frontier = []
  heapq.heappush(frontier, initial_state) # frontier is a priority queue with start state initially
  explored = []

  while True:
    if frontier == []:
      break
    leaf = heapq.heappop(frontier)
    if not leaf.valid:
      continue
    explored.append(leaf)
    if leaf.is_goal:
      break    

    # Find all possible adjacent squares (i.e. actions we can take)
    adjacent_squares = []
    leaf_row = leaf.square.row
    leaf_column = leaf.square.column
    if leaf.square.row > 0:
      adjacent_squares.append(Square(leaf_row - 1, leaf_column))
    if leaf.square.row < grid.shape[0] - 1:
      adjacent_squares.append(Square(leaf_row + 1, leaf_column))
    if leaf.square.column > 0:
      adjacent_squares.append(Square(leaf_row, leaf_column - 1))
    if leaf.square.column < grid.shape[1] - 1:
      adjacent_squares.append(Square(leaf_row, leaf_column + 1))

    for curr_square in adjacent_squares:
      curr_obstacle = (grid[curr_square.row, curr_square.column] == "X")
      curr_node = State(leaf.obstacles_traversed + curr_obstacle, curr_square, goal_square, leaf)
      if curr_node.obstacles_traversed > MAX_OBSTACLES_TRAVERSABLE:
        continue

      curr_path_cost = leaf.g + 1

      if curr_node in explored:
        continue

      if curr_node in frontier:
        frontier_node = frontier[frontier.index(curr_node)] # Gets the equal node that is in the frontier
        if curr_path_cost > frontier_node.g:
          continue
        frontier_node.valid = False

      curr_node.parent = leaf
      curr_node.g = curr_path_cost
      heapq.heappush(frontier, curr_node)

  # If the search failed, leaf will not be a goal state
  if not leaf.is_goal:
    return None, None, None

  # Reconstruct the path
  optimal_path = []
  curr_node = leaf
  while curr_node is not None:
    optimal_path.append((curr_node.square.row, curr_node.square.column))
    curr_node = curr_node.parent
  optimal_path.reverse()

  # Get the optimal path cost
  optimal_path_cost = len(optimal_path) - 1

  # Get the number of states explored
  num_states_explored = len(explored)
  print(explored)

  return optimal_path, optimal_path_cost, num_states_explored


if __name__ == '__main__':
  optimal_path, optimal_path_cost, num_states_explored = pathfinding(sys.argv[1])
  print(optimal_path)
  print(optimal_path_cost)
  print(num_states_explored)