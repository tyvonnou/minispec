# minispec

## Description

School project of metaprogramming whose goal is to generate java code.

There are two versions available. 

The first one generates a simple java class with constructor and attribute from an XML file.

The second one generates, still from an xml file, several java classes with a management of links between the classes and the implementation of collections (List, Set, Bag, Array). This version uses the visitor patern.

There is also the possibility to generate an XML file from the java objects.

## Inputs 

The basic version will take as input the file **dtd/Satellite.xml** and the version using the visitor **dtd/SatelliteVisitor.xml**.

The XML generator also use the file **dtd/Satellite.xml** as input file.

## How to run

To launch the project you just have to execute the file **src/test/JavaGenerateTest.java** with JUnit.

## Outputs

The generated code of the basic version can be found in the package **src/generated**.

The generated code for the version that uses the visitor patern can be found in the package **src/generatedVisitor**.

The generated XML file is the file **dtd/OUTPUTDOMTEST.xml**.

## Author

* [Théo Yvonnou](https://github.com/tyvonnou)