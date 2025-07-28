# EnhancedLegacyText

[![Maven Central](https://img.shields.io/maven-central/v/dev.vankka/enhancedlegacytext?label=release)](https://central.sonatype.com/search?q=g%3Adev.vankka+a%3Aenhancedlegacytext)
![Sonatype (Snapshots)](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fdev%2Fvankka%2Fenhancedlegacytext%2Fmaven-metadata.xml&label=snapshot)

An alternative input format for [Adventure](https://github.com/KyoriPowered/adventure), 
building on top of the well known [legacy formatting codes](https://minecraft.fandom.com/wiki/Formatting_codes).

## Dependency Information
### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'dev.vankka:enhancedlegacytext:1.0.0'
}
```

### Maven
```xml
<dependency>
    <groupId>dev.vankka</groupId>
    <artifactId>enhancedlegacytext</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

## Basic Usage
```java
Component component = EnhancedLegacyText.get().buildComponent("&c&lThis is red, [click:open_url:https://github.com]this is clickable, this %placeholder% got replaced")
        .replace("%placeholder%", Component.text("<The placeholder replacement>").color(NamedTextColor.GREEN))
        .build();
```

## The Format

[Version 2.0.0+ Format](https://github.com/Vankka/EnhancedLegacyText/wiki/Format)

### Color & Formatting

- [The legacy codes](https://minecraft.fandom.com/wiki/Formatting_codes)
    + Exception: Formatting codes remain enabled after color codes (Can be re-enabled via `EnhancedLegacyText.Builder#colorResets`)
- Adventure's hex format, (`&#<hex>` / `&#abc123`)

### Color Gradients

Surrounded by `{` and `}`, seperated by `,`

Examples:
- `{&a,&2,&3}`
- `{&a,&#00aa00,&3}` (mix & match permitted)
- `{&#55ff55,&#00aa00,&#00aaaa}`

## Square Brackets format

### Click & Hover Events

Events are surrounded by `[` and `]`, and split at the first two `:`, first part being either `click` or `hover`

#### Click

Valid types: `open_url`, `run_command`, `suggest_command`, `change_page`, `copy_to_clipboard`

Examples:
- `[click:open_url:https://github.com/Vankka/EnhancedLegacyText]`
- `[click:run_command:say hello]`
- `[click:suggest_command:/help]`
- `[click:change_page:2]`
- `[click:copy_to_clipboard:Secret]`

#### Hover

Valid type: `show_text`

Examples:
- `[hover:show_text:Hello]`
- `[hover:show_text:&#00aa00Hello]`