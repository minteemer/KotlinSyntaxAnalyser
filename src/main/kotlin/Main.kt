import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.*
import java.io.File
import java.util.LinkedHashMap
import java.util.ArrayList

const val INPUT_FILE = "in.txt"
const val OUTPUT_FILE = "out.txt"

private val gson = GsonBuilder().setPrettyPrinting().create()

fun main(args: Array<String>) {
    val input = CharStreams.fromPath(File(INPUT_FILE).toPath())
    val lexer = KotlinLexer(input)
    val parser = KotlinParser(CommonTokenStream(lexer))

    val tree = parser.kotlinFile()

    println(gson.toJson(tree.toMap()))
}


fun ParseTree.toMap(): Map<String, Any> = LinkedHashMap<String, Any>().also { map -> traverse(this, map) }

fun traverse(tree: ParseTree, map: MutableMap<String, Any>) {
    val node = tree.payload
    val ruleNames = KotlinParser.ruleNames.toList()

    when(node){
        is Token -> {
            map["TokenType"] = KotlinLexer.VOCABULARY.getSymbolicName(node.type)
            map["Lexeme"] = node.text
        }
        is RuleContext -> {
            val children = ArrayList<Map<String, Any>>()
            val name = ruleNames[node.ruleIndex]
            map[name] = children

            for (i in 0 until tree.childCount) {
                val nested = LinkedHashMap<String, Any>()
                children.add(nested)
                traverse(tree.getChild(i), nested)
            }
        }
    }
}