# COMP4102 Assignment 1
# SticksFilter.py

import cv2 as cv
import numpy as np

def SticksFilter(img):
    # Make the image greyscale
    img = cv.cvtColor(img, cv.COLOR_BGR2GRAY)
    # Copy of the image
    sticksImage = img.copy()

    sticksImage = cv.Canny(sticksImage, 200,200)

    # Show the original and new image side by side
    cv.imshow('Sticks Image', np.hstack((img, sticksImage)))
    cv.waitKey(0) # waits until a key is pressed
    cv.destroyAllWindows() # destroys the window showing image

def main():
    # Image to be used
    input = 'EdgeMagnitude.jpg' # change this to whatever image you want to use
    img = cv.imread(input)
    
    # Run the Sticks Filter!
    SticksFilter(img)

if __name__ == '__main__':
    main()