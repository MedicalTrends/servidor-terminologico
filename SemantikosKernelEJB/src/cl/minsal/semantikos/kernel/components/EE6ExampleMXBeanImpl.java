package cl.minsal.semantikos.kernel.components;

/*
    To the extent possible under law, Red Hat, Inc. has dedicated all copyright to this software to the public domain worldwide, pursuant to the CC0 Public Domain Dedication. This software is distributed without any warranty.  See <http://creativecommons.org/publicdomain/zero/1.0/>.
*/

import java.lang.management.ManagementFactory;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author bmaxwell
 */
@Singleton
@Startup
public class EE6ExampleMXBeanImpl implements EE6ExampleMXBean
{
    private ObjectName objectName;
    private MBeanServer platformMBeanServer;
    private String className = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);

    // attributes
    private String status = "not started";
    private String readOnlyAttribute = "This Is Read Only, it cannot be changed through JMX";
    private String writeOnlyAttribute = "This Is Write Only, it cannot be viewed through JMX";
    private String readWriteAttribute = "This Is Read/Write, it can be be viewed & changed through JMX";

    @PostConstruct
    public void start()
    {
        status = "starting";
        try {
            objectName = new ObjectName(this.getClass().getPackage().getName() + ":service=" + className);
            platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            platformMBeanServer.registerMBean(this, objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to register " + objectName + " into JMX:" + e);
        }
        status = "started & registered in jmx";
        System.out.println(this.getClass().getName() + ": registered MBean at: " + objectName + " use jconsole or other JMX tool to test it");
    }

    @PreDestroy
    public void stop()
    {
        status = "stopping";
        try {
            platformMBeanServer.unregisterMBean(objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to unregister " + objectName + " from JMX:" + e);
        }
        status = "stopped & unregistered from jmx";
    }

    // This is a method which the mbean is exposing
    public String hello(String name)
    {
        return "Hello " + name;
    }

    // This is a read only attribute the mbean is exposing.
    // To make it writeable add a public void setStatus(String) to the interface and mbean implementation
    public String getStatus()
    {
        return status;
    }

    public String getReadWriteAttribute()
    {
        return readWriteAttribute;
    }

    public void setReadWriteAttribute(String readWriteAttribute)
    {
        this.readWriteAttribute = readWriteAttribute;
    }

    public void setWriteOnlyAttribute(String writeOnlyAttribute)
    {
        this.writeOnlyAttribute = writeOnlyAttribute;
        System.out.println(this.getClass().getName() + ": writeOnlyAttribute(" + writeOnlyAttribute + ")");
    }

    public String getReadOnlyAttribute()
    {
        return readOnlyAttribute;
    }
}