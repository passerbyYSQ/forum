#!/bin/bash

all_module_paths=$(find . -type f -name "pom.xml" | xargs -I {} dirname {})
#echo $all_module_paths

changed_module_paths=$(git diff --name-only HEAD^ HEAD | grep 'src/' | awk -F'/src/' '{print "./"$1""}' | sort -u)
#echo $changed_module_paths
if [ -z "$changed_module_paths" ]; then
    echo "No changed module paths found."
    exit 0

update_pom_modules=()
for current_module_path in $all_module_paths; do
    for changed_module_path in $changed_module_paths; do
		if [[ "$changed_module_path" == "$current_module_path"* ]]; then
			#echo $current_module_path"/pom.xml"
			update_pom_modules+=($current_module_path)
			break
		fi
    done
done

project_path=$(readlink -f ".")
echo $project_path
root_version=""
for module_path in "${update_pom_modules[@]}"; do
	cd $module_path
	artifact_id=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
    version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
    new_version=$(echo $version | awk -F'[.]' '{printf "%d.%d.%d", $1, $2, $3+1}')
    echo "path=$module_path, artifact_id=${artifact_id}, version=$version, new_version=$new_version"
    if [[ "$module_path" == "." ]]; then
		root_version=$new_version
	fi
	# -DprocessDependencies=false
    mvn versions:set -q -DnewVersion=$new_version -DartifactId=$artifact_id -DgenerateBackupPoms=false -DupdateMatchingVersions=false
    cd $project_path
done

echo "Auto-increment version: $root_version"
git status --porcelain | grep 'pom.xml$' | awk '{print $2}' | xargs git add
git commit --author="GitHub Actions Robot <1127664027@qq.com>" -m "Auto-increment version: $root_version"
