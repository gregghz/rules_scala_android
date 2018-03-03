load("@io_bazel_rules_scala//scala:scala.bzl", "scala_specs2_junit_test")
load("//scala-android:scala-android.bzl", "scala_android_library", "typed_resources")

scala_android_library(
    name = "resource-gen-lib",
    custom_package = "com.lucidchart.android.test",
    manifest = "AndroidManifest.xml",
    resource_files = glob(["res/**"]),
    deps = [
        "@androidsdk//com.android.support.constraint:constraint-layout-1.0.2",
    ],
)

typed_resources(
    name = "resource-gen-out",
    custom_package = "com.lucidchart.android.test",
    resource_files = glob(["res/**"]),
)

scala_specs2_junit_test(
    name = "resource-gen-test",
    deps = [
        "//external:io_bazel_rules_scala/dependency/android_sdk",
        "//typed-resources:typed-resources",
        ":resource-gen-out",
        "@androidsdk//com.android.support.constraint:constraint-layout-1.0.2",
    ],
    srcs = [
        ":resource-gen-out",
        ":resource-gen-lib",
        "ResourceCompilerTest.scala",
    ],
    suffixes = ["Test"],
)
