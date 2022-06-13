//package assignment4;

import java.io.FileWriter;

import org.opencv.core.*;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class RetinalMatch {
	public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        Mat src = Imgcodecs.imread(args[0]);
        Mat src2 = Imgcodecs.imread(args[1]); 
        processImage(src);
        processImage(src2);
        if(checkMatching(src, src2)) System.out.println("1");
        else System.out.println("0");
        
        // Testing loop
        /*for(int i = 1; i <= 20; i++) {
        	for (int j = 1; j <= 5; j++) {
                Mat src = Imgcodecs.imread("RIDB/IM00000" + j + "_" + i + ".JPG");
                processImage(src);
                System.out.println("Testing " + j + "_" + i + ": ");
                matchAgainstRIDB(src, i, j + "_" + i);
        	}
        }*/
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
     * Sharpens the image using gaussian sharpen
     */
    public static void gaussianSharpen(Mat img) {
    	Mat dst = new Mat();
        Imgproc.GaussianBlur(img, dst, new Size(0, 0), 3);
        Core.addWeighted(img, 2.0, dst, -1.5, 1, dst);
        img.release();
        dst.copyTo(img);
    }
    
    /**
     * Applies Sobel edge detection to the image
     * @param img
     */
    public static void SobelTransform(Mat img)
    {
    	Mat dst = new Mat();
        // First we declare the variables we are going to use
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;
        Mat grad_x = new Mat(), grad_y = new Mat();
        Mat abs_grad_x = new Mat(), abs_grad_y = new Mat();
        Imgproc.Sobel( img, grad_x, ddepth, 1, 0, 3, scale, delta, Core.BORDER_DEFAULT );
        Imgproc.Sobel( img, grad_y, ddepth, 0, 1, 3, scale, delta, Core.BORDER_DEFAULT );
        // converting back to CV_8U
        Core.convertScaleAbs( grad_x, abs_grad_x );
        Core.convertScaleAbs( grad_y, abs_grad_y );
        Core.addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, dst );
        img.release();
        dst.copyTo(img);
    }
    
    /**
     * Applies a mask to the image to remove the outline,
     * also inverts the colour of the image.
     * @param img
     */
    public static void applyMask(Mat img) {
        Mat mask = new Mat(img.rows(), img.cols(), CvType.CV_8U);
        Imgproc.circle(mask, new Point(img.cols()/2 - img.cols()/60, img.rows()/2 + img.rows()/65), img.rows()/2, new Scalar(255,255,255), -1, 8, 0 );
        Imgproc.rectangle(mask, new Point(0, 0),  new Point(img.cols(), img.rows()/13), new Scalar(0,0,0), -1);
        Imgproc.rectangle(mask, new Point(0, img.rows()),  new Point(img.cols(), img.rows() - img.rows()/15), new Scalar(0,0,0), -1);
        Mat img2 = new Mat();
        img.copyTo(img2);
        img.release();
        img2.copyTo(img, mask);
        Core.bitwise_not(img, img);
    }
    
    /**
     * Crops the image to reduce white/empty space
     * @param img
     */
    public static void applyCrop(Mat img) {
        int[][] translateArr = {{1, 0, -(img.cols()/7)}, {0, 1, -(img.rows()/13)}};
        Mat translation = new Mat(2, 3, CvType.CV_32F);
        for(int i = 0; i < 2; i++) {
        	for(int j = 0; j < 3; j++) {
        		translation.put(i, j, translateArr[i][j]);
        	}
        }
        Imgproc.warpAffine(img, img, translation, new Size(img.cols() - (img.cols()/19 * 6), img.rows() - img.rows()/7));
    }
    
    
    /**
     * Blurs the image via median algorithm
     * @param img
     */
    public static void medianBlur(Mat img) { for(int i = 1; i < 3; i=i+2) Imgproc.medianBlur(img,img,i); }
    
    /**
     * Checks if two processed images match up
     * i.e. are the images from the same person.
     * @param src1
     * @param src2
     * @return
     */
    public static Boolean checkMatching(Mat src1, Mat src2) {
    	int numSplits = 4;
    	double threashhold = 0.3;
    	Mat template = new Mat();
    	Mat result = new Mat();
    	for(int i = 0; i < numSplits; i++) {
    		for(int j = 0; j < numSplits; j++) {
    	    	// block of code to create the template for matching (currently only gets the last quarter)
    	        int[][] translateArr = {{1, 0, -(src1.cols()/numSplits) * j}, {0, 1,  -(src1.rows()/numSplits) * i}};
    	        Mat translation = new Mat(2, 3, CvType.CV_32F);
    	        for(int row = 0; row < 2; row++) {
    	        	for(int column = 0; column < 3; column++) {
    	        		translation.put(row, column, translateArr[row][column]);
    	        	}
    	        }
    	        Imgproc.warpAffine(src1, template, translation, new Size(src1.cols()/numSplits , src1.rows()/numSplits));
    	        
    	        // block of code that does the matching
    	        Imgproc.matchTemplate(src2, template, result, Imgproc.TM_CCOEFF_NORMED);
    	        MinMaxLocResult mmr = Core.minMaxLoc(result);
    	        
    	        if(mmr.maxVal > threashhold)
    	            return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Processes the image to extract the veins
     * (The computer vision pipeline)
     * @param img
     */
    public static void processImage(Mat img) {
        resize(img, 60);
        img.convertTo(img, -1, 1.5, 0); 
        Imgproc.GaussianBlur(img, img, new Size(5, 5), 3, 3, Core.BORDER_DEFAULT);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        gaussianSharpen(img);
        SobelTransform(img);
        gaussianSharpen(img);
        applyMask(img);
        applyCrop(img);
        medianBlur(img);
        Imgproc.adaptiveThreshold(img, img, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 11, 12);
    }
    
    /**
     * Testing: gets a processed image and runs matches against the whole RIDB.
     * If it matches where it shouldn't or doesn't match when it should, then it returns false.
     * @param src Processed image
     * @param pNum Person number that the image belongs to
     * @param fileName Name of the image file used for 'src'
     * @return
     */
    public static boolean matchAgainstRIDB(Mat src, int pNum, String fileName) {
        for(int i = 1; i <= 20; i++) { // Loop that processes every image in RIDB and runs the match against 'src'
        	for (int j = 1; j <= 5; j++) {
        		Mat src2 = Imgcodecs.imread("RIDB/IM00000" + j + "_" + i + ".JPG"); 
                processImage(src2);
                Boolean isMatch = checkMatching(src, src2);
                if(isMatch && pNum != i || !isMatch && pNum == i) {
                	try {
                		// writes out where it failed into text file
                		FileWriter myWriter = new FileWriter("test_log.txt", true);
                		myWriter.append("Test failed at " + fileName + " comparing to " + j + "_" + i + '\n');
                		myWriter.close();
                	}
                	catch(Exception e) {}
                	System.out.println("Test failed at " + fileName + " comparing to " + j + "_" + i);
                	return false;
                }
        	}
        }
        System.out.println("Success");
    	return true;
    }
 }

