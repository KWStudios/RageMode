#!/bin/bash

# Create tmp directory
TMP_NAME=`cat /dev/urandom | tr -cd 'a-f0-9' | head -c 32`
mkdir `echo $TMP_NAME`/
cd `echo $TMP_NAME`/

# Download latest BuildTools.jar
curl https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar --output BuildTools.jar

# Set up git
git config --global --unset core.autocrlf

# Run BuildTools for all needed versions
VERSIONS[0]="1.8.8"
MAVEN_VERSIONS[0]="R0.1-SNAPSHOT"

for (( i=0; i<${#VERSIONS[@]}; i++ ))
do
  if [ -e `echo $HOME/.m2/repository/org/spigotmc/spigot/${VERSIONS[$i]}-${MAVEN_VERSIONS[$i]}` ]
  then
    echo "${VERSIONS[$i]} already exists in the local maven cache. Skipping this build..."
    continue
  else
    echo "${VERSIONS[$i]} does not exist in the local maven cache. Building it now..."
  fi

  # Run BuildTools.jar for the version $i
  java -jar -Xms1G -Xmx3G BuildTools.jar --rev `echo ${VERSIONS[$i]}`

  # Delete all files but BuildTools.jar
  TMP_JAR=`tar -c BuildTools.jar | base64`
  rm -rf ./*
  echo "$TMP_JAR" | base64 --decode | tar -x
done

# Clean up tmp directory
cd ..
rm -rf `echo $TMP_NAME`
