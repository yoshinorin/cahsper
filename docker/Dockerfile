FROM yoshinorin/docker-sbt:1.3.13

MAINTAINER YoshinoriN

RUN apt-get update -y \
  && apt-get upgrade -y \
  && apt-get install -y git vim \
  && apt-get autoremove \
  && apt-get clean

WORKDIR /usr/opt

# TODO: download latest from GitHub releases after first release.
RUN git clone https://github.com/YoshinoriN/cahsper.git

WORKDIR /usr/opt/cahsper

RUN rm -rf .git docs \
  && rm .gitignore .editorconfig .travis.yml \
  && pwd && ls -la \
  && mv cahsper-backend/* . \
  && rm -rf scripts \
  && rm -rf cahsper-backend docker \
  && sbt compile
