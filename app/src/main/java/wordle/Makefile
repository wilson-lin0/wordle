JAVAC = javac
JAVA = java
JFLAGS = -g

SOURCES = Client.java
CLASSES = $(SOURCES:.java=.class)

all: $(CLASSES)

%.class: %.java
	$(JAVAC) $(JFLAGS) $<


run: all
	$(JAVA) Client $(ARGS)

clean:
	rm -f *.class