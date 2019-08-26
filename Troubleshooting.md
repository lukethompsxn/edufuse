# Troubleshooting

### Incorrect JDK Version
```
Error:java: invalid source release: 11
```
#### Solution
Download and install JDK 11 found [here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html).
Once you've done this you'll need to set the correct JDK within your IDE (example shown is IntelliJ.
1. File > Project Structure > Project Settings > Project. Set your project JDK to point to JDK 11 by selecting new > /usr/lib/jvm/jdk-11.x.x
![image](https://user-images.githubusercontent.com/31730723/63681195-668a0580-c849-11e9-82a9-2ce0ef473281.png)
2. File > Project Structure > Project Settings > Modules. Ensure Language level is set to 11.
![image](https://user-images.githubusercontent.com/31730723/63681254-87525b00-c849-11e9-945d-92e487d4e9ec.png)
3. File > Settings > Build, Execution, Deployment > Compiler > Java Compiler. Check Target bytecode version is 11.
![image](https://user-images.githubusercontent.com/31730723/63681315-a94bdd80-c849-11e9-9e50-b12169280a6e.png)
