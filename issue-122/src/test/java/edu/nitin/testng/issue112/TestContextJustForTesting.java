package edu.nitin.testng.issue112;

import org.testng.IClass;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.xml.XmlTest;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created on 12/31/15.
 *
 * @author <a href="mailto:nitin.matrix@gmail.com">Nitin Verma</a>
 */
public class TestContextJustForTesting implements ITestContext {
  @Override
  public String getName() {
    return null;
  }

  @Override
  public Date getStartDate() {
    return null;
  }

  @Override
  public Date getEndDate() {
    return null;
  }

  @Override
  public IResultMap getPassedTests() {
    return null;
  }

  @Override
  public IResultMap getSkippedTests() {
    return null;
  }

  @Override
  public IResultMap getFailedButWithinSuccessPercentageTests() {
    return null;
  }

  @Override
  public IResultMap getFailedTests() {
    return null;
  }

  @Override
  public String[] getIncludedGroups() {
    return new String[0];
  }

  @Override
  public String[] getExcludedGroups() {
    return new String[0];
  }

  @Override
  public String getOutputDirectory() {
    return null;
  }

  @Override
  public ISuite getSuite() {
    return null;
  }

  @Override
  public ITestNGMethod[] getAllTestMethods() {
    return new ITestNGMethod[0];
  }

  @Override
  public String getHost() {
    return null;
  }

  @Override
  public Collection<ITestNGMethod> getExcludedMethods() {
    return null;
  }

  @Override
  public IResultMap getPassedConfigurations() {
    return null;
  }

  @Override
  public IResultMap getSkippedConfigurations() {
    return null;
  }

  @Override
  public IResultMap getFailedConfigurations() {
    return null;
  }

  @Override
  public XmlTest getCurrentXmlTest() {
    return null;
  }

  @Override
  public List<com.google.inject.Module> getGuiceModules(Class<? extends com.google.inject.Module> cls) {
    return null;
  }

  @Override
  public com.google.inject.Injector getInjector(List<com.google.inject.Module> moduleInstances) {
    return null;
  }

  @Override
  public com.google.inject.Injector getInjector(IClass iClass) {
    return null;
  }

  @Override
  public void addInjector(List<com.google.inject.Module> moduleInstances, com.google.inject.Injector injector) {

  }

  @Override
  public Object getAttribute(String name) {
    return null;
  }

  @Override
  public void setAttribute(String name, Object value) {

  }

  @Override
  public Set<String> getAttributeNames() {
    return null;
  }

  @Override
  public Object removeAttribute(String name) {
    return null;
  }
}
