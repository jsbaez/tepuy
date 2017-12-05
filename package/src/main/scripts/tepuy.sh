#!/bin/bash

DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
USER_DIR=$DIR/..
CLASSPATH=$DIR/../libs/*
DIR_MODULES=$DIR/../modules/
CONF_FILE=$DIR/../config/tepuy-config.properties
LOG_FILE=$DIR/../config/log.properties
MAX_MEMORY=100M
MIN_MEMORY=50M

#
# &> /dev/null
#
java -Xms$MIN_MEMORY -Xmx$MAX_MEMORY -Duser.dir="$USER_DIR"\
 -Djava.util.logging.config.file="$LOG_FILE" -Dtepuy.config.file="$CONF_FILE"\
 -Dtepuy.modules.dir="$DIR_MODULES"\
 -cp "$CLASSPATH" net.jbaez.tepuy.server.Main $@
