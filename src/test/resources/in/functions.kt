fun sum(a: Int, b: Int): Int {
    return a + b
}

/** Applies [func] to this object and [b] */
fun <A, B, R> A.applyFunc(b: B, func: (A, B) -> R): R = func(this, b)

fun main(args: Array<String>) {
    val s = sum(1, 2)

    s.applyFunc(3) { a, b -> a - b }
}