package org.testcontainers.containers;

import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.traits.LinkableContainer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

/**
 * @author richardnorth
 */
public class NginxContainer<SELF extends NginxContainer<SELF>> extends GenericContainer<SELF> implements LinkableContainer {

    private static final int NGINX_DEFAULT_PORT = 80;

    public NginxContainer() {
        this("nginx:1.9.4");
    }

    public NginxContainer(String dockerImageName) {
        super(dockerImageName);

        addExposedPort(NGINX_DEFAULT_PORT);
        setCommand("nginx", "-g", "daemon off;");
    }

    @NotNull
    @Override
    protected Set<Integer> getLivenessCheckPorts() {
        return Collections.singleton(getMappedPort(80));
    }

    public URL getBaseUrl(String scheme, int port) throws MalformedURLException {
        return new URL(scheme + "://" + getHost() + ":" + getMappedPort(port));
    }

    public void setCustomContent(String htmlContentPath) {
        addFileSystemBind(htmlContentPath, "/usr/share/nginx/html", BindMode.READ_ONLY);
    }

    public SELF withCustomContent(String htmlContentPath) {
        this.setCustomContent(htmlContentPath);
        return self();
    }
}
