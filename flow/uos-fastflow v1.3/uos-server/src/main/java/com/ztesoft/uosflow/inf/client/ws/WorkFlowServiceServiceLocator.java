/**
 * WorkFlowServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ztesoft.uosflow.inf.client.ws;

@SuppressWarnings("all")
public class WorkFlowServiceServiceLocator extends org.apache.axis.client.Service implements WorkFlowServiceService {

    public WorkFlowServiceServiceLocator() {
    }


    public WorkFlowServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WorkFlowServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WorkFlowServicePort
    private java.lang.String WorkFlowServicePort_address = "http://10.45.47.225:8001/IOMPROJ/services/WorkFlowServicePort";

    public java.lang.String getWorkFlowServicePortAddress() {
        return WorkFlowServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WorkFlowServicePortWSDDServiceName = "WorkFlowServicePort";

    public java.lang.String getWorkFlowServicePortWSDDServiceName() {
        return WorkFlowServicePortWSDDServiceName;
    }

    public void setWorkFlowServicePortWSDDServiceName(java.lang.String name) {
        WorkFlowServicePortWSDDServiceName = name;
    }

    public WorkFlowService getWorkFlowServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WorkFlowServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWorkFlowServicePort(endpoint);
    }

    public WorkFlowService getWorkFlowServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WorkFlowServicePortSoapBindingStub _stub = new WorkFlowServicePortSoapBindingStub(portAddress, this);
            _stub.setPortName(getWorkFlowServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWorkFlowServicePortEndpointAddress(java.lang.String address) {
        WorkFlowServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (WorkFlowService.class.isAssignableFrom(serviceEndpointInterface)) {
                WorkFlowServicePortSoapBindingStub _stub = new WorkFlowServicePortSoapBindingStub(new java.net.URL(WorkFlowServicePort_address), this);
                _stub.setPortName(getWorkFlowServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WorkFlowServicePort".equals(inputPortName)) {
            return getWorkFlowServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://exe.com/", "WorkFlowServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://exe.com/", "WorkFlowServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WorkFlowServicePort".equals(portName)) {
            setWorkFlowServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
