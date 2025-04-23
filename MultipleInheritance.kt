interface FirstInterface {
    
    fun printName(s: String) {
        println("[${this.javaClass.canonicalName}]: $s")
        // println("[FirstInterface]: $s")
    }
}

interface SecondInterface {
    
    fun printName(s: String) {
        println("[${this.javaClass.canonicalName}]: $s")
        // println("[SecondInterface]: $s")
    }
}

class SomeClass : FirstInterface, SecondInterface {
    
    override fun printName(s: String) {
        super<FirstInterface>.printName(s)
        super<SecondInterface>.printName(s)
        println("[${this.javaClass.canonicalName}]: $s")
    }
}

fun main() {
    val name = "Hello!"
    val someObj = SomeClass()
    someObj.printName(name)
    
    (someObj as FirstInterface).printName(name)
    (someObj as SecondInterface).printName(name)
}