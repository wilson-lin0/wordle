SRC_DIR = ./src/main/java/wordle
BIN_DIR = bin
CP = $(SRC_DIR)/*

all: compile

compile:
	@if [ ! -d $(BIN_DIR) ]; then mkdir $(BIN_DIR); fi
	javac -cp "$(CP)" -d $(BIN_DIR) $(SRC_DIR)/*.java

clean:
	@if [ -d $(BIN_DIR) ]; then rm -rf $(BIN_DIR); fi