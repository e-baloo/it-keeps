package org.ebaloo.itkeeps.restapp;

import org.glassfish.jersey.server.ResourceConfig;

public interface InterfaceApplicationConfig {
    public void classesAdd(Class<?> c);
    public ResourceConfig getResourceConfig();
}
