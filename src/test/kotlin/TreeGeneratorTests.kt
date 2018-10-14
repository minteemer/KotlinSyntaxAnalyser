import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.jupiter.api.*
import java.io.File

class TreeGeneratorTests {
    companion object {
        private val classLoader = TreeGeneratorTests::class.java.classLoader


        private lateinit var testData: Map<File, File>

        @BeforeAll
        @JvmStatic
        fun setUpData(){
            testData = File(classLoader.getResource("in").file).listFiles().toList().associate {
                it to File(classLoader.getResource("out/${it.name.dropLast(3)}.json").file)
            }
        }
    }


    @TestFactory
    fun testSquares() = testData
            .map { (input, output) ->
                DynamicTest.dynamicTest(input.name) {
                    Assertions.assertEquals(
                            KotlinSyntaxTreeGenerator.generateTree(input.toPath()),
                            output.readText().jsonToMap()
                    )
                }
            }

    private val gson = Gson()
    private val typeToken = object: TypeToken<Map<String, Any>>(){}.type

    private fun String.jsonToMap(): Map<String, Any> = gson.fromJson(this, typeToken)
}