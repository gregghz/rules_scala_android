#!/usr/bin/env bash

set -e

bazel test --test_verbose_timeout_warnings //test:resource-gen-test
