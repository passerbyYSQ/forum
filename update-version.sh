#!/bin/bash
set -e

#all_module_paths=$(find . -type f -name "pom.xml" -print0 | xargs -0 -I {} dirname {})
#echo "all_module_paths: $all_module_paths"

changed_module_paths=$(git diff --name-only HEAD^ HEAD | grep 'src/' | awk -F'src/' '{print "./"$1""}' | sort -u)
#echo "changed_module_paths: $changed_module_paths"

if [ -z "$changed_module_paths" ]; then
    echo "No changed module paths found."
    exit 0
fi

#update_pom_modules=()
#for current_module_path in $all_module_paths; do
#    for changed_module_path in $changed_module_paths; do
#      if [[ "$changed_module_path" == "$current_module_path"* ]]; then
#        #echo "$current_module_path/pom.xml"
#        update_pom_modules+=("$current_module_path")
#        break
#      fi
#    done
#done

project_path=$(readlink -f ".")
echo "$project_path"
#description=""
#for module_path in "${update_pom_modules[@]}"; do
for module_path in $changed_module_paths; do
	cd "$module_path"
	artifact_id=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
  version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
  new_version=$(echo "$version" | awk -F'[.]' '{printf "%d.%d.%d", $1, $2, $3+1}')
  echo "path=$module_path, artifact_id=${artifact_id}, version=$version, new_version=$new_version"
#  description+="${artifact_id}-$new_version;"
  mvn versions:set -q -DnewVersion="$new_version" -DartifactId="$artifact_id" -DgenerateBackupPoms=false -DupdateMatchingVersions=false -DprocessDependencies=false
  cd "$project_path"
done

echo "Auto-increment version: 222"
echo "2222"

old_name=$(git config --local user.name)
echo "old_name: ${old_name}"
old_email=$(git config --local user.email)
echo "old_email: ${old_email}"

git config --local user.name "GitHub Actions Robot"
echo "new_name:"
git config --local user.name

git config --local user.email "1127664027@qq.com"
echo "new_email:"
git config --local user.email

echo "git status before: "
git status
git add "**pom.xml"
echo "git status after: "
git status

git commit -m "Auto-increment version: 222"
echo "commit success"
git push
echo "push success"
git config --local user.name "$old_name"
git config --local user.email "$old_email"
echo "reset success"
