/*
 * Copyright 2019 Yasin Sinan Kayacan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ykayacan.hashing.rendezvous;

import com.ykayacan.hashing.api.Node;
import java.util.Objects;
import java.util.Optional;

/** The type Weighted node. */
public class WeightedNode implements Node, Comparable<WeightedNode> {

  private final String nodeId;
  private final Object data;
  private final int weight;

  /**
   * Instantiates a new Weighted node.
   *
   * @param nodeId the node id
   * @param weight the weight
   * @param data the data
   */
  protected WeightedNode(String nodeId, int weight, Object data) {
    this.nodeId = nodeId;
    this.data = data;
    this.weight = weight;
  }

  /**
   * New builder builder.
   *
   * @return the builder
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  @Override
  public String getNodeId() {
    return nodeId;
  }

  @Override
  public Optional<Object> getData() {
    return Optional.ofNullable(data);
  }

  /**
   * Gets weight.
   *
   * @return the weight
   */
  public int getWeight() {
    return weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WeightedNode)) {
      return false;
    }
    WeightedNode that = (WeightedNode) o;
    return weight == that.weight
        && Objects.equals(nodeId, that.nodeId)
        && Objects.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, data, weight);
  }

  @Override
  public String toString() {
    return "WeightedNode{"
        + "nodeId='"
        + nodeId
        + '\''
        + ", data="
        + data
        + ", weight="
        + weight
        + '}';
  }

  @Override
  public int compareTo(WeightedNode o) {
    return nodeId.compareTo(o.getNodeId());
  }

  /** The type Builder. */
  public static class Builder {

    private String nodeId;
    private Object data;
    private int weight;

    private Builder() {}

    /**
     * Node id builder.
     *
     * @param nodeId the node id
     * @return the builder
     */
    public Builder nodeId(String nodeId) {
      this.nodeId = nodeId;
      return this;
    }

    /**
     * Data builder.
     *
     * @param data the data
     * @return the builder
     */
    public Builder data(Object data) {
      this.data = data;
      return this;
    }

    /**
     * Weight builder.
     *
     * @param weight the weight
     * @return the builder
     */
    public Builder weight(int weight) {
      this.weight = weight;
      return this;
    }

    /**
     * Build weighted node.
     *
     * @return the weighted node
     */
    public WeightedNode build() {
      return new WeightedNode(nodeId, weight, data);
    }
  }
}
