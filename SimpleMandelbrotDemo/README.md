SimpleMandelbrotDemo
====================

## Goal of this project
SimpleMandelbrotDemo is a project I started as a training project to practice Java programming and to learn different concepts (multithreading, Swing gui) with a problematic of my choice.  
At the time of writing, mostly all the features I wanted to implement is coded with some basic tests executed. Further improvement, bug correction or fully advanced tests may / will be added / done.

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
'''Java
public static int mand(Complex z0, int maxIteration) { 
   Complex z = z0; 
   for (int t = 0; t < maxIteration; t++) { 
       if (z.abs() >= 2.0) return t; 
       z = z.times(z).plus(z0); 
   }   
   return maxIteration; 
} 
'''

### Rendering colors
I use a basic method to display the fractal in color :
- greyscale level : I(i, j) = 255 - mand(z0, 255) where any pixel in the Mandelbrot set are black, the others have an intensity corresponding to the iteration where the test failed
- color : same principle, one channel is used to do the intensity variation

![alt text](https://github.com/catree/SimpleMandelbrotDemo/blob/master/SimpleMandelbrotDemo/Mandelbrot.png "Mandelbrot picture")

### Live / offline rendering
In case of an offline rendering, the image is displayed only when all calculations are done.  
Live rendering consists to display the image at a regular interval, during the computation of the fractal.  

### Single thread rendering

### Multi-thread rendering
