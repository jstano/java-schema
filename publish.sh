#!/bin/bash

VERSION="$1"

if [ -z "$VERSION" ]; then
  echo "Error: VERSION is not set. Please provide it as the first argument."
  exit 1
fi

if [ -z "$MAVEN_TOKEN" ]; then
  echo "Error: MAVEN_TOKEN is not set."
  exit 1
fi

function upload() {
  curl --request POST \
    --verbose \
    --header "Authorization: Bearer $MAVEN_TOKEN" \
    --form "bundle=@$1/build/tmp/staging-deploy.zip" \
    "https://central.sonatype.com/api/v1/publisher/upload?name=$1:$2"
}

upload "schema-importer" "$VERSION"
upload "schema-installer" "$VERSION"
upload "schema-installer-liquibase" "$VERSION"
upload "schema-migrations" "$VERSION"
upload "schema-model" "$VERSION"
upload "schema-parser" "$VERSION"
upload "schema-sql-generator" "$VERSION"
