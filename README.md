# Hashing Utils

[![Build Status](https://travis-ci.com/ykayacan/hashing-utils.svg?branch=master)](https://travis-ci.com/ykayacan/hashing-utils)

## Table of Contents
+ [About](#about)
+ [Getting Started](#getting_started)
+ [Usage](#usage)
+ [Contributing](CONTRIBUTING.md)

## About <a name = "about"></a>
A basic Java implementation of [Consistent Hashing](https://en.wikipedia.org/wiki/Consistent_hashing), 
[Rendezvous Hashing](https://en.wikipedia.org/wiki/Rendezvous_hashing) and Weighted Rendezvous Hashing.

## Getting Started <a name = "getting_started"></a>

### Installing

#### Gradle

Add dependencies to `build.gradle`:

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/ykayacan/hashing-utils"
    }
}

dependencies {
  implementation 'com.ykayacan.hashing:hashing-api:LATEST_VERSION'
  // add one of them
  implementation 'com.ykayacan.hashing:hashing-consistent:LATEST_VERSION'
  implementation 'com.ykayacan.hashing:hashing-rendezvous:LATEST_VERSION'
}
```
#### Maven

In a Maven project, include the artifacts in the dependencies section of your `pom.xml`:

```xml
<dependencies>
  <dependency>
    <groupId>com.ykayacan.hashing</groupId>
    <artifactId>hashing-api</artifactId>
    <version>LATEST_VERSION</version>
  </dependency>
  <dependency>
    <groupId>com.ykayacan.hashing</groupId>
    <artifactId>hashing-consistent</artifactId>
    <version>LATEST_VERSION</version>
  </dependency>
  <dependency>
    <groupId>com.ykayacan.hashing</groupId>
    <artifactId>hashing-rendezvous</artifactId>
    <version>LATEST_VERSION</version>
  </dependency>
</dependencies>
```

## Usage <a name = "usage"></a>

#### Consistent Hashing

```java
NodeRouter<PhysicalNode> router = ConsistentNodeRouter.create(MurMurHashFunction.create());

List<PhysicalNode> initialNodes = Arrays.asList(
    PhysicalNode.newBuilder().nodeId("node1").virtualNodeCount(15).build(),
    PhysicalNode.newBuilder().nodeId("node2").virtualNodeCount(15).build());

router.addNodes(initialNodes);

// get
Optional<PhysicalNode> nodeOpt = router.getNode("node1");

// add
Node node3 = PhysicalNode.newBuilder().nodeId("node3").virtualNodeCount(15).build();
router.addNode(node3);

// remove
router.removeNode("node1");
```

#### Rendezvous Hashing

```java
NodeRouter<WeightedNode> router = RendezvousNodeRouter.create(
    MurMurHashFunction.create(), DefaultRendezvousStrategy.create());

List<WeightedNode> initialNodes = Arrays.asList(
    WeightedNode.newBuilder().nodeId("node1").build(),
    WeightedNode.newBuilder().nodeId("node2").build());

router.addNodes(initialNodes);

// get
Optional<WeightedNode> nodeOpt = router.getNode("node1");

// add
Node node3 = WeightedNode.newBuilder().nodeId("node3").build();
router.addNode(node3);

// remove
router.removeNode("node1");
```

## License

```text
Copyright 2019 Yasin Sinan Kayacan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
