import ReleaseType.*
import java.util.*

/**
 * Tells us what type of the dependency is being released
 * The level is the priority and the highest level is always preferred
 * We try to avoid low level releases by all means
 */
enum class ReleaseType(private val level: Int) {
    SNAPSHOT(0),
    DEV(1),
    ALPHA(10),
    BETA(20),
    RC(60),
    RELEASE(100);
}

/**
 * Checks if the current release is stable enough
 * to be integrated into the project
 */
object DependencyUpdates {
    private val stableKeywords = arrayOf("RELEASE", "FINAL", "GA")
    private val releaseRegex = "^[0-9,.v-]+(-r)?$".toRegex(RegexOption.IGNORE_CASE)
    private val rcRegex = releaseKeywordRegex("rc")
    private val betaRegex = releaseKeywordRegex("beta")
    private val alphaRegex = releaseKeywordRegex("alpha")
    private val devRegex = releaseKeywordRegex("dev")

    /**
     * Determines the release type of the given version
     */
    fun versionToRelease(version: String): ReleaseType {
        val uppercase = version.toUpperCase(Locale.ROOT)
        val stableKeyword = stableKeywords.any { uppercase.contains(it) }
        if (stableKeyword) return RELEASE

        return when {
            releaseRegex.matches(version) -> RELEASE
            rcRegex.matches(version) -> RC
            betaRegex.matches(version) -> BETA
            alphaRegex.matches(version) -> ALPHA
            devRegex.matches(version) -> DEV
            else -> SNAPSHOT
        }
    }

    private fun releaseKeywordRegex(keyword: String): Regex {
        return "^[0-9,.v-]+(-$keyword[0-9]*)$".toRegex(RegexOption.IGNORE_CASE)
    }
}