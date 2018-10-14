# Compilers construction HA-3: Syntax analyzer
*Timur Valiev and Ilgizar Murzakov, BS3-DS-1*

## Description of the project
Program takes Kotlin code from `in.txt`, parses it, generates 
Abstract Syntax Tree and puts it in `out.txt` in JSON format.

The parser recognises all Kotlin structures (Advanced task *b*)

### Structure
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
### Dependencies
- Kotlin
- ANTLR4
- GSON
- Junit 5


## How to run
### From console using Gradle
First make sure that you have JDK for Java 1.8 or higher installed. To build and 
run the project run `./gradlew run` form root folder of this project or `gradle run` 
if you have Gradle 4.10 installed (trying to build project on other versions is 
unrecommended).

To run unit tests run `./gradlew test` (or `gradle test`)

### From IDEA
Open the project in IDEA, synchronise Gradle and run `Tasks/application/run` task 
in Gradle window.

To run unit tests run `Tasks/verification/test` task in Gradle window or tests in
`src/test/kotlin/` folder.

## Used Kotlin grammar
The grammar is based on this [Kotlin grammar description](https://github.com/JetBrains/kotlin/tree/master/grammar). 

### Symbols and naming

_Terminal symbol_ names start with an uppercase letter, e.g. **SimpleName**.
_Nonterminal symbol_ names start with a lowercase letter, e.g. **kotlinFile**.
Each _production_ starts with `::=`.

### EBNF expressions

Operator `|` denotes _alternative_.
Operator `*` denotes _iteration_ (zero or more).
Operator `+` denotes _iteration_ (one or more).
Operator `[`something`]` denotes _option_ (zero or one).
alpha`{`beta`}` denotes a nonempty _beta_-separated list of _alpha_'s.

```EBNF
annotations ::=  (annotation | annotationList)*

annotation ::= "@" [annotationUseSiteTarget ":"] unescapedAnnotation

annotationList ::= "@" [annotationUseSiteTarget ":"] "[" unescapedAnnotation+ "]"

annotationUseSiteTarget ::= "field" | "file" | "property" | "get" | "set" | "receiver" | "param" | "setparam" | "delegate"

unescapedAnnotation ::= SimpleName{"."} [typeArguments] [valueArguments]



class ::= modifiers ("class" | "interface") SimpleName [typeParameters] [primaryConstructor] [":" annotations delegationSpecifier{","}] typeConstraints ([classBody] | enumClassBody)

primaryConstructor ::= [modifiers "constructor"] "(" functionParameter{","} ")"

classBody ::= ["{" members "}"]

members ::= memberDeclaration*

delegationSpecifier ::= constructorInvocation | userType | explicitDelegation

explicitDelegation ::= userType "by" expression

typeParameters ::= "<" typeParameter{","} ">"

typeParameter ::= modifiers SimpleName [":" userType]

typeConstraints ::= ["where" typeConstraint{","}]

typeConstraint ::= annotations SimpleName ":" type



memberDeclaration ::= companionObject | object | function | property | class | typeAlias | anonymousInitializer | secondaryConstructor

anonymousInitializer ::= "init" block

companionObject ::= modifiers "companion" "object" [SimpleName] [":" delegationSpecifier{","}] [classBody]

valueParameters ::="(" [functionParameter{","}] ")"

functionParameter ::= modifiers [("val" | "var")] parameter [("=" expression)]

block ::= "{" statements "}"

function ::= modifiers "fun" [typeParameters] [type "."] SimpleName [typeParameters] valueParameters [":" type] typeConstraints [functionBody]

functionBody ::= block | "=" expression

variableDeclarationEntry ::= SimpleName [":" type]

multipleVariableDeclarations ::= "(" variableDeclarationEntry{","} ")"

property ::= modifiers ("val" | "var") [typeParameters] [type "."] (multipleVariableDeclarations | variableDeclarationEntry) typeConstraints ["by" | "=" expression [";"]] ([getter] [setter] | [setter] [getter]) [";"]

getter ::= modifiers "get" | modifiers "get" "(" ")" [":" type] functionBody

setter ::= modifiers "set" | modifiers "set" "(" modifiers (SimpleName | parameter) ")" functionBody

parameter ::= SimpleName ":" type

object ::= modifiers "object" SimpleName [primaryConstructor] [":" delegationSpecifier{","}] [classBody]

secondaryConstructor ::= modifiers "constructor" valueParameters [":" constructorDelegationCall] block

constructorDelegationCall ::= "this" valueArguments | "super" valueArguments



controlStructureBody ::= block | blockLevelExpression

if ::= "if" "(" expression ")" controlStructureBody [";"] ["else" controlStructureBody]

try ::= "try" block catchBlock* [finallyBlock]

catchBlock ::= "catch" "(" annotations SimpleName ":" userType ")" block

finallyBlock := "finally" block

loop ::= for | while | doWhile

for := "for" "(" annotations (multipleVariableDeclarations | variableDeclarationEntry) "in" expression ")" controlStructureBody

while ::= "while" "(" expression ")" controlStructureBody

doWhile ::= "do" controlStructureBody "while" "(" expression ")"



enumClassBody ::= "{" enumEntries [";" members] "}"

enumEntries ::= [enumEntry{","} [","] [";"]]

enumEntry ::= modifiers SimpleName [valueArguments] [classBody]



expression ::= disjunction (assignmentOperator disjunction)*

disjunction ::= conjunction ("||" conjunction)*

conjunction ::= equalityComparison ("&&" equalityComparison)*

equalityComparison ::= comparison (equalityOperation comparison)*

comparison ::= namedInfix (comparisonOperation namedInfix)*

namedInfix ::= elvisExpression (inOperation elvisExpression)* | elvisExpression [isOperation type]

elvisExpression ::= infixFunctionCall ("?:" infixFunctionCall)*

infixFunctionCall ::= rangeExpression (SimpleName rangeExpression)*

rangeExpression ::= additiveExpression (".." additiveExpression)*

additiveExpression ::= multiplicativeExpression (additiveOperation multiplicativeExpression)*

multiplicativeExpression ::= typeRHS (multiplicativeOperation typeRHS)*

typeRHS ::= prefixUnaryExpression (typeOperation prefixUnaryExpression)*

prefixUnaryExpression ::= prefixUnaryOperation* postfixUnaryExpression

postfixUnaryExpression ::= atomicExpression postfixUnaryOperation* | callableReference postfixUnaryOperation*


callableReference ::= [userType "?"*] "::" SimpleName [typeArguments]

atomicExpression ::= "(" expression ")" | literalConstant | functionLiteral | "this" [labelReference]
| "super" ["<" type ">"] [labelReference] | if | when | try | objectLiteral
| jump | loop | collectionLiteral | SimpleName

labelReference ::= "@"LabelName

labelDefinition ::= LabelName"@"

literalConstant ::= "true" | "false" | stringTemplate | NoEscapeString | IntegerLiteral
| CharacterLiteral | FloatLiteral | "null"

stringTemplate ::= '"' stringTemplateElement* '"'

stringTemplateElement ::= RegularStringPart | ShortTemplateEntryStart (SimpleName | "this")
| EscapeSequence | longTemplate

longTemplate ::= "${" expression "}"

declaration ::= function | property | class | typeAlias | object

statement ::= declaration | blockLevelExpression

blockLevelExpression ::= annotations ("\n")+ expression

multiplicativeOperation ::= "*" | "/" | "%"

additiveOperation ::= "+" | "-"

inOperation ::= "in" | "!in"

typeOperation ::= "as" | "as?" | ":"

isOperation ::= "is" | "!is"

comparisonOperation ::= "<" | ">" | ">=" | "<="

equalityOperation ::= "!=" | "=="

assignmentOperator ::= "=" | "+=" | "-=" | "*=" | "/=" | "%="

prefixUnaryOperation ::= "-" | "+" | "++" | "--" | "!" | annotations | labelDefinition

postfixUnaryOperation ::= "++" | "--" | "!!" | callSuffix | arrayAccess | memberAccessOperation postfixUnaryExpression

callSuffix ::= [typeArguments] valueArguments annotatedLambda | typeArguments annotatedLambda

annotatedLambda ::= ("@" unescapedAnnotation)* [labelDefinition] functionLiteral

memberAccessOperation ::= "." | "?." | "?"

typeArguments ::= "<" type{","} ">"

valueArguments ::= "(" ([SimpleName "="] ["*"] expression){","} ")"

jump ::= "throw" expression | "return" [labelReference] [expression] | "continue" [labelReference] | "break" [labelReference]

functionLiteral ::= "{" statements "}" | "{" lambdaParameter{","} "->" statements "}"

lambdaParameter ::= variableDeclarationEntry | multipleVariableDeclarations [":" type]

statements ::= ";"* statement{";"+} ";"*

constructorInvocation ::= userType callSuffix

arrayAccess ::= "[" expression{","} "]"

objectLiteral ::= "object" [":" delegationSpecifier{","}] classBody

collectionLiteral ::= "[" [element{","}] "]"



modifiers ::= (modifier | annotations)*

typeModifiers ::= (suspendModifier | annotations)*

modifier ::= classModifier | accessModifier | varianceAnnotation | memberModifier
| parameterModifier | typeParameterModifier | functionModifier | propertyModifier

classModifier ::= "abstract" | "final" | "enum" | "open" | "annotation" | "sealed" | "data"

memberModifier ::= "override" | "open" | "final" | "abstract" | "lateinit"

accessModifier ::= "private" | "protected" | "public" | "internal"

varianceAnnotation ::= "in" | "out"

parameterModifier ::= "noinline" | "crossinline" | "vararg"

typeParameterModifier ::= "reified"

functionModifier ::= "tailrec" | "operator" | "infix" | "inline" | "external" | suspendModifier

propertyModifier ::= "const"

suspendModifier ::= "suspend"



kotlinFile ::= preamble topLevelObject*

script ::= preamble expression*

preamble ::= [fileAnnotations] [packageHeader] import*

fileAnnotations ::= fileAnnotation*

fileAnnotation ::= "@" "file" ":" ("[" unescapedAnnotation+ "]" | unescapedAnnotation)

packageHeader ::= modifiers "package" SimpleName{"."} [";"]

import ::= "import" SimpleName{"."} ["." "*" | "as" SimpleName] [";"]

topLevelObject ::= class | object | function | property | typeAlias

typeAlias ::= modifiers "typealias" SimpleName [typeParameters] "=" type



type ::= typeModifiers typeReference

typeReference ::= "(" typeReference ")" | functionType | userType | nullableType | "dynamic"

nullableType ::= typeReference "?"

userType ::= simpleUserType{"."}

simpleUserType ::= SimpleName ["<" (projection? type | "*"){","} ">"]

projection ::= varianceAnnotation

functionType ::= [type "."] "(" [parameter{","}] ")" "->" type


when ::= "when" ["(" expression ")"] "{" whenEntry* "}"

whenEntry ::= whenCondition{","} "->" controlStructureBody ";" | "else" "->" controlStructureBody ";"

whenCondition ::= expression | ("in" | "!in") expression | ("is" | "!is") type



LongSuffix ::= "L"

IntegerLiteral ::= DecimalLiteral [LongSuffix] | HexadecimalLiteral [LongSuffix] | BinaryLiteral [LongSuffix]

Digit ::= ["0".."9"]

DecimalLiteral ::= Digit | Digit (Digit | "_")* Digit

FloatLiteral ::= <Java double literal>

HexDigit ::= Digit | ["A".."F", "a".."f"]

HexadecimalLiteral ::= "0" ("x" | "X") HexDigit | "0" ("x" | "X") HexDigit (HexDigit | "_")* HexDigit

BinaryDigit ::= ("0" | "1")

BinaryLiteral ::= "0" ("b" | "B") BinaryDigit | "0" ("b" | "B") BinaryDigit (BinaryDigit | "_")* BinaryDigit

CharacterLiteral ::= <character as in Java>

NoEscapeString ::= <"""-quoted string>

RegularStringPart ::= <any character other than backslash, quote, $ or newline>

ShortTemplateEntryStart ::= "$"

EscapeSequence ::= UnicodeEscapeSequence | RegularEscapeSequence

UnicodeEscapeSequence ::= "\u" HexDigit{4}

RegularEscapeSequence ::= "\" <any character other than newline>

SimpleName ::= <java identifier> | "`" <java identifier> "`"

LabelName ::= SimpleName

/* Symbols:

[](){}<>
,
.
:
<= >= == != === !==
+ - * / %
=
+= -= *= /= %=
++ -- !
&& ||  -- may be just & and |
=>
..
?
?:
?.
*/

/* Keywords:
package
as
type
class
this
val
var
fun
extension
for
null
typeof
new
true
false
is
in
throw
return
break
continue
object
if
else
while
do
when
out
ref
try

Soft:
where 
by 
get, set
import
final
abstract
enum
open
annotation
override
private
public
internal
protected
catch
finally
*/

```