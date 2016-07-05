# LSDM2016
The repository for the programming assignment of LSDM at L3S in 2016.

Clone the repository and navigate in the LSDM folder to find the "master.sh" script. If you execute it, it will ask you for 2 arguments:

1. The first is the complete path (the path MAY NOT contain any whitespaces) to the graph file. Type in something like "/home/user/Documents/graphfile" (without quotation marks).
2. The second argument (delimited by a whitespace from the first argument) is the task you want to execute: There are three tasks available:

2.1 "indexing" creates the index of the graph. Afterwards you can query the index and ask for the number of hops between two vertices. Type in for example "1 2" and press enter.

2.2 "clustering" first asks you for the modularity value of the graph that you want to accomplish by your clustering. Type in for example "0.75". After the clustering is done, you can type in two vertices to learn if they belong to the same cluster. Type in for example "1 2" and press enter.

2.3 "counting" counts the triangles of the graph. Afterwards you can query the vertices and find out what the cluster coefficient of a specific vertex is. Type in for example "1" and press enter.

Give it a try!
