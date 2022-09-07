# COMPX301 - Assignment 4
### Biometric Recognition Fom Retinal Fundus Images

---

### Project Specification

In this project we will be using computer vision techniques to see if a person can be uniquely identified from their retina images. This is an important application in security, since it can be used to verify a person’s identity. You have probably seen retina scanners used in the movies quite a lot to let characters into certain restricted high-tech areas. A retinal image is taken using a retinal fundus camera that scans the eye. Typically, the patterns of veins on the retinal image are so unique to an individual that they can be used like a fingerprint. Below are four such images. The top two images are different images from the same individual, and the bottom two images are from a different individual. You can clearly see that the vein patterns between the two individuals are quite different, while they are more or less the same between images of the same individual

Since this is a project, there is no single algorithm or solution I will give you to implement. Instead, you should come up with your own computer vision pipeline made up of operations covered in the computer vision part of the course.

---
### Installation
```sh
git clone https://github.com/shippi/COMPX301-A4
```
This project utilizes the OpenCV library for image processing and image matching, so you will need to install the java version of that in order for the program to work. Please see the install instructions here: https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html

---

### How to use:
```sh
javac RetinalMatch.java
java RetinalMatch <path to image 1>.jpg <path to image 2>.jpg
```

---
### Other Notes:
The report in this repository goes over the computer vision pipeline used to process an individual image and the other operations that were tested but didn't make it into the final algorithm. It also goes over the matching algorithm used and the overall accuracy of our program.
