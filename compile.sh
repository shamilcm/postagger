#!/bin/bash
javac -sourcepath src -d build src/Model.java
javac -sourcepath src -d build src/build_tagger.java
javac -sourcepath src -d build src/run_tagger.java
javac -sourcepath src -d build src/evaluate_tagger.java
