runApp:
	javac Backend.java Frontend.java 
	javac -cp .:../junit5.jar App.java
	java App

runTests:
	javac -cp .:../junit5.jar BackendTests.java FrontendTests.java
	java -jar ../junit5.jar -cp . -c FrontendTests
	java -jar ../junit5.jar -cp . -c BackendTests

clean:
	rm -f *.class



