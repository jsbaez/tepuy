#!/bin/bash

DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
USER_DIR=$DIR/..
CLASSPATH=$DIR/../libs/*
CONF_FILE=$DIR/../config/tepuy-config.properties
LOG_FILE=$DIR/../config/log.properties
MAX_MEMORY=100M
MIN_MEMORY=50M

#
#
#
java -Xms$MIN_MEMORY -Xmx$MAX_MEMORY -Duser.dir="$USER_DIR"\
 -Djava.util.logging.config.file="$LOG_FILE" -Dtepuy.config="$CONF_FILE"\
 -cp $CLASSPATH net.jbaez.tepuy.Main $@ &> /dev/null
