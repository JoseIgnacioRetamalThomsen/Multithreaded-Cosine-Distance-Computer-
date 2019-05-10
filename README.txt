     A Multithreaded Cosine Distance Computer 
 --------------------------------------------------
 
 Jose Ignacio Retamal G00351330
 
 
   Steps for run:
----------------------
 1.- Start application using : java–cp ./oop.jar  ie.gmit.sw.Runner
 2.- Select subject directory and then press OK or use select path button.
 3.- Select query file and press OK or use select path button.
 4.- Click "start comparng files..." button.
 
 
      Abstract 
 -------------------
      The solutions aim to perform most of the work on parallel. Each file is done separately,
    while one thread is parsing file and creating shingles other is using hashmaps for count those 
    shingles. Then the count of each file is place in a queue for be comparing against the query file by 
    another thread.
 
 
     Features 
-------------------
 
 *- Compare files using fixed-size group of words(shingles) or fixed size block of 
    characters(k-mers).
      Use a interface that define how to create shingles from string line. One implementation 
     give shingles and other give k-mers.
       Type of shingles can be selected in UI>

*- Parallelizing work, I tried to improve performance by parallelizing some task:
   -Parsing file and building map, while one thread is reading from file and creating shingles 
      other is taking those shingles and building the maps.
	-Parsing file/building map and calculating cosine distances, one thread parse files and build map 
	   and another thread calculate cosine distances.

*-  Represent shingles using hashcode and use a hashmap for count shingles.
     Shingles are first grouped and then counted using the hash code, a map with a 
     key set of shingles and a value with the count of the shingle key is used.

*-  Builds maps using several threads.
      My first tough was that with this design was not necessary to use multiple thread to put shingles in
      the hashmap use for count, because since I use compute method for count in map only one thread can be 
      counting in the map at a time. But when testing, using several threads shows some performance increase.

*-  Use Counter hashmaps, which are maps where the key value is mapped to his total number of appearances.
    The new java 8 feature for ConcurrentHashMap “compute” is used for increase the count when shingles are added
    to the map

*-  Limit memory usage.
      The amount of memory used is limited by the queue that give counted maps to Calculator for compute the 
     results. So the program can process any number of files with out running out of memory. In other hand the 
     results are not cached for process quickly different files again same subject directory.
       This give no limit on the amount of files the program can process.

-*   Big algorithm  for very big amount of shingles, good for very long files or when small size shingles are user.
     Use java BigDecimal for deal with float point problems.

*-  Graphic User interface using javafx
      Use javafx concurrent classes for a multithread UI, show progress of query’s again a subject directory, dynamically 
      add results to UI.
      Easy select file and subject directory using javaFx file/directory chooser.

*-  Distributed system. Allow to connect to many remote computers for calculate cosine distance on files on those computers.
      Use java net for connect to remote clients and compute cosine distance against subject folder on remote computer.

     Instruction for connect remote clients

     1) Run normal client in server computer.
     2) Click “add remote service” button.
     3) Run remote client using : java–cp ./oop.jar  ie.gmit.sw.RemoteClientRunner
     4) Enter client ip address and port 82
     5)Enter full path of subject directory in remote client.
     6) Select subject director and query file in normal client.
     7)Click start comparing file in normal client.

*******Warning: the remote thread must be manually stopped by clicking “Stop listing for remote” in normal 
*******client server, if the normal client server is just closed thread will be still running.
     I make a video of the a test of this feature:
     https://vimeo.com/user73416298/review/309773960/86732f6084