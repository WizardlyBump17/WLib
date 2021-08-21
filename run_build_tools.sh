wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
java -Xmx1G -jar BuildTools.jar --rev 1.8.8
java -Xmx1G -jar BuildTools.jar --rev 1.12.2
java -Xmx1G -jar BuildTools.jar --rev 1.13.2
java -Xmx1G -jar BuildTools.jar --rev 1.15.2
java -Xmx1G -jar BuildTools.jar --rev 1.16.5