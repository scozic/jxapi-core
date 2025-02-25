package com.scz.jxapi.util;

import java.io.Serializable;

public interface Pojo<T> extends Serializable, Cloneable, DeepCloneable<T>, Comparable<T> {

}
