import io.gatling.commons.shared.unstable.util.PathHelper.RichPath

import java.net.URI
import java.nio.file.Paths

object IDEPathHelper {
    val gatlingConfUrl: URI = getClass.getClassLoader.getResource("gatling.conf").toURI

    val projectRootDir = Paths.get(URI.create(gatlingConfUrl.toString))

    val mavenSourcesDirectory = projectRootDir / "src" / "test" / "scala"
    val mavenResourcesDirectory = projectRootDir / "src" / "test" / "resources"
    val mavenTargetDirectory = projectRootDir / "target"
    val mavenBinariesDirectory = mavenTargetDirectory / "test-classes"

    val dataDirectory = mavenResourcesDirectory / "data"
    val bodiesDirectory = mavenResourcesDirectory / "bodies"

    val recorderOutputDirectory = mavenSourcesDirectory
    val resultsDirectory = mavenTargetDirectory / "gatling"

    val recorderConfigFile = mavenResourcesDirectory / "recorder.conf"
}
