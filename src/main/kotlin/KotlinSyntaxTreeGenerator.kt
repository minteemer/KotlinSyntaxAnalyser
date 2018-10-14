import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.ParseTree
import java.nio.file.Path
import java.util.LinkedHashMap

object KotlinSyntaxTreeGenerator {

    fun generateTree(filePath: Path): Map<String, Any>? {
        val lexer = KotlinLexer(CharStreams.fromPath(filePath))
        val parser = KotlinParser(CommonTokenStream(lexer))

        return parser.kotlinFile().toMap()
    }


    private fun ParseTree.toMap(): Map<String, Any>? {
        val map = LinkedHashMap<String, Any>()

        val node = payload
        when (node) {
            is Token -> map[node.getName()] = node.text
            is RuleContext -> (0 until childCount)
                    .mapNotNull { getChild(it).toMap()?.takeIf { it.isNotEmpty() } }
                    .let { map[node.getName()] = if (it.size == 1) it[0] else it }
        }

        return map
    }

    private fun Token.getName() = KotlinLexer.VOCABULARY.getSymbolicName(type)

    private fun RuleContext.getName() = KotlinParser.ruleNames[ruleIndex]
}