package org.ebaloo.itkeeps.restapp;

import org.glassfish.jersey.server.ResourceConfig;

public interface InterfaceApplicationConfig {
    void classesAdd(Class<?> c);
    ResourceConfig getResourceConfig();
}
