#!/bin/bash

# This is a script which builds and deploys the service RPM to the artifact repository.
# Generally, this should be done by your choice of CI tool.

ARTIFACT_NAME=aws-demo-service
GIT_REPO=https://github.com/lupinsky/aws-demo.git
LIGHT_BLUE='\033[0;34m'
RED='\033[0;31m'
NO_COLOR='\033[0m'

info() {
    echo -e "${LIGHT_BLUE}$1${NO_COLOR}"
}

error() {
    echo -e "${RED}$1${NO_COLOR}"
}

info "Removing existing project build"
rm -rf aws-demo

info "Cloning git repository"
git clone ${GIT_REPO}

if [ $? -ne 0 ]
then
	error "Error cloning repository; exiting."
	exit -1
fi

cd aws-demo

info "Extracting artifact version"
version=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\[|Download)')

info "Building service JAR"
mvn package

info "Preparing directory structure for packaging"
mkdir -p dist/opt/${ARTIFACT_NAME}/bin
cp service/target/${ARTIFACT_NAME}.jar dist/opt/${ARTIFACT_NAME}/bin

info "Packaging artifact"
cd dist
fpm --url "https://github.com/lupinsky/aws-demo" \
	--maintainer "Idan Lupinsky <lupinsky@gmail.com>" \
	--vendor "Idan Lupinsky" \
	--license "MIT License" \
	--description "AWS demo service deployment" \
	--version ${version} \
	-s dir \
	-t rpm \
	-n ${ARTIFACT_NAME} \
	--rpm-user awsdemo \
	--rpm-group awsdemo \
	opt/

if [ $? -ne 0 ]
then
	error "Error packaging artifact; exiting."
	exit -1
fi

cd ..

info "Preparing repository directories"
mkdir -p repo/x86_64

info "Copying artifact to repository"
cp dist/*.rpm repo/x86_64

info "Creating repository"
cd repo
createrepo x86_64

info "Syncing repository to S3"
s3cmd sync . s3://repo-awsdemo/
