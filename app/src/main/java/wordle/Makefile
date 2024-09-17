JAVAC = javac
JAVA = java
JFLAGS = -cp ".;C:\Users\wilso\wordle\app\dep\*"
SRC = C:\Users\wilso\wordle\app\src\main\java\wordle\Client.java C:\Users\wilso\wordle\app\src\main\java\wordle\Controller\*.java
CLASS = wordle.Client
JAR_NAME = client.jar
OUT_DIR = C:\Users\wilso\wordle\app\src\main\java\
DEP_DIR = C:\Users\wilso\wordle\app\dep\

all: compile

compile:
	$(JAVAC) $(JFLAGS) -d $(OUT_DIR) $(SRC)

package: compile
	jar cvf $(JAR_NAME) -C $(OUT_DIR) .

run: compile
	java -cp "$(OUT_DIR);$(DEP_DIR)\*" $(CLASS) $(ARGS)

clean:
	del /Q $(OUT_DIR)\*.class $(JAR_NAME)