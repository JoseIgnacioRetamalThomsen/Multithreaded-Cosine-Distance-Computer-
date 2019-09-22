 # Multithreaded Cosine Distance Computer 

 ![UML](https://github.com/JoseIgnacioRetamalThomsen/Multithreaded-Cosine-Distance-Computer-/blob/master/design.png)

 [JavaDocs](https://joseignacioretamalthomsen.github.io/Multithreaded-Cosine-Distance-Computer-/)

 [Live Demo](https://vimeo.com/309773960)

 Jose Ignacio Retamal G00351330@gmit.ie
 
# Steps for run:

1. Start application using : java–cp ./oop.jar  ie.gmit.sw.Runner
1.  Select subject directory and then press OK or use select path button.
1.  Select query file and press OK or use select path button.
1.  Click "start comparing files..." button.
 
 
# Abstract 

Calculate cosine distance of text files.
The solutions aim to perform most of the work on parallel. Each file is done separately,
while one thread is parsing file and creating shingles other is using hash maps for count those 
shingles. 

 
 
# Features 

* Compare files using fixed-size group of words(shingles) or fixed size block of 
    characters(k-mers).
      Use a interface that define how to create shingles from string line. One implementation 
     give shingles and other give k-mers.
       Type of shingles can be selected in UI. 

* Parallelizing work, I tried to improve performance by parallelizing some task:

    -Parsing file and building map, while one thread is reading from file and creating shingles 
      other is taking those shingles and building the maps.

    -Parsing file/building map and calculating cosine distances, one thread parse files and build map 
	   and another thread calculate cosine distances.

*  Represent shingles using hash-code and use a hash-map for count shingles.
     Shingles are first grouped and then counted using the hash code, a map with a 
     key set of shingles and a value with the count of the shingle key is used.

*  Builds maps using several threads.
      My first tough was that with this design was not necessary to use multiple thread to put shingles in
      the hash-map use for count, because since I use compute method for count in map only one thread can be 
      counting in the map at a time. But when testing, using several threads shows some performance increase.

*  Use Counter hash-maps, which are maps where the key value is mapped to his total number of appearances.
    The new java 8 feature for ConcurrentHashMap “compute” is used for increase the count when shingles are added
    to the map.

*  Limit memory usage.
      The amount of memory used is limited by the queue that give counted maps to Calculator for compute the 
     results. So the program can process any number of files with out running out of memory. In other hand the 
     results are not cached for process quickly different files again same subject directory.
       This give no limit on the amount of files the program can process.

*  Big algorithm  for very big amount of shingles, good for very long files or when small size shingles are user.
     Use java BigDecimal for deal with float point problems.

*  Graphic User interface using javafx
      Use javafx concurrent classes for a multithread UI, show progress of query’s again a subject directory, dynamically 
      add results to UI.
      Easy select file and subject directory using javaFx file/directory chooser.

*  Distributed system. Allow to connect to many remote computers for calculate cosine distance on files on those computers.
      Use java net for connect to remote clients and compute cosine distance against subject folder on remote computer.


# Instruction for connect remote clients

1. Run normal client in server computer.
1. Click “add remote service” button.
1. Run remote client using : java–cp ./oop.jar  ie.gmit.sw.RemoteClientRunner
1. Enter client ip address and port 82
1. Enter full path of subject directory in remote client.
1. Select subject director and query file in normal client.
1. Click start comparing file in normal client.

## Warning:
the remote thread must be manually stopped by clicking “Stop listing for remote” in normal 

client server, if the normal client server is just closed thread will be still running.
    
