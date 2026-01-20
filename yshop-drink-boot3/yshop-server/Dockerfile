## AdoptOpenJDK 停止发布 OpenJDK 二进制，而 Eclipse Temurin 是它的延伸，提供更好的稳定性
## 感谢复旦核博士的建议！灰子哥，牛皮！
FROM eclipse-temurin:21-jre

## 创建目录，并使用它作为工作目录
RUN mkdir -p /yshop-server/logs
WORKDIR /yshop-server

## 将后端项目的 Jar 文件，复制到镜像中
COPY ./target/yshop-server.jar app.jar

## 设置 TZ 时区
ENV TZ=Asia/Shanghai

## 设置 JAVA_OPTS 环境变量，可通过 docker run -e "JAVA_OPTS=" 进行覆盖
## 优化：增加内存配置和GC参数
ENV JAVA_OPTS="-Xms1g -Xmx1g \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/yshop-server/logs/heapdump.hprof \
    -Djava.security.egd=file:/dev/./urandom"

## 应用参数
ENV ARGS=""

## 暴露后端项目的 48080 端口
EXPOSE 48080

## 健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:48080/actuator/health || exit 1

## 启动后端项目
CMD java ${JAVA_OPTS} -jar app.jar $ARGS
