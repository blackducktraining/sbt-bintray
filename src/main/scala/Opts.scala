package bintray

import sbt.{ MavenRepository, RawRepository }
import bintry.Client

object Opts {
  object resolver {
    val jcenter = MavenRepository("BintrayJCenter", "https://jcenter.bintray.com")

    def publishTo(repo: Client#Repo, pkg: Client#Repo#Package, version: String, mvnStyle: Boolean = true, isSbtPlugin: Boolean = false) =
      if (mvnStyle) new RawRepository(
        BintrayMavenResolver(s"Bintray-Maven-Publish-${repo.subject}-${repo.repo}-${pkg.name}",
                             s"https://api.bintray.com/maven/${repo.subject}/${repo.repo}/${repo.repo}", pkg))
      else new RawRepository(
        BintrayIvyResolver(s"Bintray-${if (isSbtPlugin) "Sbt" else "Ivy"}-Publish-${repo.subject}-${repo.repo}-${pkg.name}",
                           pkg.version(version),
                           sbt.Resolver.ivyStylePatterns.artifactPatterns))

    def mavenRepo(name: String) = repo(name, "maven")

    // newer versions of sbt have an alternative built in.
    def repo(name: String, repo: String) =
      MavenRepository(
        s"Bintray-Resolve-$name-$repo",
        s"https://dl.bintray.com/content/$name/$repo")
  }
}