# stellaris tech tree parser
[![Java CI with Maven](https://github.com/azrdev/stellaris-technology-parser/actions/workflows/maven-build-jar.yml/badge.svg)](https://github.com/azrdev/stellaris-technology-parser/actions/workflows/maven-build-jar.yml)

Usage:
1. install a JDK and maven
2. build the project using `mvn package` resulting in `target/stellaris-1.0.0-SNAPSHOT.jar`, or use the .jar built by GitHub Actions
3. open a shell, change to a directory in which `files/` contains your Stellaris installation (e.g. `ln -Ts files ~/.steam/steam/steamapps/common/Stellaris`)
4. Run `java -jar /path/to/your/target/stellaris-1.0.0-SNAPSHOT.jar`
5. Collect the generated `*.json` files and `index.html` and publish to a webserver, e.g. add to https://github.com/turanar/stellaris-tech-tree/
