fun main(args: Array<String>) {
    val n : Number? = 1L
    when(n){
        16 ->  print("Never gonna say goodbye")
        in 1..10 -> print("Never gonna tell a lie and hurt you")
        is Long -> print("Never gonna give you up")
        else -> print("Never gonna let you down")
    }
}