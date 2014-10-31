# COMP3109 Assignment 3 - Compiler Optimisation

This implementation of a Compiler Optimiser for the Intermediate Language defined in the [assignment specification](A03-Optimizer.pdf).

## How to Run

Use the provided script `optimise.sh`. This can be used... _describe the steps_.


## File/Folder Structure

_I was thinking we could detail the folder structure and describe what each package/file is responsible for_

+-- CFG
|   +-- ControlFlowGraph.java
|   +-- ControlFlowGraphTest.java
|   +-- Node.java
+-- IntermediateLanguage
|   +-- BinOpInstruction.java
|   +-- Block.java
|   +-- BrInstruction.java
|   +-- CallInstruction.java
|   +-- Function.java
|   +-- Instruction.java
|   +-- LcInstruction.java
|   +-- LdInstruction.java
|   +-- Parser.java
|   +-- ParserTest.java
|   +-- Program.java
|   +-- Register.java
|   +-- RetInstruction.java
|   +-- StInstruction.java
+-- Lattice
|   +-- Lattice.java
+-- comp3109a3
|   +-- IntermediateCodeOptimiser.java
+-- genKillFramework
|   +-- Analysable.java
|   +-- AvailableExpressionsAnalysis.java
|   +-- AvailableExpressionsAnalysisTest.java
|   +-- DataFlowAnalysis.java
|   +-- LiveVariableAnalysis.java
|   +-- LiveVariableAnalysisTest.java
|   +-- LoadVariableAnalysis.java
+-- optimisation
    +-- DeadCodeEliminationOptimisation.java
    +-- DeadCodeEliminationOptimisationTest.java
    +-- Optimisation.java
    +-- Optimiser.java
    +-- OptimiserTest.java
    +-- RedundantLoadOptimisation.java

    
## Contributors

- Joe Godbehere (SID)
- Ricky Ratnayake (SID)
- Craig Sketchley (312127790)

    