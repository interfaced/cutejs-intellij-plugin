# CuteJS IntelliJ plugin

##Info

Associated extension is **.jst**

## Features

- Syntax highlighting
  - [x] Open/close tags (including variants of *{{* like *{{-*, *{{=*, etc.)
  - [x] Text outside of template blocks as HTML (that can be changed by *Template Data Language* option in ide)
  - [x] Text inside of template blocks as JS
  - [ ] Different syntax highlight depenging on type of template blocks
- Code assistance
  - [ ] Syntax errors annotation
  - [ ] Jump to source from widget code
  - [ ] Autocompletion (JS, HTML, Template tags)
  
## Issues

- No tests

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

```
./gradlew runIde
```