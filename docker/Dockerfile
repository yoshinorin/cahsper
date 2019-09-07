FROM yoshinorin/docker-sbt:1.2.8.2

MAINTAINER YoshinoriN

RUN apt-get update \
  && apt-get install git

WORKDIR /usr/opt

# TODO: download latest from GitHub releases after first release.
RUN git clone https://github.com/YoshinoriN/cahsper.git

WORKDIR /usr/opt/cahsper

RUN rm -rf .git docs \
  && rm .gitignore .editorconfig .travis.yml \
  && pwd && ls -la \
  && mv cahsper-backend/* . \
  && rm -rf scripts \
  && sbt compile
