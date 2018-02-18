#!/bin/bash

CLASSPATH=.:target/libs/*:target/z-sak-1.0-SNAPSHOT.jar $CLASSPATH
java -cp $CLASSPATH gr.kzps.Zsak
