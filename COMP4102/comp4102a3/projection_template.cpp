#include <opencv2/features2d.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/opencv.hpp>
#include <vector>
#include <iostream>
#include <fstream>

using namespace std;
using namespace cv;
using namespace std;

#define NUM_POINTS    10
#define RANGE         100.00

#define MAX_CAMERAS   100 
#define MAX_POINTS    3000


float extrinsics[3][4] = {
	0.902701, 0.051530, 0.427171, 10.0,
	0.182987, 0.852568, -0.489535, 15.0,
	-0.389418, 0.520070, 0.760184, 20.0,
};

float intrinsics[3][3] = {
	-1000.000000, 0.000000, 0.000000,
	0.000000, -2000.000000, 0.000000,
	0.000000, 0.000000,     1.000000,
};

float all_object_points[10][4] = {
	0.1251, 56.3585, 19.3304, 1.0,
	80.8741, 58.5009, 47.9873, 1.0,
	35.0291, 89.5962, 82.2840, 1.0,
	74.6605, 17.4108, 85.8943, 1.0,
	71.0501, 51.3535, 30.3995, 1.0,
	1.4985, 9.1403, 36.4452, 1.0,
	14.7313, 16.5899, 98.8525, 1.0,
	44.5692, 11.9083, 0.4669, 1.0,
	0.8911, 37.7880, 53.1663, 1.0,
	57.1184, 60.1764, 60.7166, 1.0,
};

void compute_projection_matrix(Mat image_points, Mat object_points, Mat projection_matrix) {
}

void decompose_projection_matrix(Mat projection_matrix, Mat rotation_matrix, Mat translation_vector, Mat intrinsic_matrix) {

}

int main(void)
{
	ofstream myfile;
	myfile.open("assign3-out");

	// set up the R, T and K matrix, which are given.
	// we are also given the 3d points that we will project.
	// you can assume that all the elements of T are positive.
	Mat given_extrinsics(3, 4, CV_32FC1, extrinsics);
	cout << "Given extrinsics" << endl;
	cout << given_extrinsics << endl;
	myfile << "given extrinsics (R and T)" << endl;
	myfile << given_extrinsics << endl;
	Mat given_intrinsics(3,3, CV_32FC1, intrinsics);
	cout << "Given intrinsics" << endl;
	cout << given_intrinsics << endl;
	myfile << "Given intrinsics" << endl;
	myfile << given_intrinsics << endl;
	Mat object_points(10, 4, CV_32FC1, all_object_points);
	cout << "Object points" << endl;
	cout << object_points << endl;
	myfile << "object points" << endl;
	myfile << object_points << endl;

	// compute given projection matrix
	// which is P = K[R T]
	Mat given_projection_matrix = given_intrinsics * given_extrinsics;
	cout << "Given projection matrix" << endl;
	cout << given_projection_matrix << endl;

	// now apply this projection matrix to the given 3d points.
	// the result is a set of 2d pixel locations.
	Mat transp_object_points = object_points.t();
	Mat transp_image_points = given_projection_matrix * transp_object_points;
	Mat image_points = transp_image_points.t();

	// this converts from homogeneous to ordinary co-ordinates
	for (unsigned int i = 0; i < image_points.rows; ++i) {
		image_points.at<float>(i, 0) = image_points.at<float>(i, 0) / image_points.at<float>(i, 2);
		image_points.at<float>(i, 1) = image_points.at<float>(i, 1) / image_points.at<float>(i, 2);
		image_points.at<float>(i, 2) = 1.0;
	}
	cout << "Image points" << endl;
	cout << image_points << endl;
	myfile << "Image points" << endl;
	myfile << image_points << endl;

	// now using just the object points, and image points compute the projection matrix.
	Mat computed_projection_matrix(3, 4, CV_32FC1);
	compute_projection_matrix(image_points, object_points, computed_projection_matrix);
	cout << "Computed projection matrix" << endl;
	cout << computed_projection_matrix << endl;
	myfile << "Computed projection matrix" << endl;
	myfile << computed_projection_matrix << endl;

	// finally decompose this projection matrix to get back
	// the K, R and T, which should be the same as the given K, R and T
	Mat computed_rotation_matrix(3, 3, CV_32FC1);
	Mat computed_translation_vector(3, 1, CV_32FC1);
	Mat computed_intrinsics(3, 3, CV_32FC1);
	decompose_projection_matrix(computed_projection_matrix, computed_rotation_matrix, computed_translation_vector, computed_intrinsics);
	myfile << "Computed rotation matrix" << endl;
	myfile << computed_rotation_matrix << endl;
	myfile << "Computed translation vector" << endl;
	myfile << computed_translation_vector << endl;
	myfile << "Computed intrinsics" << endl;
	myfile << computed_intrinsics << endl;

	//getchar();
	myfile.close();
	
	return 0;
}