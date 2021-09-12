<h1 align="center">Ping Nametags (Fabric)</h1>

<p align="center">
  <img src="./src/main/resources/assets/pingnametags/icon.png" height="256">
</p>

<p align="center">
  A minecraft mod that adds colored ping values inside each player's nametag.
</p>

<p align="center">
  <a href="https://github.com/paul-soporan/ping-nametags/releases/latest"><img alt="Latest Release" src="https://img.shields.io/github/v/release/paul-soporan/ping-nametags?include_prereleases"></a>
  <a href="https://github.com/paul-soporan/ping-nametags/actions?query=workflow%3Abuild"><img alt="GitHub Actions Build Workflow" src="https://github.com/paul-soporan/ping-nametags/workflows/build/badge.svg"></a>
  <a href="https://github.com/paul-soporan/ping-nametags/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/paul-soporan/ping-nametags"></a>
</p>

---

## Installation

### Stable Releases (recommended)

The latest stable releases are published on the [GitHub releases page](https://github.com/paul-soporan/ping-nametags/releases).

[**Click here to get the latest stable release**](https://github.com/paul-soporan/ping-nametags/releases/latest)

### Bleeding-edge Builds (unstable)

Bleeding-edge builds can be found under the artifacts produced by [the `build` GitHub actions workflow](https://github.com/paul-soporan/ping-nametags/actions?query=workflow%3Abuild&query=event%3Apush).

## Configuration

The configuration can be edited in-game if [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) is installed.

| Option                 | Type            | Default Value | Description                                                                                                        |
|----------------------- |-----------------|---------------|--------------------------------------------------------------------------------------------------------------------|
| `enabled`              | `boolean`       | `true`        | Whether ping nametags are enabled or not.                                                                          |
| `pingTextPosition`     | `Left \| Right` | `Right`       | The position of the ping text placed next to the name.                                                             |
| `pingTextFormatString` | `String`        | `"(%dms)"`    | The format string for ping text. Must include a `%d`, which will be replaced dynamically by the actual ping value. |
