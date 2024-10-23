runApp:
	javac Backend.java Frontend.java 
	javac -cp .:../junit5.jar App.java
	java App

runTests:
	javac -cp .:../junit5.jar BackendTests.java FrontendTests.java TextUITester.java
	java -jar ../junit5.jar -cp . -c FrontendTests
	java -jar ../junit5.jar -cp . -c BackendTests
	java -jar ../junit5.jar -cp . -c TextUITester

clean:
	rm -f *.class



