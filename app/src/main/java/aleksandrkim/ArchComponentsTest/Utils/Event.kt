package aleksandrkim.ArchComponentsTest.Utils

/**
 * Created by Aleksandr Kim on 04 Jun, 2018 10:40 PM for ArchComponentsTest
 */
class Event<out T>(private val content: T) {

    private var hasBeenHandled: Boolean = false

    fun getContentIfAvailable(): T? {
        return if (hasBeenHandled)
            null
        else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent() : T = content
}