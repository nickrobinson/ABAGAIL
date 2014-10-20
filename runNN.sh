#!/bin/bash
# edit the classpath to to the location of your ABAGAIL jar file

# NN
echo "NN 10 iters"
{ time java -cp ABAGAIL.jar func.test.NNClassificationTest 10;}  > logs/NN_NNClassificationTest_10iter.log 2>&1 &

echo "NN 10 50 iters"
{ time java -cp ABAGAIL.jar func.test.NNClassificationTest 50;}  > logs/NN_NNClassificationTest_50iter.log 2>&1 &

echo "NN 10 100 iters"
{ time java -cp ABAGAIL.jar func.test.NNClassificationTest 100;}  > logs/NN_NNClassificationTest_100iter.log 2>&1 &

echo "NN 10 500 iters"
{ time java -cp ABAGAIL.jar func.test.NNClassificationTest 500;}  > logs/NN_NNClassificationTest_500iter.log 2>&1 &

echo "NN 10 1000 iters"
{ time java -cp ABAGAIL.jar func.test.NNClassificationTest 1000;}  > logs/NN_NNClassificationTest_1000iter.log 2>&1 &

echo "NN 10 2000 iters"
{ time java -cp ABAGAIL.jar func.test.NNClassificationTest 2000;}  > logs/NN_NNClassificationTest_2000iter.log 2>&1 &

echo "NN 10 10000 iters"
{ time java -cp ABAGAIL.jar func.test.NNClassificationTest 10000;}  > logs/NN_NNClassificationTest_10000iter.log 2>&1 &
