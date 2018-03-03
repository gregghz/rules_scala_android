
workspace(name = "io_bazel_rules_scala_android")

rules_scala_version = "98dc457356c60b23ccbbdcbf73acfad9e15e8b07" # update this as needed
http_archive(
    name = "io_bazel_rules_scala",
    url = "https://github.com/bazelbuild/rules_scala/archive/%s.zip"%rules_scala_version,
    type = "zip",
    strip_prefix= "rules_scala-%s" % rules_scala_version,
)

load("@io_bazel_rules_scala//scala:scala.bzl", "scala_repositories")
scala_repositories()

register_toolchains("//toolchain:scala_android_toolchain")

load("//scala-android:scala-android.bzl", "scala_android_repositories")
scala_android_repositories()

load("@io_bazel_rules_scala//specs2:specs2_junit.bzl", "specs2_junit_repositories")
specs2_junit_repositories()

android_sdk_repository(
    name = "androidsdk",
    api_level = 27,
    build_tools_version = "27.0.1"
)

maven_jar(
    name = "com_lucidchart_extract_2_11",
    artifact = "com.lucidchart:xtract_2.11:1.3.1",
)

maven_jar(
    name = "org_typelevel_cats_core_2_11",
    artifact = "org.typelevel:cats-core_2.11:1.0.1",
)

maven_jar(
    name = "org_typelevel_cats_kernel_2_11",
    artifact = "org.typelevel:cats-kernel_2.11:1.0.1",
)

maven_jar(
    name = "com_typesafe_play_play_functional_2_11",
    artifact = "com.typesafe.play:play-functional_2.11:2.5.15",
)

maven_jar(
    name = "com_github_scopt_scopt_2_11",
    artifact = "com.github.scopt:scopt_2.11:jar:3.7.0",
    sha1 = "2f4b95257d082feb9e2a353a9a669c766b850931",
)
