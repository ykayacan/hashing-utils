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

/**
 * The type Virtual node.
 *
 * @param <N> the type parameter
 */
final class VirtualNode<N extends Node> {
  private final N physicalNode;
  private final int replicaIndex;
  private final String nodeId;

  private VirtualNode(N physicalNode, int replicaIndex) {
    this.physicalNode = physicalNode;
    this.replicaIndex = replicaIndex;
    nodeId = physicalNode.getNodeId() + "-" + replicaIndex;
  }

  /**
   * Create virtual node.
   *
   * @param <N> the type parameter
   * @param physicalNode the physical node
   * @param replicaIndex the replica index
   * @return the virtual node
   */
  static <N extends Node> VirtualNode<N> create(N physicalNode, int replicaIndex) {
    return new VirtualNode<>(physicalNode, replicaIndex);
  }

  /**
   * Gets node id.
   *
   * @return the node id
   */
  String getNodeId() {
    return nodeId;
  }

  /**
   * Gets physical node.
   *
   * @return the physical node
   */
  N getPhysicalNode() {
    return physicalNode;
  }

  /**
   * Checks if current node is virtual node of given physical node.
   *
   * @param node the physical node
   * @return the boolean
   */
  boolean isVirtualNodeOf(Node node) {
    return physicalNode.equals(node);
  }

  /**
   * Checks if current node is virtual node of given physical node id.
   *
   * @param nodeId the physical nodeId
   * @return the boolean
   */
  boolean isVirtualNodeOf(String nodeId) {
    return physicalNode.getNodeId().equals(nodeId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VirtualNode<?> that = (VirtualNode<?>) o;
    return replicaIndex == that.replicaIndex && Objects.equals(physicalNode, that.physicalNode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(physicalNode, replicaIndex);
  }

  @Override
  public String toString() {
    return "VirtualNode{"
        + "physicalNode="
        + physicalNode
        + ", replicaIndex="
        + replicaIndex
        + ", nodeId='"
        + nodeId
        + '\''
        + '}';
  }
}
