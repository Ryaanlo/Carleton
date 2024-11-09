# COMP4102 Assignment 2
# Harris Corner Detector
# Ryan Lo (101117765)

import numpy as np
import cv2

# Load the image
img = cv2.imread('box_in_scene.png')

# Show the image
cv2.imshow('Original Image', img)

# Convert the image to grayscale
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

# Compute the minimum eigenvalue of the image
mineigenvalue = cv2.cornerMinEigenVal(gray, blockSize=5, ksize=5)

# Create a window with a slider at the top of the window
cv2.namedWindow('Thresholded Image')
cv2.createTrackbar('Threshold', 'Thresholded Image', 1, 100, lambda x: None)

while True:
    # Get the threshold value from the slider then divide by 100 to get 0.01 value if it was at 1
    threshold = cv2.getTrackbarPos('Threshold', 'Thresholded Image') / 100.0

    # Threshold the eigenvalue and set the pixel to white or black
    thresholded = np.zeros_like(gray)
    thresholded[mineigenvalue > threshold * mineigenvalue.max()] = 255

    # Non-maxima suppression to thin out the potential corners
    radius = 10
    corners = []
    for i in range(radius, thresholded.shape[0]-radius):
        for j in range(radius, thresholded.shape[1]-radius):
            if thresholded[i,j] == 255:
                neighborhood = thresholded[i-radius:i+radius+1, j-radius:j+radius+1]
                if neighborhood.max() == thresholded[i,j]:
                    corners.append((j,i))

    # Draw the final corners in another window as a cross
    final_corners = np.copy(img)
    for corner in corners:
        cv2.drawMarker(final_corners, tuple(corner), color=(0,0,255), markerType=cv2.MARKER_CROSS, thickness=1)

    # Display the thresholded image and the final corners
    cv2.imshow('Thresholded Image', thresholded)
    cv2.imshow('Final Corners', final_corners)

    # Wait for a key press
    key = cv2.waitKey(1)

    # Exit the loop if the 'q' key is pressed
    if key == ord('q'):
        break

# Close all windows
cv2.destroyAllWindows()
