import kotlin.reflect.*

sealed interface Message {
    data class TextMessage(val text: String) : Message
    object End : Message
}

fun nameProvider(initializer: () -> String): Lazy<String> {
    return NameProviderLazy(initializer)
}

class NameProviderLazy @JvmOverloads constructor(
    private val initializer: () -> String = { "UNDEFINED" }
) : Lazy<String> {
    private var cached: String? = null
    
    override val value: String
    	get() {
            val name = cached
            return if (name == null) {
                println("Initializing name..")
                initializer().also { cached = it }
            } else {
                name
            }
        }
        
    override fun isInitialized(): Boolean = cached != null
}

class CaptureNonNull<T> {
    private var capturedValue: T? = null
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return capturedValue
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (value == null) {
            println("Ignoring null value.")
        } else {
            capturedValue = value
        }
    }
}

fun <T> neverNull(): CaptureNonNull<T> {
    return CaptureNonNull<T>()
}

interface LoadState {
    val isLoading: Boolean
    fun update(loading: Boolean)
}

class DefaultLoadState(
    val initialState: Boolean = false
) : LoadState {
    private var state: Boolean = initialState
    
    override val isLoading: Boolean 
    	get() = state
    
    override fun update(loading: Boolean) {
        this.state = loading
    }
}

interface ErrorHandler {
    fun onError(message: String)
}

class ScreamingErrorHandler : ErrorHandler {
    override fun onError(message: String) {
        println("AAHHH! An error happened: $message")
    }
}

class UserViewModel() : LoadState by DefaultLoadState(), ErrorHandler by ScreamingErrorHandler()

fun main() {
	val name: String by nameProvider { "John Doe" }
    
    println("Start")	
    println(name)
    
    var happyNotNull: String? by neverNull()
    happyNotNull = "Happy happy happy!!"
    println(happyNotNull)
    println("Trying to assign null to happyNotNull")
    happyNotNull = null
    println(happyNotNull)
    
    val userViewModel = UserViewModel()
    println(userViewModel.isLoading)
    userViewModel.update(true)
    println(userViewModel.isLoading)
    println(userViewModel.onError("User is unavailable."))
}