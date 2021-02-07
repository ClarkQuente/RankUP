# RankUP
👌 Create rank evolutions for your server.

### Requirements to run

* Java 8
* [Spigot 1.8.8](https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar)

<h3>Commands</h3>

<table>
 <tr>
  <th>Command</th>
  <th>Permission</th>
  <th>Description</th>
 </tr>
 <tr>
  <td>/ranks <new delay></td>
  <td>none</td>
  <td>View ranks.</td>
 </tr>
 <tr>
  <td>/rankup <new delay></td>
  <td>none</td>
  <td>Evolve ranks.</td>
 </tr>
</table>

<h3>Configuration Files</h3>

<h4>Configuration Files</h4>
config.yml

```yml
settings:
  data_storage:
    storage_type: "SQLITE" # MYSQL or SQLITE
    mysql_credentials: # Complete only if the storage_type is MYSQL.
      host: "localhost"
      port: 3306
      database: "database_name"
      username: "root"
      password: "password"
    sqlite_file:  # Touch only if the storage_type is SQLITE.
      name: "rankup_storage"
  auto_save_data:
    active: true # false = Disable save data task. Recommended: true
    check_delay: 5 # In minutes.
  auto_rank_up:
    active: false # false = Disable auto rank up task.
    check_delay: 10 # In seconds.
  ranks_menu: true
  rank_up_confirmation: true

view:
  ranks:
    title: "Ranks"
    size: 6
  confirmation:
    title: "RankUP - Confirmation"
    info:
      - "&fCost: &2$&f{cost}"
    accept:
      display: "&aConfirm"
      description:
        - "&7Click to confirm."
    cancel:
      display: "&cCancel"
      description:
        - "&7Click to cancel."
```

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
