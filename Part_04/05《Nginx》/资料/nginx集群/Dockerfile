FROM baseservice.qfjava.cn:60001/nginx:1.21.6

RUN apt-get update && apt-get upgrade  

RUN apt-get install -y  curl ipvsadm iproute2 openrc keepalived 

COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

CMD ["/entrypoint.sh"]
