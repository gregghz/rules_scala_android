load("@io_bazel_rules_scala//scala:scala.bzl", "scala_binary")

scala_binary(
    name = "resource-compiler",
    srcs = glob(["src/main/**/*.scala"]),
    visibility = ["//visibility:public"],
    main_class = "com.lucidchart.android.resources.ResourceCompiler",
    deps = [
        "//external:io_bazel_rules_scala/dependency/scala/scala_xml",
        "@com_github_scopt_scopt_2_11//jar",
        "@com_lucidchart_extract_2_11//jar",
        "@com_typesafe_play_play_functional_2_11//jar",
        "@org_typelevel_cats_core_2_11//jar",
        "@org_typelevel_cats_kernel_2_11//jar",
    ]
)
