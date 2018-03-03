load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library")

gendir_base_path = "main/gen/res"

def _sanitize_string_for_usage(s):
    res_array = []
    for c in s:
        if c.isalnum() or c == ".":
            res_array.append(c)
        else:
            res_array.append("_")
    return "".join(res_array)

def _typed_resources_impl(ctx):
    gendir_path = gendir_base_path + "/" + _sanitize_string_for_usage(ctx.attr.name)
    gendir = ctx.actions.declare_directory(gendir_path)

    print(ctx.attr.resource_files)

    res_files = ctx.attr.resource_files
    tr_file = ctx.actions.declare_file(gendir_path + "/" + "TR.scala")

    options = [
        "-o", tr_file.path,
        "-p", ctx.attr.custom_package,
    ]

    input_files = [f for res_file in res_files for f in res_file.files]
    args = options + [f.short_path for f in input_files]

    ctx.actions.run(
        inputs = input_files,
        outputs = [gendir, tr_file],
        arguments = args,
        progress_message = "Processing resources",
        executable = ctx.executable._resource_compiler,
    )

    ctx.actions.run(
        inputs = [tr_file],
        outputs = [ctx.outputs.srcjar],
        arguments = ["cfM", ctx.outputs.srcjar.path, tr_file.path],
        progress_message = "Bundling generated resoures into srcjar",
        executable = "jar",
    )

typed_resources = rule(
    implementation = _typed_resources_impl,
    attrs = {
        "resource_files": attr.label_list(mandatory = True, allow_files = True),
        "custom_package": attr.string(mandatory = True),
        "_resource_compiler": attr.label(
            executable = True,
            allow_files = True,
            cfg = "host",
            default = Label("//resource-compiler")
        )
    },
    outputs = {
        "srcjar": "scala_resources_%{name}.srcjar",
    },
)

def scala_android_repositories():
    native.bind(name = "io_bazel_rules_scala/dependency/android_sdk", actual = "//tools/defaults:android_jar")

def scala_android_library(
        name,
        srcs = [],
        deps = [],
        resource_files = [],
        custom_package = None,
        manifest = None,
        proguard_specs = [],
        visibility = None,
        plugins = [],
        generate_typed_resources = False,
        **kwargs):

    res_deps = []
    if len(resource_files) > 0:
        if manifest == None:
            fail("manifest is required when resource_files are present", "manifest")

        native.android_library(
            name = name + "_res",
            custom_package = custom_package,
            manifest = manifest,
            resource_files = resource_files,
            deps = deps,
            **kwargs
        )

        if generate_typed_resources:
            typed_resources(
                name = name + "_typed_res",
                deps = [name + "_res"],
            )

        res_deps.append(name + "_res")

    native.android_library(
        name = name + "_aar",
        neverlink = 1,
    )

    scala_lib_deps = []
    if generate_typed_resources and len(resource_files) > 0:
        scala_lib_deps = deps + [name + "_typed_res"]
    else:
        scala_lib_deps = deps

    scala_library(
        name = name + "_compile",
        srcs = srcs,
        plugins = plugins,
        deps = scala_lib_deps + ["//external:io_bazel_rules_scala/dependency/android_sdk", name + "_aar"] + res_deps,
        **kwargs
    )

    native.java_import(
        name = name + "_scala",
        jars = [name + "_compile.jar"],
        deps = deps,
        visibility = visibility,
        exports = [
            "//external:io_bazel_rules_scala/dependency/scala/scala_library"
        ],
    )

    native.android_library(
        name = name,
        exports = res_deps + [
            name + "_scala",
        ],
        proguard_specs = proguard_specs,
        visibility = visibility,
        custom_package = custom_package,
        manifest = manifest,
        **kwargs
    )
