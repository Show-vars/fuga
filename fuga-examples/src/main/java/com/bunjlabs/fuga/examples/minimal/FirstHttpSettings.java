package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.settings.annotations.Settings;
import com.bunjlabs.fuga.settings.annotations.Value;

@Settings("first")
public interface FirstHttpSettings extends HttpSettings {
    @Value("default")
    String name();
}
