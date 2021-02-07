# RankUP
👌 Create rank evolutions for your server.

### <h3>Requirements to run
* Java 8+
* [Spigot 1.8.8](https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar)

<h3>Using RankUP API</h3>

```java
final SkyRankUPAPI skyRankUPAPI = SkyRankUPAPI.getInstance();
```

<h3>Using Custom Event</h3>

```java
@EventHandler
private void onPlayerRankUP(PlayerRankUPEvent event) {
    if (event.isCancelled())
        return;

    val player = event.getPlayer();

    player.sendMessage("Custom Event");
    player.sendMessage("Cost: $" + event.getCost());
    player.sendMessage("Old Rank Tag: " + event.getOldRank().getTag());
    player.sendMessage("New Rank Tag: " + event.getNextRank().getTag());
}
```

<h3>Installing with Maven</h3>

```xml
<dependency>
    <groupId>com.github.SkyLinx</groupId>
    <artifactId>RankUP</artifactId>
    <version>Tag</version>
</dependency>
```

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

<h3>Installing with Gradle</h3>

```gradle
dependencies {
    implementation 'com.github.SkyLinx:RankUP:Tag'
}
```

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
