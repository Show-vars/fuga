package com.bunjlabs.fuga.examples.remoteexportimporttest.units;

import com.bunjlabs.fuga.examples.remoteexportimporttest.services.TestExportedService;
import com.bunjlabs.fuga.inject.Configuration;
import com.bunjlabs.fuga.inject.Inject;
import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.remoting.RemoteExporter;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteExporter;

public class RemoteExportUnit implements Unit {

    private TestExportedService service;

    @Inject
    public RemoteExportUnit(TestExportedService service) {
        this.service = service;
    }

    @Override
    public void setup(Configuration configuration) {
        RemoteExporter exporter = new DefaultRemoteExporter(service, TestExportedService.class);
    }
}
