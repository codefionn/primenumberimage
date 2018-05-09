package feder.primenumberimage;

/*  fionn/primenumberimage/ProgramMain.java: Main file of the program
    Copyright (C) 2018  Fionn Langhans

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

//import java.lang.*;
import java.util.List;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

class ProgramMain
{
    private static final void printHelp()
    {
        System.out.println("Usage:");
        System.out.println("    primenumberimage [output-file] [type] [width] [height] ([prime-color] [non-prime-color])");
        System.out.println("    [output-file]: The file to write the image to.");
        System.out.println("                   Should end if an image extention.");
        System.out.println("    [type]: How to paint the pixels on the image");
        System.out.println("            - xy: Paint x first, then switch to next y (if x reached width)");
        System.out.println("            - yx: Paint y first, then switch to next x");
        System.out.println("            - spiral: Paint a spiral");
        System.out.println("    [width]: Width of the image in pixels");
        System.out.println("    [height]: Height of the image in pixels");
        System.out.println("    [prime-color]: Number from 3 bytes (red,green,blue). Color for primes");
        System.out.println("    [non-prime-color]: Number from 3 bytes (red,green,blue). Color for non-primes");
        System.out.println("Description:");
        System.out.println("    Generates an image, which visualizes all prime numbers till");
        System.out.println("    the number [width] * [height]. With [prime-color] (Default: FFFFFF)");
        System.out.println("    as color for primes and [non-prime-color] (Default: 000000) for");
        System.out.println("    non prime numbers");
        System.out.println("Examples:");
        System.out.println("    Generate an image with the size 1920x1080 (widthxheight) with");
        System.out.println("    file name 'image.png' and type spiral:");
        System.out.println("      $ primenumberimage image.png spiral 1920 1080");
        System.out.println("    An image with size 128x128 (widthxheight), filename 'image.png',");
        System.out.println("    type 'xy' and green as color for primes and black for non-primes");
        System.out.println("      $ primenumberimage image.png xy 128 128 00FF00 000000");
    }

    private static final boolean isPrime(List<Integer> primesList, int number)
    {
        if (number < 1)
            return false;

        int max = (int) Math.ceil((int) Math.sqrt((double) number));
        for (Integer I : primesList) {
            if (I.intValue() > max)
                return true;
            if (number == I.intValue())
                return true;

            if (number % I.intValue() == 0) {
                return false;
            }
        }

        return true;
    }

    private static final boolean isImageWriteExt(String ext)
    {
        for (String posExt : ImageIO.getWriterFileSuffixes()) {
            if (posExt.equalsIgnoreCase(ext)) {
                return true;
            }
        }

        return false;
    }

    public static final void main(String[] args)
    {
        System.out.println("PrimeNumberImage  Copyright (C) 2018 Fionn Langhans");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY;");
        System.out.println("This is free software, and you are welcome to redistribute it");
        System.out.println("under certain conditions;");
        System.out.println();

        if (args.length != 4 && args.length != 6) {
            System.err.println("Error: Invalid number of arguments");
            printHelp();
            System.exit(1); // Error
        }

        String outputFilePath = args[0];
        if (outputFilePath.isEmpty()) {
            outputFilePath = "image.png";
        }
        
        String type = args[1];
        int itype = 0;
        final int TYPE_XY = 0;
        final int TYPE_YX = 1;
        final int TYPE_SPIRAL = 2;
        if (type.equalsIgnoreCase("xy"))
            itype = TYPE_XY;
        else if (type.equalsIgnoreCase("yx"))
            itype = TYPE_YX;
        else if (type.equalsIgnoreCase("spiral"))
            itype = TYPE_SPIRAL;
        else {
            System.err.println("Not a valid type");
            System.exit(1);
        }

        final char[] ILLEGAL_CHARACTERS = { '\n', '\r',
            '\t', '\0', '\f', '`', '?', '*', '<', '>', '|', '\"', ':' };

        for (char c : ILLEGAL_CHARACTERS) {
            if (outputFilePath.indexOf(c) >= 0) {
                System.err.println("Invalid file path given. Must not contain "
                    + "?, `, ?, *, <, >, |, \", :");
                System.exit(1); // Error
            }
        }

        int width = 0, height = 0;

        try {
            width = Integer.decode(args[2]);
            height = Integer.decode(args[3]);
        } catch (IllegalArgumentException ex) {
            System.err.println("Width & height must be integers!");
            printHelp();
            System.exit(1);
        }

        int colorPrime = 0xFFFFFF, colorNonPrime = 0x000000;
        if (args.length == 6) {
            try {
                colorPrime = Integer.decode("0x" + args[4]);
                colorNonPrime = Integer.decode("0x" + args[5]);
            } catch (IllegalArgumentException ex) {
                System.err.println("prime-color & non-prime-color must be 3-byte long hexadecimal numbers");
                printHelp();
                System.exit(1);
            }
        }

        // System.out.println("# width=" + width + ", height=" + height);
        System.out.println("Redering image. Please be patient. Numbers: " + (width * height));

        int cx = width / 2, cy = height / 2;
        int maxx = 0, maxy = 1;
        int pmaxx = cx, pmaxy = cy - maxy;
        int mx = 0, my = -1;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        int length = pixels.length;
        if (itype == TYPE_SPIRAL) {
            length = width > height ? width * width : height * height;
        }

        List<Integer> primesList = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            int color = 0;
            if (isPrime(primesList, i+1)) {
                color = colorPrime;
                if (i != 0) primesList.add(Integer.valueOf(i+1));
            } else {
                color = colorNonPrime;
                if (colorNonPrime < 0)
                    color = (int) i;
            }

            if (itype == TYPE_XY)
                pixels[i] = color;
            else if (itype == TYPE_YX) {
                // index = x + y * WIDTH // Confuration of the image
                // Of that configuratoin y is:
                // y = floor(index / WIDTH)
                //   (Note: x < WIDTH !!)
                // x = index - y * WIDTH
                // => x = index - (index % WIDTH) * WIDTH

                // But we want a solution for:
                // index0 = x * HEIGHT + y
                // => Solution:
                // Determine x of index:
                int x = (int) Math.floor(i / height);
                // Determine y of index:
                int y = (int) i - x * height;
                
                pixels[/* The index index = x + y * WIDTH */ x + y * width] = color;
            } else if (itype == TYPE_SPIRAL) {
                if (!(cx < 0 || cy < 0 || cx >= width || cy >= height))
                    pixels[cx + cy * width] = color;

                cx += mx;
                cy += my;

                if (cx == pmaxx && mx != 0) {
                    maxy += 1;
                    if (mx < 0) {
                        my = -1;
                    } else {
                        my = 1;
                    }

                    mx = 0;
                    pmaxy = cy + maxy * my;
                } else if (cy == pmaxy && my != 0) {
                    //maxx += 1;
                    maxx = maxy;
                    if (my < 0) {
                        mx = 1;
                    } else {
                        mx = -1;
                    }

                    my = 0;
                    pmaxx = cx + maxx * mx;
                }
            }
        }

        String[] splitFileName = outputFilePath.split("\\.");
        String ext = splitFileName[splitFileName.length-1];
        if (ext.contains("\\") || ext.contains("/")
                || ext.equals(outputFilePath)
                || ext.equals("." + outputFilePath)
                || !isImageWriteExt(ext)) {
            ext = "png";
            outputFilePath += ".png";
        }

        ext = ext.toUpperCase();

        File file = new File(outputFilePath);
        if (file.exists()) {
            System.err.println("Write to: " + outputFilePath);
            if (file.isDirectory()) {
                System.err.println("File output path is a directory. Delete it, if you want to continue.");
                System.exit(1);
            }

            try {
                System.err.print("File output path is a file. Delete it ? (Y/n): ");
                int c;
                while ((c = System.in.read()) != 'y' && c != 'n' && c != '\0' && c != '\n' && c != 'Y') {
                    System.err.print("Not understood. Delete ? (Y/n): ");
                }

                if (c == 'n' || c == '\0') {
                    System.err.println("Don't override file. Exit");
                    System.exit(1);
                }
            } catch (IOException ex) {
                System.err.println("IOException while reading from stdin");
                System.exit(1);
            }
        }

        try {
            if (!ImageIO.write(image, ext, file)) {
                System.err.println("Writer not found!");
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}
