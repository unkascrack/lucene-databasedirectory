/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lucene.store.database.config;

/**
 * Sybase Database Config.
 *
 */
public class SybaseConfig extends DatabaseConfig {

    private static final String SYBASE_CONFIG = "sybase.sql";

    public SybaseConfig() {
        super(SYBASE_CONFIG);
    }

    public SybaseConfig(final long threshold) {
        super(SYBASE_CONFIG, threshold);
    }
}
