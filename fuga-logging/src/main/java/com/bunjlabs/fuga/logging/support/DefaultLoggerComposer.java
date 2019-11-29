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

package com.bunjlabs.fuga.logging.support;

import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.ProvisionException;
import com.bunjlabs.fuga.logging.LoggerComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLoggerComposer implements LoggerComposer {
    @Override
    public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
        if (!Logger.class.equals(requested.getType())) {
            throw new ProvisionException("This composer can create org.slf4j.Logger only");
        }

        return doGet(requester.getType(), requested.getType());
    }

    @SuppressWarnings("unchecked")
    private <T> T doGet(Class<?> requester, Class<T> cast) {
        return (T) LoggerFactory.getLogger(requester);
    }
}
