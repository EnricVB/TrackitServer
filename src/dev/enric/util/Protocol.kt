package dev.enric.util

class Protocol {

    companion object {
        fun validateRequest(request: String): MatchResult?  {
            val regex = """trackit://(.+):(.+)@(.+):(.+)/(.*)""".toRegex()
            return regex.matchEntire(request)
        }
    }
}