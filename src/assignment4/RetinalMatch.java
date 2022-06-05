package assignment4;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class RetinalMatch {
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// test code (do whatever in the main method, but leave loadLibrary alone)
		Mat src = Imgcodecs.imread("RIDB/IM000001_1.JPG");
		Mat dst = new Mat();
		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
		resize(dst, 60);
		Imgcodecs.imwrite("test.jpg",dst);
	}
	
	/**
	 * Resizes a given image based on given scale.
	 * (Doesn't create a new Mat for the new image, it edits the source) 
	 * @param dst
	 * @param scale
	 */
	public static void resize(Mat img, int scale) {
		double width = (img.size().width * ((double)scale / 100));
		double height = (img.size().height * ((double)scale / 100));
		Imgproc.resize(img, img, new Size(width, height));
	}
}
