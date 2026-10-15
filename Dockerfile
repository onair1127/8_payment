FROM ghcr.io/graalvm/graalvm-community:25 AS builder

COPY . /home/project
WORKDIR /home/project

RUN ./gradlew nativeCompile


FROM ubuntu

RUN apt-get update && apt-get install -y locales && apt-get install -y curl
RUN locale-gen "ko_KR.UTF-8"
ENV LANG=ko_KR.UTF-8
ENV LANGUAGE=ko_KR.UTF-8
ENV LC_ALL=ko_KR.UTF-8

ENV TZ=Asia/Seoul

COPY --from=builder /home/project/build/native/nativeCompile /home/project
ENV PROFILE=proto
ENV TUNING_OPTS=
# -XX:MinRAMPercentage=50.0 -XX:MaxRAMPercentage=200.0

WORKDIR /home/project

ENTRYPOINT ["./food", "${TUNING_OPTS}", "--spring.profiles.active=${PROFILE}"]
