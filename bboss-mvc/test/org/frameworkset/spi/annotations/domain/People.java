package org.frameworkset.spi.annotations.domain;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class People implements Serializable {
  private static final long serialVersionUID = 1L;

  private List<Person> person;

  public People() {
    // empty constructor required for JAXB
  }

  public People(List<Person> person) {
    this.person = person;
  }

  public List<Person> getPerson() {
    return person;
  }

  public void setPerson(List<Person> person) {
    this.person = person;
  }

}
