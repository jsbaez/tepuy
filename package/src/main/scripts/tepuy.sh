#!/bin/bash

DIR="$(cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
USER_DIR=$DIR/..
CLASSPATH=$DIR/../libs/*
MODULES_DIR=$DIR/../modules/
CONF_DIR=$DIR/../config/
LOG_FILE=$DIR/../config/log.properties
MAX_MEMORY=100M
MIN_MEMORY=50M

#
# &> /dev/null
#
java -Xms$MIN_MEMORY -Xmx$MAX_MEMORY -Duser.dir="$USER_DIR"\
 -Djava.util.logging.config.file="$LOG_FILE" -Dtepuy.config.dir="$CONF_DIR"\
 -Dtepuy.modules.dir="$MODULES_DIR"\
 -cp "$CLASSPATH" net.jbaez.tepuy.server.Main $@
