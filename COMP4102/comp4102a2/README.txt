COMP4102 Assignment 2
Ryan Lo (101117765)

Write down a short (one paragraph) description of how you would get rid of these visible anomalies and
include it in your upload. In other words, I want to know what you would do in place of the OR operation
to reduce these anomalies.

Setup & Run:
Run the python files and it should run with examples.

Instead of using the bitwise OR function to merge the left and the right images together, we can use blending techniques to reduce the anomalies.
Some ways to do this is multiplying this pixel values of each image before adding them together. Another way is to compute the weighted average of the pixel values
of each image.