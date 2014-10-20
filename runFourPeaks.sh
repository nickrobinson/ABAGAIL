#!/bin/bash
# edit the classpath to to the location of your ABAGAIL jar file

# FourPeaks
echo "FourPeaks 10 iters"
{ time java -cp ABAGAIL.jar opt.test.FourPeaksTest 200 2000 2000 200;}

echo "FourPeaks 10 50 iters"
{ time java -cp ABAGAIL.jar opt.test.FourPeaksTest 200 4000 4000 300;}

echo "FourPeaks 10 200 iters"
{ time java -cp ABAGAIL.jar opt.test.FourPeaksTest 200 8000 8000 400;}

echo "FourPeaks 10 500 iters"
{ time java -cp ABAGAIL.jar opt.test.FourPeaksTest 200 16000 16000 500;}

echo "FourPeaks 10 2000 iters"
{ time java -cp ABAGAIL.jar opt.test.FourPeaksTest 200 32000 32000 600;}

echo "FourPeaks 10 2000 iters"
{ time java -cp ABAGAIL.jar opt.test.FourPeaksTest 200 64000 64000 700;}

echo "FourPeaks 10 2000 iters"
{ time java -cp ABAGAIL.jar opt.test.FourPeaksTest 200 128000 128000 800;}
