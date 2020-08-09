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

COPY entry-point.sh entry-point.sh

RUN rm -rf .git docs \
  && rm .gitignore .editorconfig \
  && mv cahsper-backend/* . \
  && rm -rf scripts \
  && rm -rf cahsper-backend docker \
  && sbt assembly exit \
  && cp ./target/scala-2.13/cahsper-assembly-0.0.1.jar cahsper-assembly.jar

ENTRYPOINT [ "/usr/opt/cahsper/entry-point.sh" ]
