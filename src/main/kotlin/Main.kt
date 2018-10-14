import com.google.gson.GsonBuilder
import java.io.File

const val INPUT_FILE = "in.txt"
const val OUTPUT_FILE = "out.txt"

private val gson = GsonBuilder().setPrettyPrinting().create()

fun main(args: Array<String>) {
    val tree = KotlinSyntaxTreeGenerator.generateTree(File(INPUT_FILE).toPath())

    val output = gson.toJson(tree?.toMap())
    File(OUTPUT_FILE).writeText(output)
}