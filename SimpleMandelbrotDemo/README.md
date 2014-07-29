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
> ![alt text](https://upload.wikimedia.org/math/1/6/8/1686ce42df2b6ee51a3ae880613ca4d9.png "Mandelbrot formula")
> remains bounded.