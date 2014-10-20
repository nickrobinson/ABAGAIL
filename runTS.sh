#!/bin/bash
# edit the classpath to to the location of your ABAGAIL jar file

# TravelingSalesman
echo "TravelingSalesman 10 iters"
{ time java -cp ABAGAIL.jar opt.test.TravelingSalesmanTest 50 100 10000 100;}

echo "TravelingSalesman 10 50 iters"
{ time java -cp ABAGAIL.jar opt.test.TravelingSalesmanTest 50 200 20000 200;}

echo "TravelingSalesman 10 200 iters"
{ time java -cp ABAGAIL.jar opt.test.TravelingSalesmanTest 50 400 40000 400;}

echo "TravelingSalesman 10 500 iters"
{ time java -cp ABAGAIL.jar opt.test.TravelingSalesmanTest 50 800 80000 800;}

echo "TravelingSalesman 10 2000 iters"
{ time java -cp ABAGAIL.jar opt.test.TravelingSalesmanTest 50 1600 160000 1600;}

echo "TravelingSalesman 10 2000 iters"
{ time java -cp ABAGAIL.jar opt.test.TravelingSalesmanTest 50 3200 320000 3200;}

echo "TravelingSalesman 10 2000 iters"
{ time java -cp ABAGAIL.jar opt.test.TravelingSalesmanTest 50 6400 640000 6400;}
