package assignment4;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class RetinalMatch {
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// test code (do whatever in the main method, but leave loadLibrary alone)
		Mat src = Imgcodecs.imread("RIDB/IM000001_2.JPG");
		Mat dst = new Mat();
		
		// turn the src greyscale and store in dst
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
		
		// pipeline
		resize(dst, 60);
		increaseContrast(dst);
		gaussianBlur(dst);
		adaptiveThreshold(dst);
				
		Imgcodecs.imwrite("test.jpg",dst);	// writes image out to file
	}
	
	/** 
	 * Resizes a given image based on given scale.
	 * @param img
	 * @param scale percentage of the scaling
	 */
	public static void resize(Mat img, int scale) {
		double width = (img.size().width * ((double)scale / 100));
		double height = (img.size().height * ((double)scale / 100));
		Imgproc.resize(img, img, new Size(width, height));
	}
	
	/**
	 * Increases the contrast of the image
	 * @param img
	 */
	public static void increaseContrast(Mat img) {
		img.convertTo(img, -1, 3.6, -220);
	}
	
	/**
	 * Applies adaptive threshold to the image
	 * @param img
	 */
	public static void adaptiveThreshold(Mat img) {
		Imgproc.adaptiveThreshold(img, img, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 8);
	}
	
	/**
	 * Applies a gaussian blur to the image
	 * @param img
	 */
	public static void gaussianBlur(Mat img) {
		Imgproc.GaussianBlur(img, img, new Size(7, 7), 0);
	}
}

