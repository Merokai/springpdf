FROM maven:3-jdk-8 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM tomcat:jdk8
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/springpdf*.war $CATALINA_HOME/webapps/ROOT.war

HEALTHCHECK --interval=1m --timeout=3s CMD wget --quiet --tries=1 --spider http://localhost:8080/api/country || exit 1