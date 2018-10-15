build-local:
	@mvn clean package -DskipTests -Dmaven.test.skip=true && \
	cp target/moviefun.war ~/dev/tomee/webapps && \
	~/dev/tomee/bin/catalina.sh run
