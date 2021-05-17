Jar Analyzer
================

Jar Analyzer is open source solution that attempts to provide detailed analysis of .jar file.

Today, this analyzer will give you following details:
1. Total number of classes.
2. Total number of public methods.
3. List of classes.
4. List of public methods.
5. List of 'NoDefinitionFound' classes, etc.

How to use it?
-------------
### Prerequisite

You need to have Java installed and setup in path.

### Steps

1. Download this repository into your local machine, using either Download ZIP option or you can clone it.
2. Unzip the "Jar-Details-master.zip", if you have downloaded the zip version.
3. Compile the code;
   You can use your favorate command prompt and travel to unziped directory and using Java compiler compile it.
   Ex.:
   ```
   Downloads\Jar-Details-master\Jar-Details-master>javac JarDetails.java
   ```
4. Now provide your .jar file to this analyzer for analysis.
   Ex.:
   ```
   java JarDetails --jarFile=D:\jars\xyzjar.jar
   ```
    Output:
    ```
    #####################################
    Total classes = 45
    Total public methods = 0
    #####################################
    List of classes is stored into :classes.txt
    List of public methods is stored into :publicmethods.txt
    List of 'NoDefinitionFound' classes is stored into :nodeffoundclasses.txt
    #####################################
    ```
5. Now, to check classes and public methods, open "classes.txt" and "publicmethods.txt" created into your current directory.
