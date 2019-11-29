/*
 * Copyright 2019 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bunjlabs.fuga.logging;

import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.UnitBuilder;
import com.bunjlabs.fuga.logging.support.DefaultLoggerComposer;
import org.slf4j.Logger;

public class LoggingUnitBuilder implements UnitBuilder {
    public LoggingUnitBuilder() {
    }

    @Override
    public Unit build() {
        return c -> {
            var composer = new DefaultLoggerComposer();

            c.bind(LoggerComposer.class).toInstance(composer);
            c.bind(Logger.class).toComposer(composer);
        };
    }
}
