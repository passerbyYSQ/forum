#!/bin/bash
# 任何命令返回非零状态时立即退出，以便中断CI/CD
set -e

# 获取所有模块相对于项目根目录的路径
#all_module_paths=$(find . -type f -name "pom.xml" -print0 | xargs -0 -I {} dirname {})
#echo "all_module_paths: $all_module_paths"

# 获取源码文件发生改动的模块的相对路径
changed_module_paths=$(git diff --name-only HEAD^ HEAD | grep 'src/' | awk -F'src/' '{print "./"$1""}' | sort -u)
#echo "changed_module_paths: $changed_module_paths"

# 如果没有识别出改动的模块，则正常退出，防止CI/CD失败
if [ -z "$changed_module_paths" ]; then
    echo "No changed module paths found."
    exit 0
fi

# 匹配改动模块的祖先模块。如果只管改动模块本身升版，此操作忽略
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
description=""
#for module_path in "${update_pom_modules[@]}"; do
for module_path in $changed_module_paths; do
  # 去到改动模块的目录。注意这是相对于项目根目录的路径
	cd "$module_path"
	artifact_id=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
  version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
  new_version=$(echo "$version" | awk -F'[.]' '{printf "%d.%d.%d", $1, $2, $3+1}')
  echo "path=$module_path, artifact_id=${artifact_id}, version=$version, new_version=$new_version"
  description+="${artifact_id}-$new_version; "
	# 执行模块升版。约定主分支的版本号形如x.y.z
  mvn versions:set -q -DnewVersion="$new_version" -DartifactId="$artifact_id" -DgenerateBackupPoms=false -DupdateMatchingVersions=false -DprocessDependencies=false
  # 重新回到项目根目录
  cd "$project_path"
done

echo "Auto-increment version: $description"

# 备份旧的用户和邮箱信息，以便在CI/CD之后能够还原
old_name=$(git config --local user.name)
old_email=$(git config --local user.email)
git config --local user.name "GitHub Actions Robot"
git config --local user.email "1127664027@qq.com"
git add "**pom.xml"
git commit -m "Auto-increment version: $description"
git push
# 还原原本的用户和邮箱信息
git config --local user.name "$old_name"
git config --local user.email "$old_email"
