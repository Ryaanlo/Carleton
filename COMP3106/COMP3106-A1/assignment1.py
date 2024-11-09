# COMP3106 Assignment 1
#
# Ryan Lo (101117765)
# Adrian Alexander (101150602)

import os
import csv

class Node:
    def __init__(self, x, y, passed_obstacle=False):
        self.x = x
        self.y = y
        self.g = float('inf')
        self.h = float('inf')
        self.f = self.g + self.h
        self.parent = None
        self.passed_obstacle = passed_obstacle

    def __eq__(self, other):
        if isinstance(other, Node):
            return self.x == other.x and self.y == other.y
        return False

def print_grid(grid):
    for row in grid:
        print(' '.join(row))
    print("\n")

def manhattan_distance(node1, node2):
    return abs(node1.x - node2.x) + abs(node1.y - node2.y)

def construct_path(node):
    path = [(node.x, node.y)]
    while node.parent:
        node = node.parent
        path = [(node.x, node.y)] + path
    return path

def get_neighbours(node, grid):
    neighbours = []
    m, n = len(grid), len(grid[0])
    directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
    for dx, dy in directions:
        nx, ny = node.x + dx, node.y + dy
        if 0 <= nx < m and 0 <= ny < n:
            neighbours.append(Node(nx, ny, node.passed_obstacle))
    return neighbours

def pathfinding(filepath):
    grid = []
    frontier = []
    explored = []
    start = None
    goal = None

    with open(filepath, 'r') as f:
        reader = csv.reader(f)
        for i, row in enumerate(reader):
            grid.append(row)
            for j, node in enumerate(row):
                if node == 'S':
                    start = Node(i, j)
                elif node == 'G':
                    goal = Node(i, j)
    
    print_grid(grid)

    start.g = 0
    start.h = manhattan_distance(start, goal)
    start.f = start.g + start.h
    frontier.append(start)

    print(f"Starting A* search from ({start.x}, {start.y}) to ({goal.x}, {goal.y})\n")

    while frontier:
        # grabs the node with the lowest f cost, removes it fron the frontier and adds it to the explored list
        current_node = min(frontier, key=lambda node: node.f)
        frontier.remove(current_node)
        explored.append(current_node)

        # if the current node is the goal, we're done!
        if current_node == goal:
            return construct_path(current_node), current_node.g, len(explored)

        neighbours = get_neighbours(current_node, grid)

        for neighbour in neighbours:
            # if the neighbour is in the explored list and passed an obstacle, move onto the next neighbour
            if any(node.x == neighbour.x and node.y == neighbour.y and node.passed_obstacle == neighbour.passed_obstacle for node in explored):
                continue
            
            is_obstacle = grid[neighbour.x][neighbour.y] == 'X'
            if is_obstacle:
                if current_node.passed_obstacle:
                    continue
                else:
                    neighbour.passed_obstacle = True

            temp_g = current_node.g + 1
           
            # grabs the first node from the frontier list that matches the current neighbour. If there's no existing node, frontier_node is None.
            frontier_node = next((node for node in frontier if node.x == neighbour.x and node.y == neighbour.y and node.passed_obstacle == neighbour.passed_obstacle), None)
            
            '''
            if the neighbour node is not in the frontier or the temporary cost to reach the neighbour is cheaper than the cost stored in
            frontier_node
            '''
            if frontier_node is None or temp_g < frontier_node.g:
                # set the parent of the neighbour to be the current node, and update its g, h, and f values
                neighbour.parent = current_node
                neighbour.g = temp_g
                neighbour.h = manhattan_distance(neighbour, goal)
                neighbour.f = neighbour.g + neighbour.h

                # if neighbour isn't in the frontier, add it
                if frontier_node is None:
                    frontier.append(neighbour)
                else:
                    # the neighbour is already in frontier. Update the costs and the parent
                    frontier_node.g = neighbour.g
                    frontier_node.h = neighbour.h
                    frontier_node.f = neighbour.f
                    frontier_node.parent = current_node

    return None, float('inf'), len(explored)

if __name__ == "__main__":
    root_dir = "./Examples"

    tests = [d for d in os.listdir(root_dir) if os.path.isdir(os.path.join(root_dir, d)) and d.startswith("Example")]

    for test in tests:
        input_file_path = os.path.join(root_dir, test, "input.csv")
        with open(os.path.join(root_dir, test, "optinal_path.txt"), "r") as f:
            expected_optimal_path = eval(f.read().strip())
        with open(os.path.join(root_dir, test, "optimal_path_cost.txt"), "r") as f:
            expected_optimal_path_cost = int(f.read().strip())
        with open(os.path.join(root_dir, test, "num_states_explored.txt"), "r") as f:
            expected_num_states_explored = int(f.read().strip())

        optimal_path, optimal_path_cost, num_states_explored = pathfinding(input_file_path)

        result_path = f"""
        Expected Path: 
        {expected_optimal_path}
        Resulting Path: 
        {optimal_path}

        Expected Cost:
        {expected_optimal_path_cost}
        Resulting Cost:
        {optimal_path_cost}

        Expected Num Explored:
        {expected_num_states_explored}
        Resulting Explored:
        {num_states_explored}
        """

        print(result_path)