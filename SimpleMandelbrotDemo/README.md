Simple Mandelbrot Demo
====================

## Goal of this project
SimpleMandelbrotDemo is a project I started as a training project to practice Java programming and to learn different concepts (multithreading, Swing gui) with a problematic of my choice.  
At the time of writing, mostly all the features I wanted to implement is coded with some basic tests executed. Further improvement, bug correction or fully advanced tests may or may not be done if I don't have time or the desire.  
As a non professional project, I recommend to be very careful if you want to reuse some parts of code. The code and the methods used are not optimal but I hope this project could help someone.  
Feel free to transmit me some bugs or possible improvements.

## Overview
### Quick description
This application is coded in Java (Java 7) and after some computation displays a Mandelbrot set.

### Main features
- compute and display a fractal / Mandelbrot set with different colors
- use different multithreading methods / approaches to do the computation
- let the user to modify different options as the number of threads to use or the image size
- possibility to zoom or unzoom a particular region by drawing a rectangular selection with the mouse
- in case of zoom, the new image is computed with the new parameters
- possibility to save an image
- benchmark dialog to do some tests between each multithreading approaches

## Detailed description
### Mandelbrot set
According to the [Wikipedia](https://en.wikipedia.org/wiki/Mandelbrot_set) article about Mandelbrot set :
> The Mandelbrot set is the set of values of c in the complex plane for which the orbit of 0 under iteration of the complex quadratic polynomial 
> ![alt text](https://upload.wikimedia.org/math/5/a/d/5adf5f6cc8f7e30a1fdb1c37bbb785c3.png "Mandelbrot sequence") 
> remains bounded.
For our purpose, to compute the Mandelbrot set :
- the complex plan <==> the image
- for each pixel of coordinate (i, j) in the image, using the previous recurrence relation test if it is bounded  

To implement this condition for our computers (quote from [Introduction to Programming in Java book](http://introcs.cs.princeton.edu/java/32class/)) :
> Given a complex point, we can compute the terms at the beginning of its sequence, but may not be able to know for sure that the sequence remains bounded. 
> Remarkably, there is a test that tells us for sure that a point is not in the set: if the magnitude of any number in the sequence ever gets to be greater than 2 (like 3 + 0i), then the sequence will surely diverge.  

In Java now :
```Java
public static int mand(Complex z0, int maxIteration) { 
   Complex z = z0; 
   for (int t = 0; t < maxIteration; t++) { 
       if (z.abs() >= 2.0) return t; 
       z = z.times(z).plus(z0); 
   }   
   return maxIteration; 
} 
```

### Rendering colors
I use a basic method to display the fractal in color :
- greyscale level : I(i, j) = 255 - mand(z0, 255) where any pixel in the Mandelbrot set are black, the others have an intensity corresponding to the iteration where the test failed
- color : same principle, one channel is used to do the intensity variation

![alt text](https://github.com/catree/SimpleMandelbrotDemo/blob/master/SimpleMandelbrotDemo/Mandelbrot.png "Mandelbrot picture")

### Live / offline rendering
In case of an offline rendering, the image is displayed only when all calculations are done.  
Live rendering consists to display the image at a regular interval, during the computation of the fractal.  

### Single thread rendering
We need a system of synch in case of live rendering : 
- one background thread which computes all the computation for the whole image
- the Swing EDT (Event Dispatch Thread) from which we ask to render the image at regular interval of time
One lock object is used, we request the lock before writing in the BufferedImage and before displaying the image to avoid asynchronous access.

### Multi-thread rendering
#### Regular subdivision
With this method, we divide the image by the number of chosen threads, each thread will compute the rendering for a part of the final image.  
The same mechanism of synch is used than in the case of live single thread rendering.  
For example in case of a number of threads :
- perfectly square (4, 9, 16, etc.) : we divide the width and the height by the square root of the number of threads
![alt text](https://github.com/catree/SimpleMandelbrotDemo/blob/master/SimpleMandelbrotDemo/MandelbrotThread1.png "Regular subdivision, perfect square number of threads")  

- even : we divide the height by two and we divide the width by half the number of threads
![alt text](https://github.com/catree/SimpleMandelbrotDemo/blob/master/SimpleMandelbrotDemo/MandelbrotThread2.png "Regular subdivision, even number of threads")  

- odd : we divide the width by the number of threads  
![alt text](https://github.com/catree/SimpleMandelbrotDemo/blob/master/SimpleMandelbrotDemo/MandelbrotThread3.png "Regular subdivision, odd number of threads")  
 
 This method of rendering is obviously better than using a single thread but when we experiment we realize that some threads finish their tasks faster than others. 
 This is particularly the case in black areas where pixels does't belong to the Mandelbrot set.  
 To improve this, we want a method which maximize the number of working threads in the same time.  
 
#### Multi-patch subdivision
This time, the subdivision is made not by the number of chosen threads but by the size of an elementary patch.  
For example : 
- the displayed image is 1000 x 1000
- the patch size is 100 x 100
- each thread will compute a part of the image of size 100 x 100
- there will be 100 tasks : we use a pool of threads with 100 submitting tasks but with only the number of chosen threads active in same time  
 
When we experiment, this method is better than the previous as we could suspect.
 
#### Fork / Join multithreading
Fork / Join is a multithreading pattern introduce with Java 7 and based on the principle of divide and conquer algorithms.
> The fork/join framework is an implementation of the ExecutorService interface that helps you take advantage of multiple processors.
> It is designed for work that can be broken into smaller pieces recursively. The goal is to use all the available processing power to enhance the performance of your application.
> As with any ExecutorService implementation, the fork/join framework distributes tasks to worker threads in a thread pool.
> The fork/join framework is distinct because it uses a work-stealing algorithm. Worker threads that run out of things to do can steal tasks from other threads that are still busy.  

Oddly, when using the same parameter than for the multi-patch subdivision to divide the work recursively, we obtain better result with the multi-patch subdivision.  
The gap could be negligible if we do not compare the processing time.

### User interface
#### Main interface
![alt text](https://github.com/catree/SimpleMandelbrotDemo/blob/master/SimpleMandelbrotDemo/SimpleMandelbrotDemo.png "Main interface")
- (1) Start button to launch the computation and display the fractal
- (2) Display the number of chosen threads to do the calculation and the processing time
- draw a rectangle selection (drag with left mouse click) from left to right : zoom on a specific area corresponding to the rectangle selection
- draw a rectangle selection  (drag with left mouse click) from right to left : load the previous Mandelbrot image before the zooming 
- Option menu > Save image : save the image on disk
- Option menu > Parameters : open a dialog to configure the different parameters
- Option menu > Benchmark : open a dialog to run multiple times the calculation to compare the impact of the different parameters
- Help menu > Help / License / About : open a dialog and display help, license and about content

#### Parameters dialog
- Modify the number of threads used to do the computation
- Choose a color method
- Choose a multithreading method, if the number of chosen threads is one, all methods will be the same (single thread rendering)
- Choose between live / offline rendering
- Choose the patch size
- Choose the Fork / Join threshold starting from the division is made
- Choose between dynamic and fixe size : dynamic size will adapt the rendering size based on the window size of the application, fixe size permits to choose a rendering size and allows to save for example an image of size 3000 x 3000
- Choose the bounds of the complex plane to render a particularly area of the fractal