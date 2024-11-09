# COMP4102 Assignment 2
# Image Stitching
# Ryan Lo (101117765)

import numpy as np
import cv2
from matplotlib import pyplot as plt

img1 = cv2.imread('uttower_right.jpg',0)          # queryImage
img2 = cv2.imread('large2_uttower_left.jpg',0) # trainImage

detector = cv2.AKAZE_create()

# find the keypoints and descriptors with SIFT
kp1, des1 = detector.detectAndCompute(img1, None)
kp2, des2 = detector.detectAndCompute(img2, None)

# create BFMatcher object
bf = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=True)

# Match descriptors.
matches = bf.match(des1,des2)

# Sort them in the order of their distance.
matches = sorted(matches, key = lambda x:x.distance)
print(matches)

src_pts = np.float32([ kp1[matches[m].queryIdx].pt for m in range(0, 20) ]).reshape(-1,1,2)
dst_pts = np.float32([ kp2[matches[m].trainIdx].pt for m in range(0, 20) ]).reshape(-1,1,2)
print(src_pts)
print(dst_pts)

# Compute the homography using RANSAC
H, mask = cv2.findHomography(src_pts, dst_pts, cv2.RANSAC, 5.0)

# Warp the right image into the left image
height, width = img2.shape
warped = cv2.warpPerspective(img1, H, (width, height))

# Show the matches
img3 = cv2.drawMatches(img1, kp1, img2, kp2, matches[:50], None, flags=cv2.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS)
cv2.imshow('Matches', img3)

# Merge the left and warped right images
merged = cv2.bitwise_or(img2, warped)

cv2.imshow('half', img1)
cv2.imshow('long', img2)
cv2.imshow("Warped Image", warped)
cv2.imshow("Merged Image", merged)

cv2.waitKey(0)
cv2.destroyAllWindows()
