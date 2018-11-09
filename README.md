# CuteJS IntelliJ plugin

## Info

Plugin provides support for CuteJS templates

## Features

* Syntax highlighting
* Blocks folding
* Line-marker for template references (with ability to go-to template)
* Go-to-declaration by references in template

## Install

### From zip/jar bundle

1. Download plugin zip archive from [releases](https://github.com/interfaced/cutejs-intellij-plugin/releases) page or build it from source
2. In your IDE open `Preferences -> Plugins`
3. Choose plugin archive by `Install plugin from disk...`

## Build

**Requirements:** Java 8

To build and bundle plugin in zip archive (will located in `build/distributions`)

```
./gradlew buildPlugin
```

To run IntelliJ IDEA with bundled plugin

By default `runIde` will try to launch WebStorm in `/Applications/WebStorm.app` (path to your ide can be changed in `build.gradle` file)

```
./gradlew runIde
```
