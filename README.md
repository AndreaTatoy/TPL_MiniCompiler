# Mini Compiler
This project, created for TPL, demonstrates a basic compiler's workflow through three key stages: Lexical Analysis, Syntax Analysis, and Semantic Analysis. The MiniCompiler application allows you to analyze code step-by-step, providing feedback on errors or confirming success at each stage.

The additional files in this repository are test cases designed to evaluate the functionality and robustness of the MiniCompiler.

## Files in the Repository
**MiniCompiler.java** - This is the main program that provides the user interface and performs the following tasks:
- Lexical Analysis: Checks for valid tokens (keywords, identifiers, operators, etc.)
- Syntax Analysis: Ensures the code follows correct structural rules, like matching parentheses
- Semantic Analysis: Verifies the correct declaration and usage of variables and other semantics

This file must be compiled and run to use the application.

**Test Files**
- TestPassAll.java - Contains code that passes all stages of the MiniCompiler  
- TestPassToSyntax.java - Contains code that passes Lexical Analysis and Syntax Analysis but fails at Semantis Analysis
- TestPassLexical.java - Contains code that passes Lexical Analysis only and fails at further stages  
- TestPassNothing.java - Contains code that fails all stages of analysis  

These files help demonstrate the MiniCompiler’s ability to identify and handle errors at each stage.

## Developers

&nbsp;&nbsp;&nbsp;&nbsp;Developer 1: [Louise Andrea Tatoy](https://github.com/AndreaTatoy)  
&nbsp;&nbsp;&nbsp;&nbsp;Developer 2: [Trixie Vea Picaña](https:/github.com/TrixiePicana)  
&nbsp;&nbsp;&nbsp;&nbsp;Tester: [Allen Edward Pangilinan](https://github.com/AllenPangilinan)
