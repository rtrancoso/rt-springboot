package br.com.rtrancoso.springboot.base.exception.model;

import java.io.Serializable;

public interface Error extends Serializable {

    String getCode();

    String getDescription();

}
