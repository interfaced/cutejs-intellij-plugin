# CuteJS IntelliJ plugin

## Info

Associated file extension is **.jst**

## Features

- [x] Syntax highlighting
  - [x] Open/close template block braces
  - [x] HTML markup
  - [x] JS code inside template blocks
  - [x] Custom highlighting:
    - [x] type annotation blocks
    - [x] export blocks
    - [x] inline component blocks
- [x] Supporting custom attributes for HTML elements: *data-export-id*, *data-component*
- [x] Brace matching for template block braces
- [x] Auto-closing template block braces after typing *{{*
- [x] Complete HTML markup support (means working tags autocompletion etc.)
- [x] JS code analysis (means syntax errors annotations, autocompletion, go-to-declaration etc.)
- [x] Custom icon for jst files
- [ ] Providing reference on template declaration in jst file (go-to-declaration from js files to jst)
- [ ] Providing predefined libs such as `cuteJs` in jst files (for js injections)
- [ ] Smart scope providing for js injections (binding with template compiled code)
  
## Issues

- No tests
- Auto-closing braces may put unnecessary last brace
  - Fix: disable `Insert paired braces` in ide preferences

## Install

### From zip/jar bundle

1. Download plugin zip archive from [releases](https://github.com/interfaced/cutejs-intellij-plugin/releases) page or build it from source
2. In your IDE open `Preferences -> Plugins`
3. Choose plugin archive by `Install plugin from disk...`

## Build

For build and bundle plugin in zip archive (will located in `build/distributions`)

```
./gradlew buildPlugin
```

For run IntelliJ IDEA with bundled plugin for test

By default `runIde` will try to launch WebStorm in `/Applications/WebStorm.app` (path to your ide can be changed in `build.gradle` file)

```
./gradlew runIde
```
