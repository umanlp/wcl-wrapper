all: wcl-2.2.jar mvn

mvn:
	mvn install:install-file -Dfile=lib/jlt-1.0.0.jar -DgroupId=it.uniroma1.lcl -DartifactId=jlt -Dversion=1.0.0 -Dpackaging=jar
	mvn install:install-file -Dfile=lib/stanford-ner-2011-05-18.jar -DgroupId=edu.stanford.nlp -DartifactId=stanford-ner -Dversion=2011-05-18 -Dpackaging=jar
	mvn install:install-file -Dfile=lib/stanford-parser-2011-05-18.jar -DgroupId=edu.stanford.nlp -DartifactId=stanford-parser -Dversion=2011-05-18 -Dpackaging=jar
	mvn install:install-file -Dfile=lib/stanford-postagger-2011-05-18.jar -DgroupId=edu.stanford.nlp -DartifactId=stanford-postagger -Dversion=2011-05-18 -Dpackaging=jar
	mvn install:install-file -Dfile=wcl-2.2.jar -DgroupId=it.uniroma1.lcl -DartifactId=wcl -Dversion=2.2 -Dpackaging=jar

wcl-2.2.jar: WCL-API-2.2.zip
	unzip WCL-API-2.2.zip
	find WCL-API-2.2 -type f -exec chmod -x {} \;
	mv -fv WCL-API-2.2/* .
	rm -rf WCL-API-2.2 README.txt test.sh

WCL-API-2.2.zip:
	curl -sLO http://lcl.uniroma1.it/wcl/WCL-API-2.2.zip
