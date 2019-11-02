FROM yoshinorin/docker-sbt:1.3.3

MAINTAINER YoshinoriN

RUN apt update -y \
  && apt upgrade -y \
  && apt install -y git vim \
  && apt autoremove \
  && apt autoclean

WORKDIR /usr/opt

# TODO: download latest from GitHub releases after first release.
RUN git clone https://github.com/YoshinoriN/cahsper.git

WORKDIR /usr/opt/cahsper

RUN rm -rf .git docs docker \
  && rm .gitignore .editorconfig .travis.yml \
  && pwd && ls -la \
  && mv cahsper-backend/* . \
  && rm -rf scripts \
  && sbt compile
