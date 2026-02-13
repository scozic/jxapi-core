package org.jxapi.exchanges.demo;

import java.util.List;
import java.util.Objects;

import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Sample POJO used in Demo exchange but defined 'externally' == outside generated package.
 * In order to be compatible with the generated code, it must implement {@link Pojo} interface methods.  
 */
public class TickerMeta implements Pojo<TickerMeta> {

  private static final long serialVersionUID = 8100694114884759834L;
  private List<String> chains;

  public List<String> getChains() {
      return chains;
  }

  public void setChains(List<String> chains) {
      this.chains = chains;
  }

  @Override
  public TickerMeta deepClone() {
    return new TickerMeta.Builder().chains(CollectionUtil.cloneList(this.chains)).build();
  }

  @Override
  public int compareTo(TickerMeta o) {
      return CompareUtil.compareLists(chains, o.chains, String::compareTo);
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TickerMeta that = (TickerMeta) o;
      return Objects.equals(chains, that.chains);
  }

  @Override
  public int hashCode() {
      return Objects.hash(chains);
  }

  @Override
  public String toString() {
      return EncodingUtil.pojoToString(this);
  }

  public static class Builder {
      private List<String> chains;

      public Builder() {
          this.chains = CollectionUtil.createList();
      }

      public Builder chains(List<String> chains) {
          this.chains = chains;
          return this;
      }

      public Builder addChain(String chain) {
          this.chains.add(chain);
          return this;
      }

      public TickerMeta build() {
          TickerMeta tickerMeta = new TickerMeta();
          tickerMeta.setChains(CollectionUtil.cloneList(this.chains));
          return tickerMeta;
      }
  }
}
