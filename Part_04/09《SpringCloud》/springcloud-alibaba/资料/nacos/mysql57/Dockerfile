FROM mysql:5.7.40
#此处前面的sql位置为自己文件的位置, 文件来源https://raw.githubusercontent.com/alibaba/nacos/develop/distribution/conf/mysql-schema.sql
ADD ./env/mysql.sql /docker-entrypoint-initdb.d/nacos-mysql.sql
RUN chown -R mysql:mysql /docker-entrypoint-initdb.d/nacos-mysql.sql
EXPOSE 3306
CMD ["mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci"]