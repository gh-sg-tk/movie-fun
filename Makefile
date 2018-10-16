start:
	@mvn spring-boot:run

build:
	@mvn clean package -DskipTests -Dmaven.test.skip=true

test:
	@mvn clean package

deploy-to-pcf:
	@cf push moviefun -p target/moviefun.war --random-route

deploy: build deploy-to-pcf
