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
private val ruleNames = KotlinParser.ruleNames.toList()

fun main(args: Array<String>) {
    val input = CharStreams.fromPath(File(INPUT_FILE).toPath())

    val lexer = KotlinLexer(input)
    val parser = KotlinParser(CommonTokenStream(lexer))
    val tree = parser.kotlinFile()

    val output = gson.toJson(tree.toMap())
    File(OUTPUT_FILE).writeText(output)
}


fun ParseTree.toMap(): Map<String, Any>? {
    val map = LinkedHashMap<String, Any>()
    val node = payload

    when (node) {
        is Token -> map[KotlinLexer.VOCABULARY.getSymbolicName(node.type)] = node.text
        is RuleContext -> {
            val ruleName = ruleNames[node.ruleIndex]

            if (childCount == 1) {
                getChild(0).toMap()?.let { map[ruleName] = it }
            } else if (childCount > 1) {
                val childrenList = ArrayList<Map<String, Any>>()
                map[ruleName] = childrenList

                for (i in 0 until childCount)
                    getChild(i).toMap()?.let { childrenList.add(it) }
            }
        }
    }

    return map.takeIf { it.size > 0 }
}