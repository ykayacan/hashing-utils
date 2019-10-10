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

package com.ykayacan.hashing.consistent;

import com.ykayacan.hashing.api.Node;
import java.util.Objects;
import java.util.Optional;

/** The type Physical node. */
public class PhysicalNode implements Node {

  private final String nodeId;
  private final Object data;
  private final int virtualNodeCount;

  /**
   * Instantiates a new Physical node.
   *
   * @param nodeId the node id
   * @param data the data
   * @param virtualNodeCount the virtual node count
   */
  protected PhysicalNode(String nodeId, Object data, int virtualNodeCount) {
    this.nodeId = nodeId;
    this.data = data;
    this.virtualNodeCount = virtualNodeCount;
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
   * Gets virtual node count.
   *
   * @return the virtual node count
   */
  public int getVirtualNodeCount() {
    return virtualNodeCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PhysicalNode)) {
      return false;
    }
    PhysicalNode that = (PhysicalNode) o;
    return virtualNodeCount == that.virtualNodeCount
        && Objects.equals(nodeId, that.nodeId)
        && Objects.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, data, virtualNodeCount);
  }

  @Override
  public String toString() {
    return "PhysicalNode{"
        + "nodeId='"
        + nodeId
        + '\''
        + ", data="
        + data
        + ", virtualNodeCount="
        + virtualNodeCount
        + '}';
  }

  /** The type Builder. */
  public static final class Builder {

    private String nodeId;
    private Object data;
    private int virtualNodeCount;

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
     * Virtual node count builder.
     *
     * @param virtualNodeCount the virtual node count
     * @return the builder
     */
    public Builder virtualNodeCount(int virtualNodeCount) {
      this.virtualNodeCount = virtualNodeCount;
      return this;
    }

    /**
     * Build physical node.
     *
     * @return the physical node
     */
    public PhysicalNode build() {
      return new PhysicalNode(nodeId, data, virtualNodeCount);
    }
  }
}
