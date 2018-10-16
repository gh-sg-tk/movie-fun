start:
	@mvn spring-boot:run

build:
	@mvn clean package -DskipTests -Dmaven.test.skip=true

test:
	@mvn clean package

deploy:
	@cf push moviefun -p target/moviefun.war --random-route
