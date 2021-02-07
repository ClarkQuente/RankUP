# RankUP
👌 Create rank evolutions for your server.

<h3>Using RankUP API</h3>

```java
final SkyRankUPAPI skyRankUPAPI = SkyRankUPAPI.getInstance()
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
