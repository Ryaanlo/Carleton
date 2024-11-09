# COMP4102 Assignment 1
# EdgeDetection.py

from math import atan2, ceil, sqrt

import numpy as np
import cv2 as cv

def myEdgeFilter(img0, sigma):
    # Smooth the image using gaussian filter
    gaussian_filter = cv.GaussianBlur(img0, (5,5), 2*ceil(3*sigma)+1, sigma, cv.BORDER_DEFAULT)

    # Turn the image into greyscale
    grey = cv.cvtColor(gaussian_filter, cv.COLOR_BGR2GRAY)
    cv.imshow("Greyscale Image", grey)

    # Show the original image and the Gaussian Smoothing image side by side
    cv.imshow("Gaussian Smoothing", np.hstack((img0, gaussian_filter)))

    # Array used for sobel filter in x & y
    sobelfilterx = np.array([[-1 , 0, 1],
                            [-2, 0 , 2], 
                            [-1, 0, 1]])
    sobelfiltery = np.array([[-1 , -2, -1],
                            [0, 0 , 0], 
                            [1, 2, 1]])

    # Filters the image using the sobel filter above
    filteredx = cv.filter2D(grey, -1, sobelfilterx)
    filteredy = cv.filter2D(grey, -1, sobelfiltery)

    # Applying the sobel filter
    sobelimg = cv.phase(np.array(filteredx, np.float32), np.array(filteredy, np.float32))
    cv.imshow("Sobel Filter", sobelimg)

    # Sobel combining both x & y
    sobelx = cv.convertScaleAbs(filteredx)
    sobely = cv.convertScaleAbs(filteredy)
    sobelcombined = cv.addWeighted(sobelx, 0.5, sobely, 0.5, 0)
    cv.imshow("SobelCombined", sobelcombined)

    # Making sobel filtered image into np array
    sobelx = np.array(filteredx, np.float64)
    sobely = np.array(filteredy, np.float64)

    # Gradient Magnitude estimation
    magnitude = np.sqrt((sobelx * sobelx) + (sobely * sobely))
    # Gradient Orientation
    orientation = np.arctan2(sobelx, sobely)

    # Show the images
    cv.imshow("Gradient Magnitude and & Gradient Orientation", np.hstack((magnitude, orientation)))

    cv.waitKey(0) # waits until a key is pressed
    cv.destroyAllWindows() # destroys the window showing image

    cv.imwrite("EdgeMagnitude.jpg", magnitude)

    return magnitude

def main():
    # Image to be used
    input = './img0.jpg' # change this to whatever image you want to use
    img = cv.imread(input)
    
    # Run the Edge Filter!
    myEdgeFilter(img, 0.2)

if __name__ == '__main__':
    main()