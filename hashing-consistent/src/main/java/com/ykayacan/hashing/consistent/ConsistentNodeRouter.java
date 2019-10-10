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

import com.ykayacan.hashing.api.HashFunction;
import com.ykayacan.hashing.api.NodeRouter;
import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConsistentNodeRouter<N extends PhysicalNode> implements NodeRouter<N> {

  /** All the current nodes in the pool */
  private final NavigableMap<Long, VirtualNode<N>> ring;

  private final HashFunction hashFunction;

  private ConsistentNodeRouter(Collection<N> initialNodes, HashFunction hashFunction) {
    Objects.requireNonNull(initialNodes);
    Objects.requireNonNull(hashFunction);

    this.ring = new ConcurrentSkipListMap<>();
    this.hashFunction = hashFunction;
    initialNodes.forEach(this::addNode);
  }

  /**
   * Create node router.
   *
   * @param <N> the type parameter
   * @param hashFunction the hash function
   * @return the node router
   */
  public static <N extends PhysicalNode> NodeRouter<N> create(HashFunction hashFunction) {
    return create(Collections.emptyList(), hashFunction);
  }

  /**
   * Create node router.
   *
   * @param <N> the type parameter
   * @param initialNodes the initial nodes
   * @param hashFunction the hash function
   * @return the node router
   */
  public static <N extends PhysicalNode> NodeRouter<N> create(
      Collection<N> initialNodes, HashFunction hashFunction) {
    return new ConsistentNodeRouter<>(initialNodes, hashFunction);
  }

  @Override
  public Optional<N> getNode(String nodeId) {
    Objects.requireNonNull(nodeId);

    if (ring.isEmpty()) {
      return Optional.empty();
    }

    Long hash = hashFunction.hash(nodeId);
    SortedMap<Long, VirtualNode<N>> tailMap = ring.tailMap(hash);
    Long nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();

    return Optional.ofNullable(ring.get(nodeHash)).map(VirtualNode::getPhysicalNode);
  }

  @Override
  public void addNode(N node) {
    Objects.requireNonNull(node);

    int vNodeCount = node.getVirtualNodeCount();
    if (vNodeCount < 0) {
      throw new IllegalArgumentException("Illegal virtual node count: " + vNodeCount);
    }
    int existingReplicaCount = (int) getExistingReplicaCount(node);
    for (int i = 0; i < vNodeCount; i++) {
      VirtualNode<N> vNode = VirtualNode.create(node, i + existingReplicaCount);
      long hash = hashFunction.hash(vNode.getNodeId());
      ring.put(hash, vNode);
    }
  }

  @Override
  public void removeNode(String nodeId) {
    Objects.requireNonNull(nodeId);

    ring.values().removeIf(virtualNode -> virtualNode.isVirtualNodeOf(nodeId));
  }

  private long getExistingReplicaCount(N physicalNode) {
    return ring.values().stream()
        .filter(virtualNode -> virtualNode.isVirtualNodeOf(physicalNode))
        .count();
  }
}
