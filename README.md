# COMP3109 Assignment 3

## Compiler Optimisation

This is an implementation of a Compiler Optimiser for the Intermediate Language defined in the [assignment specification](A03-Optimizer.pdf).

### How to Run

Use the provided script `optimise.sh`. This can be used... _describe the steps_.

### Documentation

The code is heavily commented. JavaDoc HTML documentation has been generated from these and they can be found [online here](http://craigsketchley.github.io/COMP3109_Assignment3/docs/) or within the [`docs` folder](docs/).

Packages:

At the root level of src we have IntermediateCodeOptimiser, which is the entry point to the program

cfg
This contains the Control Flow Graph

genKillFramework
This contains the flow analysis classes
These are centered around the abstract class DataFlowAnalysis, which implements gen/kill and attendant methods for transfer and meet functions. Each subclass in this package performs the analysis for a different optimisation.

optimisation
This contains the optimisations, centered around the abstract class Optimisation. Implemented here as subclasses are the key optimisations required for the assignment. In addition we have implemented Constant Propagation.

intermediateLangauge
The contains the parser and classes required for the AST

lattice
This contains a class for generic lattice objects (essentially a wrapper that adds the TOP and BOTTOM states to a type, and methods to correctly and monotonically merge lattice values. 

## Contributors

- Joe Godbehere (312160976)
- Ricky Ratnayake (309213134)
- Craig Sketchley (312127790)
