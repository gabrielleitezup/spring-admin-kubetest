package com.example.zup.admintest;

import com.ecwid.consul.v1.ConsulClient;
import de.codecentric.boot.admin.discovery.DefaultServiceInstanceConverter;
import de.codecentric.boot.admin.discovery.ServiceInstanceConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

public class RealwaveServiceInstanceConverter extends DefaultServiceInstanceConverter implements ServiceInstanceConverter {

    private ConsulClient consulClient;

    public RealwaveServiceInstanceConverter(ConsulClient consulClient) {
        this.consulClient = consulClient;
    }

    @Override
    protected URI getHealthUrl(ServiceInstance instance) {
        return super.getHealthUrl(instance);
    }

    @Override
    protected URI getManagementUrl(ServiceInstance instance) {
        String value = consulClient.getKVValue("config/"+instance.getServiceId()+"/data").getValue().getDecodedValue();
        Properties properties = new Properties();

        try {
            properties.load(new StringReader(value));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return UriComponentsBuilder.fromUri(instance.getUri()).path(properties.getProperty("server.contextPath") + getApplicationManage()).build().toUri();
    }

    private String getApplicationManage() {
        String value = consulClient.getKVValue("config/application/data").getValue().getDecodedValue();
        Properties properties = new Properties();

        try {
            properties.load(new StringReader(value));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties.getProperty("management.context-path");
    }

    @Override
    protected URI getServiceUrl(ServiceInstance instance) {
        String value = consulClient.getKVValue("config/"+instance.getServiceId()+"/data").getValue().getDecodedValue();
        if (instance.getServiceId() == null) {
            return UriComponentsBuilder.fromUri(instance.getUri()).path("/").build().toUri();
        }

        Properties properties = new Properties();
        try {
            properties.load(new StringReader(value));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return UriComponentsBuilder.fromUri(instance.getUri()).path(properties.getProperty("server.contextPath")).build().toUri();
    }

    @Override
    protected Map<String, String> getMetadata(ServiceInstance instance) {
        return super.getMetadata(instance);
    }
}
