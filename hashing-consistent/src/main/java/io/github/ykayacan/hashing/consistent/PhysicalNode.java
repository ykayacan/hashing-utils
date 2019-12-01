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

package io.github.ykayacan.hashing.consistent;

import io.github.ykayacan.hashing.api.Node;
import java.util.Objects;
import java.util.Optional;
import org.checkerframework.checker.nullness.qual.Nullable;

/** The type Physical node. */
public class PhysicalNode implements Node {

  private final String nodeId;
  @Nullable private final Object data;

  /**
   * Instantiates a new Physical node.
   *
   * @param nodeId the node id
   * @param data the data
   */
  private PhysicalNode(String nodeId, @Nullable Object data) {
    this.nodeId = nodeId;
    this.data = data;
  }

  public static PhysicalNode of(String nodeId) {
    return newBuilder(nodeId).build();
  }

  public static Builder newBuilder(String nodeId) {
    return new Builder(nodeId);
  }

  @Override
  public String getNodeId() {
    return nodeId;
  }

  @Override
  public Optional<Object> getData() {
    return Optional.ofNullable(data);
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
    return Objects.equals(nodeId, that.nodeId) && Objects.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, data);
  }

  @Override
  public String toString() {
    return "PhysicalNode{" + "nodeId='" + nodeId + '\'' + ", data=" + data + '}';
  }

  /** The type Builder. */
  public static final class Builder {

    private String nodeId;
    @Nullable private Object data;

    private Builder(String nodeId) {
      Objects.requireNonNull(nodeId);
      this.nodeId = nodeId;
    }

    /**
     * Data builder.
     *
     * @param data the data
     * @return the builder
     */
    public Builder data(@Nullable Object data) {
      this.data = data;
      return this;
    }

    /**
     * Build physical node.
     *
     * @return the physical node
     */
    public PhysicalNode build() {
      return new PhysicalNode(nodeId, data);
    }
  }
}
