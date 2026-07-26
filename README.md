# Chronos Timer

Chronos Timer is a client-side Fabric mod for Minecraft that replaces the
vanilla status effect HUD with configurable timers and progress bars.

## Features

- Timer and progress-bar display modes
- Top, bottom and side attachments
- Horizontal and vertical layouts
- Left and right screen positioning
- Top, center and bottom anchoring
- Configurable HUD opacity
- Roman numeral effect levels
- Warning color during the last 10 seconds
- Support for ambient status effects
- Integration with Nythral Library

## Requirements

- Minecraft 1.21.11
- Java 21
- Fabric Loader 0.19.3 or newer
- Fabric API 0.141.5+1.21.11 or newer
- Nythral Library 1.0.0 or newer

## Installation

1. Install Fabric Loader for Minecraft 1.21.11.
2. Install Fabric API.
3. Install Nythral Library.
4. Download Chronos Timer.
5. Place all required `.jar` files in the Minecraft `mods` directory.

## Configuration

Chronos Timer provides configurable options for:

- enabling and disabling the custom HUD,
- screen anchor,
- left or right positioning,
- horizontal or vertical layout,
- timer or progress-bar display,
- top, bottom or side attachment,
- HUD opacity.

The default configuration is:

```text
Enabled: true
Anchor: TOP
Side: RIGHT
Layout: HORIZONTAL
Display: TIMER
Attachment: BOTTOM
Opacity: 100%
```

## Building

Clone the repository and run:

```bash
./gradlew build
```

On Windows PowerShell:

```powershell
.\gradlew.bat build
```

The generated mod file will be available in:

```text
build/libs
```

## Source code

Source repository:

```text
https://github.com/Uriaras/chronos-timer
```

Issues:

```text
https://github.com/Uriaras/chronos-timer/issues
```

## License

Chronos Timer is available under the MIT License.

See the `LICENSE` file for details.