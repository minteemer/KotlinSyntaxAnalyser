# Compilers construction HA-3: Syntax analyzer
*Timur Valiev and Ilgizar Murzakov, BS3-DS-1*

## Description of the project
Program takes Kotlin code from `in.txt`, parses it, generates 
Abstract Syntax Tree and puts it in `out.txt` in JSON form.

This project is written on Kotlina and based on ANTLR4 library

The project has following structure: 
````
src/
    main/
        kotlin/ - source code of the project
            main.kt - entry point of the program
            KotlinSyntaxTreeGenerator.kt - generator of AST for Kotlin files
        antlr/ - rules for Kotlin lexer and parser
    test/
        kotlin/ - unit tests
        resources/ - test data
````

## How to run
### From console using Gradle
To build and run the project run `./gradlew run` form root folder of this project or 
`gradle run` if you have Gradle 4.10 installed (trying to build project on other 
versions is unrecommended).

To run unit tests run `./gradlew test` (or `gradle test`)

### From IDEA
Open the project in IDEA, synchronise Gradle and run `Tasks/application/run` task 
in Gradle window.

To run unit tests 

### Dependencies
- Kotlin
- Junit 5