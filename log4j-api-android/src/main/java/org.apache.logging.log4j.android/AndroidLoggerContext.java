/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.android;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.apache.logging.log4j.util.PropertiesUtil;

/**
 * Routes Log4j API to Android logger.
 */
public class AndroidLoggerContext implements LoggerContext {

    protected static final String SYSTEM_PREFIX = "org.apache.logging.log4j.androidlog.";

    private final LoggerRegistry<ExtendedLogger> loggerRegistry = new LoggerRegistry<>();

    private final PropertiesUtil props;

    private final Level defaultLevel;

    public AndroidLoggerContext() {
        props = new PropertiesUtil("log4j2.androidlog.properties");
        final String lvl = props.getStringProperty(SYSTEM_PREFIX + "level");
        defaultLevel = Level.toLevel(lvl, Level.ERROR);
    }

    @Override
    public Object getExternalContext() {
        return null;
    }

    @Override
    public ExtendedLogger getLogger(String name) {
        return getLogger(name, null);
    }

    @Override
    public ExtendedLogger getLogger(String name, MessageFactory messageFactory) {
        final ExtendedLogger extendedLogger = loggerRegistry.getLogger(name, messageFactory);
        if (extendedLogger != null) {
            AbstractLogger.checkMessageFactory(extendedLogger, messageFactory);
            return extendedLogger;
        }
        AndroidLogger logger = new AndroidLogger(name, messageFactory, props, defaultLevel);
        loggerRegistry.putIfAbsent(name, messageFactory, logger);
        return loggerRegistry.getLogger(name, messageFactory);
    }

    @Override
    public boolean hasLogger(String name) {
        return false;
    }

    @Override
    public boolean hasLogger(String name, MessageFactory messageFactory) {
        return false;
    }

    @Override
    public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
        return false;
    }
}
