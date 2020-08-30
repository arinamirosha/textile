// Maven project
// Command line interface
// Examples of commads below
// You can change "csv" to "xml"
// Set roots to files in config

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv add 111 RedSocks 50 50 socks 40

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv get 111

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv del 111

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv change 1234 price 300

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv showall

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv showzero

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv showbyname pillow

java -Dlog4j2.configurationFile=log4j2.properties -Dconfig=config.properties -jar textile.jar csv doc 1234 1 sale
