JAVAC := $(shell which javac)
MVN := $(shell which mvn)
JAVAC_REQ_VERSION = javac 1.8
MVN_REQ_VERSION = 3.3.9

SRCS = src/**/*

ifeq ($(JAVAC),)
$(error No javac found!)
endif

ifeq ($(MVN),)
$(error No mvn found! Please install Maven.)
endif

$(info Need Java with ${JAVAC_REQ_VERSION}.)
$(info Tested only on Maven version ${MVN_REQ_VERSION}. Please install that version if build fails.)

all:
	mvn package

compile:
	mvn compile

doc:
	mvn javadoc:javadoc

clean:
	mvn clean

run: compile
	mvn exec:java