# Hashing Utils

[![Build Status](https://travis-ci.com/ykayacan/hashing-utils.svg?branch=master)](https://travis-ci.com/ykayacan/hashing-utils)
![GitHub](https://img.shields.io/github/license/ykayacan/hashing-utils)

## Table of Contents
+ [About](#about)
+ [Getting Started](#getting_started)
+ [Usage](#usage)
+ [Contributing](CONTRIBUTING.md)

## About <a name = "about"></a>
A basic Java implementation of [Consistent Hashing](https://en.wikipedia.org/wiki/Consistent_hashing), 
[Rendezvous Hashing](https://en.wikipedia.org/wiki/Rendezvous_hashing) and Weighted Rendezvous Hashing.

## Getting Started <a name = "getting_started"></a>

### Latest Stable Releases
![Bintray](https://img.shields.io/bintray/v/ykayacan/hashing-utils/hashing-api?label=hashing-api)
![Bintray](https://img.shields.io/bintray/v/ykayacan/hashing-utils/hashing-consistent?label=hashing-consistent)
![Bintray](https://img.shields.io/bintray/v/ykayacan/hashing-utils/hashing-rendezvous?label=hashing-rendezvous)

#### Gradle

```groovy
repositories {
  jcenter()
}

dependencies {
  implementation 'io.github.ykayacan.hashing:hashing-api:LATEST_VERSION'
  implementation 'io.github.ykayacan.hashing:hashing-consistent:LATEST_VERSION'
  implementation 'io.github.ykayacan.hashing:hashing-rendezvous:LATEST_VERSION'
}
```

#### Maven

```xml
<repositories>
  <repository>
    <id>jcenter</id>
    <url>https://jcenter.bintray.com</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>io.github.ykayacan.hashing</groupId>
    <artifactId>hashing-api</artifactId>
    <version>LATEST_VERSION</version>
  </dependency>

  <dependency>
    <groupId>io.github.ykayacan.hashing</groupId>
    <artifactId>hashing-consistent</artifactId>
    <version>LATEST_VERSION</version>
  </dependency>

  <dependency>
    <groupId>io.github.ykayacan.hashing</groupId>
    <artifactId>hashing-rendezvous</artifactId>
    <version>LATEST_VERSION</version>
  </dependency>
</dependencies>
```

### Snapshots

You can access the latest snapshot by adding the repository `https://oss.jfrog.org/artifactory/oss-snapshot-local`
to your build.

Snapshots of the development version are available in [Jfrog's snapshots repository](https://oss.jfrog.org/list/oss-snapshot-local/io/github/ykayacan/hashing).

## Usage <a name = "usage"></a>

#### Consistent Hashing

```
NodeRouter<PhysicalNode> router = 
    ConsistentNodeRouter.create(15, MurMurHashFunction.create());

List<PhysicalNode> initialNodes = 
    Arrays.asList(PhysicalNode.of("node1"), PhysicalNode.of("node2"));

router.addNodes(initialNodes);

// get
Optional<PhysicalNode> nodeOpt = router.getNode("node1");

// add
router.addNode(PhysicalNode.of("node3"));

// remove
router.removeNode("node1");
```

#### Rendezvous Hashing

```
NodeRouter<WeightedNode> router = RendezvousNodeRouter.create(
    MurMurHashFunction.create(), DefaultRendezvousStrategy.create());

List<WeightedNode> initialNodes = 
    Arrays.asList(WeightedNode.of("node1"), WeightedNode.of("node2"));

router.addNodes(initialNodes);

// get
Optional<WeightedNode> nodeOpt = router.getNode("node1");

// add
router.addNode(WeightedNode.of("node3"));

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
