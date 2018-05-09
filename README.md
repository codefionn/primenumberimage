# Prime Number Image Generator

Genarates a image, where one color represents prime numbers (Default: white) and
the other one represents numbers, which can be divided by something else than 1
or itself (Default: black). There are different types, how to render the pixels
to the image: xy, yx, spiral.

## Build

```
$ ./build.sh
```

## Execute

```
$ java -jar primenumberimage.jar
```

## Program Help

```
Usage:
    primenumberimage [output-file] [type] [width] [height] ([prime-color] [non-prime-color])
    [output-file]: The file to write the image to.
                   Should end if an image extention.
    [type]: How to paint the pixels on the image
            - xy: Paint x first, then switch to next y (if x reached width)
            - yx: Paint y first, then switch to next x
            - spiral: Paint a spiral
    [width]: Width of the image in pixels
    [height]: Height of the image in pixels
    [prime-color]: Number from 3 bytes (red,green,blue). Color for primes
    [non-prime-color]: Number from 3 bytes (red,green,blue). Color for non-primes
Description:
    Generates an image, which visualizes all prime numbers till
    the number [width] * [height]. With [prime-color] (Default: FFFFFF)
    as color for primes and [non-prime-color] (Default: 000000) for
    non prime numbers
Examples:
    Generate an image with the size 1920x1080 (widthxheight) with
    file name 'image.png' and type spiral:
      $ primenumberimage image.png spiral 1920 1080
    An image with size 128x128 (widthxheight), filename 'image.png',
    type 'xy' and green as color for primes and black for non-primes
      $ primenumberimage image.png xy 128 128 00FF00 000000
```

## Examples

size: 1920x1080, colors: 77AAFF 000000, type: spiral
![Spiral1920x1080](/images/spiral.png)

size: 1920x1080, colors: 77AAFF 000000, type: xy
![XY1920x1080](/images/xy.png)

size: 1920x1080, colors: 77AAFF 000000, type: yx
![YX1920x1080](/images/yx.png)
