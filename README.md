# EnhancedLegacyText
[![Maven Central](https://img.shields.io/maven-central/v/dev.vankka/enhancedlegacytext?label=release)](https://search.maven.org/search?q=g:dev.vankka%20a:enhancedlegacytext)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/dev.vankka/enhancedlegacytext?label=dev&server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/#view-repositories;snapshots~browsestorage~dev)

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

[View the format on the wiki](https://github.com/Vankka/EnhancedLegacyText/wiki/Format)