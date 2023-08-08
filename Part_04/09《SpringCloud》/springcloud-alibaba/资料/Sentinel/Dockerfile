FROM amd64/buildpack-deps:buster-curl as installer
#设置使用的sentinel的版本号为1.8.6,可以从https://github.com/alibaba/Sentinel/releases查看
ARG SENTINEL_VERSION=1.8.6
#下载运行的jar包到/home下,并起名叫sentinel-dashboard.jar 
RUN set -x \
    && curl -SL --output /home/sentinel-dashboard.jar https://github.com/alibaba/Sentinel/releases/download/${SENTINEL_VERSION}/sentinel-dashboard-${SENTINEL_VERSION}.jar
#创建启动容器,上面的内容主要是为了下载jar包,如果网络有问题可以提前自己下载好,这样上面的内容可以删除,只保留下面的内容
FROM openjdk:8-jre-slim

# copy sentinel jar,如果上面的下载位置和文件名没变,则可以不变,如果是自己下载的文件,需要根据实际情况修改前半部分的文件地址和名字
COPY --from=installer ["/home/sentinel-dashboard.jar", "/home/sentinel-dashboard.jar"]

ENV JAVA_OPTS '-Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080'

RUN chmod -R +x /home/sentinel-dashboard.jar

EXPOSE 8080

CMD java ${JAVA_OPTS} -jar /home/sentinel-dashboard.jar