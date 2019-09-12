package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.MutableSettingsNode;
import com.bunjlabs.fuga.settings.SettingsContainer;
import com.bunjlabs.fuga.settings.SettingsNode;
import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.settings.source.SettingsSource;
import com.bunjlabs.fuga.util.Assert;

import java.util.LinkedList;
import java.util.List;

public class DefaultSettingsContainer implements SettingsContainer {

    private final MutableSettingsNode rootNode;
    private final List<MutableSettingsNode> persistentTrees;

    public DefaultSettingsContainer() {
        this.rootNode = new DefaultSettingsNode();
        this.persistentTrees = new LinkedList<>();
    }


    public DefaultSettingsContainer(MutableSettingsNode settingsTree) {
        Assert.notNull(settingsTree);
        this.rootNode = settingsTree;
        this.persistentTrees = new LinkedList<>();
    }

    @Override
    public void merge(SettingsNode settingsNode) {
        Assert.notNull(settingsNode);
        rootNode.merge(settingsNode);
    }

    @Override
    public void load(SettingsSource settingsSource, Environment environment) {
        Assert.notNull(settingsSource);
        Assert.notNull(environment);

        var settings = settingsSource.getSettings(environment);

        rootNode.setAll(settings);
        updatePersistenTrees();
    }

    @Override
    public void persist(MutableSettingsNode settingTree) {
        Assert.notNull(settingTree);
        settingTree.merge(rootNode);
        persistentTrees.add(settingTree);
    }

    private void updatePersistenTrees() {
        persistentTrees.forEach(tree -> tree.merge(rootNode));
    }
}
