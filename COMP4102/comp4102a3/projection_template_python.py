#!/usr/bin/env python

import cv2
import sys
import numpy
from numpy import matrix


R = numpy.array([[0.902701, 0.051530, 0.427171],
                 [0.182987, 0.852568, -0.489535],
                 [-0.389418, 0.520070, 0.760184]],
                numpy.float32)

rvec = cv2.Rodrigues(R)[0]
print('Initial Rotation')
print(R)

cameraMatrix = numpy.array([[-1000.000000, 0.000000, 0.000000],
                            [0.000000, -2000.000000, 0.000000],
                            [0.000000, 0.000000, 1.000000]],numpy.float32)

print('Initial Camera Matrix')
print(cameraMatrix)

tvec = numpy.array([[10],[15],[20]], numpy.float32)

print('Initial Translation')
print(tvec)


objectPoints = numpy.array([[0.1251, 56.3585, 19.3304],
                            [80.8741, 58.5009, 47.9873],
                            [35.0291, 89.5962, 82.2840],
                            [74.6605, 17.4108, 85.8943],
                            [71.0501, 51.3535, 30.3995],
                            [1.4985, 9.1403, 36.4452],
                            [14.7313, 16.5899, 98.8525],
                            [44.5692, 11.9083, 0.4669],
                            [0.8911, 37.7880, 53.1663],
                            [57.1184, 60.1764, 60.7166]], numpy.float32) 

print('Initial ObjectPoints')
print(objectPoints)

imagepoints,jac = cv2.projectPoints(objectPoints, rvec , tvec, cameraMatrix, None)
print('Image Points')
print(imagepoints)


P = numpy.dot(cameraMatrix, numpy.hstack((R,tvec)))
print(P)

# Decompose the projection matrix into intrinsic and extrinsic parameters
output = cv2.decomposeProjectionMatrix(P)
K_decomp = output[0]
R_decomp = output[1]
t_decomp = output[2]

# Print the computed parameters
print("Computed Camera Calibration Matrix:")
print(K_decomp)
print("\nComputed Rotation Matrix:")
print(R_decomp)
print("\nComputed Translation Vector:")
print(t_decomp)