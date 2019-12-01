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

package io.github.ykayacan.hashing.api;

import java.util.Optional;

/**
 * The base interface for {@link NodeRouter}
 *
 * @param <N> the {@link Node} parameter
 */
public interface NodeRouter<N extends Node> {

  /**
   * Returns node for given nodeId.
   *
   * @param nodeId Any string value
   * @return Node assigned to the nodeId given as an argument
   * @throws NullPointerException if nodeId value is null
   */
  Optional<N> getNode(String nodeId);

  /**
   * @param node node to be added
   * @throws NullPointerException if {@code nodeId} is null
   */
  void addNode(N node);

  /**
   * @param nodes nodes to be added
   * @throws NullPointerException if {@code nodes} is null
   */
  default void addNodes(Iterable<N> nodes) {
    nodes.forEach(this::addNode);
  }

  /**
   * @param nodeId nodeId to be removed
   * @throws NullPointerException if {@code nodeId} is null
   */
  void removeNode(String nodeId);

  /**
   * Returns same instance of {@link NodeRouter}.
   *
   * @param nodeIds nodeIds to be removed
   * @throws NullPointerException if {@code nodeIds} is null
   */
  default void removeNodes(Iterable<String> nodeIds) {
    nodeIds.forEach(this::removeNode);
  }
}
