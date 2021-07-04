Demonstrates two techniques aimed at breaking apart a graph into smaller, less interconnected components.
Test files are included to demonstrate functionality.

The first technique is removal of nodes in order of highest degree (# of neighbors). The second removes nodes in order
    of highest collective influence, as

Arguments are of form [-d] [-r RADIUS] num_nodes input_file
    -d is optional and instructs us to use removeByDegree() instead of removeByCI()
    -r is optional and instructs us to use RADIUS instead of the default value of r=2
    num_nodes is how many nodes to remove
    input_file is the .txt file containing two numbers per line, describing two connected vertices

   Ex. "-d 4 destruction_example_1.txt" instructs to remove the 4 nodes with highest degree from the example file
       "-r 2 6 destruction_example_2.txt" instructs to remove the 6 nodes with highest CI, updating the CI of every node
            after one is removed.

BreakNetwork.java:
    main(): Parses CLI arguments and calls removeByDegree() or removeByCI() accordingly
    removeByDegree(): Removes [num_nodes] nodes with highest degree
    BFS(): Performs breadth-first search traversal of graph, using an array to track which nodes have been visited.
            A queue holds visited nodes, separated by values of -1 to track changes in depth. If surfaceNodesOnly is true,
            returns only the nodes at exactly [radius] away from root node. If false, it returns those <= [radius] away.
    calculateCI(): Performs collective influence calculation according to project description.
    removeByCI(): Simply calls removeHighestCI() as many times as determined by program arguments.

Graph.java: Adjacency list representation of a graph implemented via HashMap. Stores the nodes in the graph as keys, and
    a set of their neighboring nodes as values.
    getHighestNode(): Determines the size of the visited[] array in BFS().

Future steps:
    Refine removeByCI() so that it does not rely on removeHighestCI() to update the CI of all nodes. Ideally, it should
        only update the CI for nodes whose distance from the removed node is <= RADIUS + 1

Edward Smakov
edwardsmakov@gmail.com
esmakov@u.rochester.edu