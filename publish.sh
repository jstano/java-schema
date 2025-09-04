#!/bin/bash

function upload() {
  curl --request POST \
    --verbose \
    --header "Authorization: Bearer $MAVEN_TOKEN" \
    --form "bundle=@$1/build/tmp/staging-deploy.zip" \
    "https://central.sonatype.com/api/v1/publisher/upload?name=$1:$2"
}

upload "schema-importer" "0.9.8"
upload "schema-installer" "0.9.8"
upload "schema-installer-liquibase" "0.9.8"
upload "schema-migrations" "0.9.8"
upload "schema-model" "0.9.8"
upload "schema-parser" "0.9.8"
upload "schema-sql-generator" "0.9.8"
