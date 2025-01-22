#!/bin/bash

source "$HOME/.sdkman/bin/sdkman-init.sh"

VERSIONS=(
  "1.20.6 21.0.5-tem"
  "1.21.1 21.0.5-tem" "1.21.4 21.0.5-tem"
)
WORK_DIR=$(mktemp -d -t "WLib-setup-spigot-versions-XXX")
BUILD_TOOLS_URL="https://hub.spigotmc.org/jenkins/job/BuildTools/lastBuild/artifact/target/BuildTools.jar"
BUILD_TOOLS_FILE="BuildTools.jar"

download_build_tools() {
  curl -O "$BUILD_TOOLS_URL"
}

run_build_tools() {
  version="$1"
  jdk_version="$2"

  mkdir "$version" && cd "$version"
  cp "$WORK_DIR/$BUILD_TOOLS_FILE" .

  sdk install java "$jdk_version"
  sdk use java "$jdk_version"
  java -jar "$BUILD_TOOLS_FILE" --rev "$version"
}

execute_all_versions() {
  for version_data in "${VERSIONS[@]}"; do
    read -r version jdk <<< "$version_data"
    echo "$version $jdk"

    cd "$WORK_DIR"
    run_build_tools "$version" "$jdk"
  done
}

run() {
  cd "$WORK_DIR"
  download_build_tools
  execute_all_versions
}

run || true
rm "$WORK_DIR" -rf