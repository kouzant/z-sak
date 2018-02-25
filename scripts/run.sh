#!/bin/bash

ZSAK_CLASSPATH=.:libs/*:z-sak-${project.version}.jar:$ZSAK_CLASSPATH
java -cp $ZSAK_CLASSPATH gr.kzps.Zsak
