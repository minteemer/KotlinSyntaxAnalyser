import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.jupiter.api.*
import java.io.File

class TreeGeneratorTests {
    companion object {
        private val classLoader = TreeGeneratorTests::class.java.classLoader

        /** Map of test files to expected result files */
        private lateinit var testFiles: Map<File, File>

        /** Folder with input .kt files */
        private val inputFilesFolder = classLoader.getResource("in").file

        /** Folder with expected output .json files */
        private val outputFilesFolder = classLoader.getResource("out").file

        /**
         * Sets up [testFiles] map where each file from [inputFilesFolder] mapped on
         * files in [outputFilesFolder] with the same name, but .json extension.
         */
        @BeforeAll
        @JvmStatic
        fun setUpData() {
            testFiles = File(inputFilesFolder).listFiles().filter { it.isFile }.associate { input ->
                input to File("$outputFilesFolder/${input.nameWithoutExtension}.json")
            }
        }
    }

    /**
     * Asserts that syntax trees generated form inputs in [testFiles] is the same
     * as the trees in output files
     */
    @TestFactory
    fun testTreeGeneration() = testFiles
            .map { (input, output) ->
                DynamicTest.dynamicTest(input.nameWithoutExtension) {
                    Assertions.assertEquals(
                            KotlinSyntaxTreeGenerator.generateTree(input.toPath()),
                            output.readText().jsonToMap()
                    )
                }
            }


    private val gson = Gson()
    private val typeToken = object : TypeToken<Map<String, Any>>() {}.type

    /** @return [HashMap] created from JSON in this string */
    private fun String.jsonToMap(): Map<String, Any> = gson.fromJson(this, typeToken)
}