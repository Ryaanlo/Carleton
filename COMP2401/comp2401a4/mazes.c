#include <stdio.h>
#include <stdlib.h>

#include "graphSet.h"
#include "mazeDisplay.h"


void traceGraph(char maze[HEIGHT][WIDTH], int x, int y, Node *prevNode, Node *root);
void freeGraph(Node *n);

// Compute the graph for the given maze and add it to the given graph set.
Graph *computeGraph(char maze[HEIGHT][WIDTH]) {

  // Create the initially-empty graph
  Graph *gr = malloc(sizeof(Graph));
  gr->rootNode = NULL;
  gr->nextGraph = NULL;

  // Find a starting node, then trace out the maze recursively.  A starting node can be
  // found by searching from top to bottom, left to right, for a non-wall maze location.
  // You MUST NOT hard-code this start location ... it must be determined by your code.

  // Searching till we hit a '0' in the maze
  // Create root node for that maze
  for (int i = 0; i < WIDTH; i ++){
    for (int j = 0; j < HEIGHT; j ++){
      if (maze[i][j] == '0'){
        Node *root = malloc(sizeof(Node));
        root->x = j;
        root->y = i;
        root->up = NULL;
        root->down = NULL;
        root->left = NULL;
        root->right = NULL;
        gr->rootNode = root;
        i = WIDTH;
        j = HEIGHT;
        break;
      }
    }
  }

  // To trace out the maze recursively, you will likely want to create a recursive
  // procedure that is called by this one.   It should take parameters to indicate
  // the location in the maze to start tracing from, the maze itself and also the node
  // that led to this node (i.e., the previous one in the tree that led here).  If you
  // start with the root node, then the previous node should be NULL.
  //
  // As you go through the maze, make sure to mark off maze locations that you have
  // visited (perhaps by putting a '2' character at that location) so that you do not
  // go back to that location again and end up with infinite recursion.  So you can
  // stop the recursion when you reach a wall (i.e., a '0' character in the maze) or a
  // '2' character.  A '1' character means that it is a free space that you just arrived
  // at for the first time.   Make sure to check recursively in all directions.  In my
  // solutions (shown on the assignment), I used an ordering of up/down/left/right.  So
  // if you want solutions to look like mine, you should follow that ordering as well.
  //
  // As you traverse the maze, make sure to connect the previous node to the current one.
  // You'll have to check which direction you can from (i.e., x and y values) so that
  // you know whether it is the up/down/left or right pointer to set.

  // Trace the graph
  traceGraph(maze, gr->rootNode->x, gr->rootNode->y, NULL, gr->rootNode);

  // You need not do this recursively, but it will lkely be a lot harder to do it non-
  // recursively.

  return gr; // Remove this line when you write your code
}

// Function for tracing the graph
void traceGraph(char maze[HEIGHT][WIDTH], int x, int y, Node *prevNode, Node *root){

  // If the position in the graph is a wall or already visited then return
  if (maze[y][x] == '1' || maze[y][x] == '2'){
    return;
  // If it's at an open space
  }else{
    // Set the location to visited
    maze[y][x] = '2';
    // If not at rootNode
    if (prevNode != NULL){
      // Allocate for initially empty new node
      Node *n = malloc(sizeof(Node));
      n->x = x;
      n->y = y;
      n->up = NULL;
      n->down = NULL;
      n->left = NULL;
      n->right = NULL;

      // Check prevNode's x and y and compare with
      // current node's x and y and assign directions
      if (prevNode->x > x){
        prevNode->left = n;
      }
      if(prevNode->x < x){
        prevNode->right = n;
      }
      if(prevNode->y > y){
        prevNode->up = n;
      }
      if(prevNode->y < y){
        prevNode->down = n;
      }

      // Trace graph in all directions
      traceGraph(maze, x - 1, y, n, root);

      traceGraph(maze, x + 1, y, n, root);

      traceGraph(maze, x, y - 1, n, root);

      traceGraph(maze, x, y + 1, n, root);

    // If at rootNode then just retrace graph in all direction
    }else{
      traceGraph(maze, x - 1, y, root, root);

      traceGraph(maze, x + 1, y, root, root);

      traceGraph(maze, x, y - 1, root, root);

      traceGraph(maze, x, y + 1, root, root);
    }

  }

}

// This procedure must clean up graph by removing all nodes in which the previous and
// next nodes have the same x or y value as it.
void cleanUpGraph(Node *n, Node *previousNode) {
  // If at rootNode then check every direction
  // and clean up graph
  if (previousNode == NULL){
    if (n->up != NULL){
      cleanUpGraph(n->up, n);
    }
    if (n->down != NULL){
      cleanUpGraph(n->down, n);
    }
    if (n->left != NULL){
      cleanUpGraph(n->left, n);
    }
    if (n->right != NULL){
      cleanUpGraph(n->right, n);
    }
  // If not at rootNode
  }else{
    // Counter for number of out(s)
    // Check and add every out(s)
    int out = 0;
    if (n->up != NULL){
      out ++;
    }
    if (n->down != NULL){
      out ++;
    }
    if (n->right != NULL){
      out ++;
    }
    if (n->left != NULL){
      out ++;
    }

    // Variable used as boolean to determine
    // if node should be freed
    int freeNode = 0;

    // If number of outs is more than 1
    // Check all directions and clean up graph
    if (out > 1){
      if (n->up != NULL){
        cleanUpGraph(n->up, n);
      }
      if (n->down != NULL){
        cleanUpGraph(n->down, n);
      }
      if (n->left != NULL){
        cleanUpGraph(n->left, n);
      }
      if (n->right != NULL){
        cleanUpGraph(n->right, n);
      }
    // If only 0 or 1 outs
    }else{
      // Check every direction
      // then check condition if 3 node is
      // in same direction, if so,
      // node is to be freed, else clean up graph again
      if (n->up != NULL){
        if (previousNode->y > n->y){
          previousNode->up = n->up;
          cleanUpGraph(n->up, previousNode);
          freeNode = 1;
        }else{
          cleanUpGraph(n->up, n);
        }
      }
      if (n->down != NULL){
        if (previousNode->y < n->y){
          previousNode->down = n->down;
          cleanUpGraph(n->down, previousNode);
          freeNode = 1;
        }else{
          cleanUpGraph(n->down, n);
        }
      }
      if (n->left != NULL){
        if (previousNode->x > n->x){
          previousNode->left = n->left;
          cleanUpGraph(n->left, previousNode);
          freeNode = 1;
        }else{
          cleanUpGraph(n->left, n);
        }
      }
      if (n->right != NULL){
        if (previousNode->x < n->x){
          previousNode->right = n->right;
          cleanUpGraph(n->right, previousNode);
          freeNode = 1;
        }else{
          cleanUpGraph(n->right, n);
        }
      }
      if (freeNode){
        free(n);
      }
    }
  }
}

void freeGraph(Node *n){
  // Check all directions
  // free node after all recursion
  if (n->up != NULL){
    freeGraph(n->up);
  }
  if (n->down != NULL){
    freeGraph(n->down);
  }
  if (n->left != NULL){
    freeGraph(n->left);
  }
  if (n->right != NULL){
    freeGraph(n->right);
  }
  free(n);
}



// This is where it all begins
int main() {
  char mazes[5][HEIGHT][WIDTH] = {
    {"111111111111111111111",
     "100000001000100000001",
     "101111111010111011111",
     "100000000010000010001",
     "101110111111101110111",
     "100010001000000000001",
     "111011111111111110111",
     "101010001000100000001",
     "101110111011101011101",
     "100010000000001010001",
     "101010111011111111111",
     "101000001000100000001",
     "101111111110101111101",
     "100010100000100000101",
     "111110101110101111101",
     "100010001000000010101",
     "101010111111111010111",
     "101010001000000010001",
     "101111111010111011101",
     "100000000010001000001",
     "111111111111111111111"},

    {"111111111111111111111",
     "100000000000000000001",
     "101111111111111111111",
     "100000000000000000001",
     "101111111111111111111",
     "100000000000000000001",
     "111111111111111111101",
     "100000000000000000001",
     "101111111111111111111",
     "100000000000000000001",
     "111111111111111111101",
     "100000000000000000001",
     "101111111111111111111",
     "101111111111111111101",
     "101111111111111111101",
     "101000100010001000101",
     "101010101010101010101",
     "101010101010101010101",
     "101010101010101010101",
     "100010001000100010001",
     "111111111111111111111"},

    {"111111111111111111111",
     "100000000000000000001",
     "101010101010101010101",
     "100000000000000000001",
     "101110111011101110111",
     "100000000000000000001",
     "101111101111101111101",
     "100000000000000000001",
     "101111111001111111101",
     "100000000000000000001",
     "101111111111111111101",
     "100111111111111111001",
     "100011111111111110001",
     "100001111111111100001",
     "100000111111111000001",
     "100000011111110000001",
     "100000001111100000001",
     "100000000111000000001",
     "100000000010000000001",
     "100000000000000000001",
     "111111111111111111111"},

    {"111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111110111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111",
     "111111111111111111111"},

    {"111111111111111111111",
     "111100000000000000001",
     "111000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "100000000000000000001",
     "111111111111111111111"}};

  // Open a display window
  openDisplayWindow();



  // Allocate a GraphSet to store the graphs for each maze
  GraphSet *gSet = malloc(sizeof(GraphSet));
  gSet->firstGraph = NULL;
  gSet->lastGraph = NULL;

  // Compute the graphs for each maze and add them to a Graph Set
  for (int i=0; i<5; i++) {
    Graph *g = computeGraph(mazes[i]);

    // Add g to gSet properly
    // ...
    if (i == 0){
      gSet->firstGraph = g;
    }else{
      Graph *gr = gSet->firstGraph;
      for (int j = 0; j < i - 1; j ++){
        gr = gr->nextGraph;
      }
      gr->nextGraph = g;
    }
    if (i == 4){
      gSet->lastGraph = g;
    }
  }

  // Show the graphs
  Graph *g = gSet->firstGraph; // ... set this to the first graph in gSet ...

  for (int i=0; i<5; i++) {
    drawMaze(mazes[i]);  // Draw the maze
    drawGraph(g->rootNode);    // Draw the graph

    getchar();  // Wait for user to press enter

    cleanUpGraph(g->rootNode, NULL);   // Clean up the graph
    drawMaze(mazes[i]);
    drawGraph(g->rootNode);

    // ... get the next graph in the set ...
    // ... INSERT A LINE OF CODE HERE ...
    g = g->nextGraph;

    getchar();  // Wait again for the user to press ENTER before going on to the next maze
  }

  // Free up all allocated memory
  // ...

  g = gSet->firstGraph;
  Graph *prevG = g;
  for (int i=0; i<5; i++) {

    freeGraph(g->rootNode);
    prevG = g;
    g = g->nextGraph;
    free(prevG);
  }

  free(gSet);

  // Close the display window
  closeDisplayWindow();
}
