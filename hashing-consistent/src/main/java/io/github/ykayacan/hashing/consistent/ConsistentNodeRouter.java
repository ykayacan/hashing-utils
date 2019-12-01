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

import io.github.ykayacan.hashing.api.HashFunction;
import io.github.ykayacan.hashing.api.NodeRouter;
import java.util.Collection;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ConsistentNodeRouter<N extends PhysicalNode> implements NodeRouter<N> {

  /** All the current nodes in the pool */
  private final NavigableMap<Long, VirtualNode<N>> ring;

  private final HashFunction hashFunction;

  private final int replicaCount;

  private ConsistentNodeRouter(
      Collection<N> initialNodes, int replicaCount, HashFunction hashFunction) {
    Objects.requireNonNull(initialNodes);
    Objects.requireNonNull(hashFunction);

    if (replicaCount < 0) {
      throw new IllegalArgumentException("Illegal partition count: " + replicaCount);
    }

    this.ring = new ConcurrentSkipListMap<>();
    this.replicaCount = replicaCount;
    this.hashFunction = hashFunction;
    initialNodes.forEach(this::addNode);
  }

  /**
   * Create node router.
   *
   * @param <N> the type parameter
   * @param replicaCount the replica count
   * @param hashFunction the hash function
   * @return the node router
   */
  public static <N extends PhysicalNode> NodeRouter<N> create(
      @Positive int replicaCount, HashFunction hashFunction) {
    return create(Collections.emptyList(), replicaCount, hashFunction);
  }

  /**
   * Create node router.
   *
   * @param <N> the type parameter
   * @param initialNodes the initial nodes
   * @param replicaCount the replica count
   * @param hashFunction the hash function
   * @return the node router
   */
  public static <N extends PhysicalNode> NodeRouter<N> create(
      Collection<N> initialNodes, int replicaCount, HashFunction hashFunction) {
    return new ConsistentNodeRouter<>(initialNodes, replicaCount, hashFunction);
  }

  @Override
  public Optional<N> getNode(@NonNull String nodeId) {
    Objects.requireNonNull(nodeId);

    if (ring.isEmpty()) {
      return Optional.empty();
    }

    Long nodeHash = ring.ceilingKey(hashFunction.hash(nodeId));
    return Optional.ofNullable(ring.get(nodeHash)).map(VirtualNode::getPhysicalNode);
  }

  @Override
  public void addNode(N node) {
    Objects.requireNonNull(node);

    for (int i = 0; i < replicaCount; i++) {
      VirtualNode<N> virtualNode = VirtualNode.create(node, i);
      long hash = hashFunction.hash(virtualNode.getNodeId());
      ring.put(hash, virtualNode);
    }
  }

  @Override
  public void removeNode(String nodeId) {
    Objects.requireNonNull(nodeId);

    ring.values().removeIf(virtualNode -> virtualNode.isVirtualNodeOf(nodeId));
  }
}
