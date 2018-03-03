load("@io_bazel_rules_scala//scala:scala_toolchain.bzl", "scala_toolchain")

scala_toolchain(
    name = "scala_android_toolchain_impl",
    scalacopts = [
        "-deprecation",
        "-feature",
        "-Xfatal-warnings",
        "-Xlint",
        "-Yno-adapted-args",
        # "-Ywarn-dead-code",
        "-Ywarn-numeric-widen",
        # "-Ywarn-value-discard",
        "-Xfuture",
        # "-Ywarn-unused-import",
        "-Ypartial-unification",
    ],
    visibility = ["//visibility:public"],
)

toolchain(
    name = "scala_android_toolchain",
    toolchain_type = "@io_bazel_rules_scala//scala:toolchain_type",
    toolchain = "scala_android_toolchain_impl",
    visibility = ["//visibility:public"],
)
