# VelocityBroadcast Reborn

A lightweight, modern network-wide broadcast plugin for **Velocity**.

Send announcements to every player connected to your proxy with a single command. Supports both legacy color codes (`&a`, `&l`, etc.) and full MiniMessage formatting.

---

## Credits & History

**Original author:** [AdzelFirestar](https://github.com/AdzelFirestar)  
Original repository: [AdzelFirestar/velocitybroadcast-reborn](https://github.com/AdzelFirestar/velocitybroadcast-reborn)

This repository is a **fork** maintained and updated by [dpembo](https://github.com/dpembo).  
The core concept and original implementation belong to AdzelFirestar. This fork includes ongoing maintenance, fixes, and improvements.

---

## Features

- Network-wide broadcasts from the Velocity proxy
- Customizable broadcast prefix
- Legacy color codes **and** MiniMessage support
- Simple config file
- Permission-based access (`vb.broadcast` / `vb.admin`)
- Reload command (no restart needed for config changes)
- Debug mode
- Extremely lightweight

---

## Requirements

- **Velocity** 3.1.1+ (tested with modern Velocity builds)
- A permissions plugin on the proxy (recommended: **LuckPerms**)
- Java 17+

> **Note:** Velocity does not have traditional "op". You **must** assign the permission nodes via a permissions plugin (e.g. LuckPerms). Being op on a backend server will **not** grant these permissions.

---

## Installation

1. Download the latest JAR from the [Releases](https://github.com/dpembo/velocitybroadcast-reborn/releases) page (or build it yourself).
2. Place the JAR in your **Velocity proxy** `plugins/` folder.  
   **Do not** put it on backend servers.
3. Restart the proxy.
4. Assign permissions (see below).
5. (Optional) Edit `plugins/velocitybroadcast/config.yml` and run `/vb reload`.

---

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/vb` or `/vb help` | Shows the help page | None |
| `/vb <message>` | Broadcasts a message to every player on the network | `vb.broadcast` |
| `/vb prefix <new prefix>` | Changes the broadcast prefix | `vb.admin` |
| `/vb reload` | Reloads the configuration | `vb.admin` |

### Examples

```
/vb &cServer restarting in 5 minutes!
/vb <red>Maintenance starting soon</red>
/vb prefix &9&l[&bNetwork&9&l]&r 
/vb reload
```

---

## Permissions

| Permission | Description | Recommended for |
|------------|-------------|-----------------|
| `vb.broadcast` | Allows sending network broadcasts | Staff / Moderators |
| `vb.admin` | Allows changing the prefix and reloading the config | Admins |

### LuckPerms examples

```bash
# Give a user the broadcast permission
lpv user <username> permission set vb.broadcast true

# Give a group both permissions
lpv group admin permission set vb.broadcast true
lpv group admin permission set vb.admin true
```

---

## Configuration

The config is generated at `plugins/velocitybroadcast/config.yml` on first start.

```yaml
# DO NOT EDIT
Plugin Version: '...' # Do not edit this value

# ONLY EDIT BELOW THIS LINE
debug-messages-enabled: false   # Enables/disables debug messages (Default: false)
version-check-enabled: true     # Toggles version update messages for admins (Default: true)
prefix: '&9&l[&3&lServer&9&l]&r '  # The prefix for broadcasts
```

- The prefix supports both legacy codes (`&a`, `&l`, etc.) and MiniMessage.
- After editing the file manually, run `/vb reload`.

---

## Building from Source

```bash
git clone https://github.com/dpembo/velocitybroadcast-reborn.git
cd velocitybroadcast-reborn
mvn clean package
```

The shaded JAR will be in the `target/` directory.

Requires **Maven** and **JDK 17+**.

---

## Related Plugin

If you want to trigger network broadcasts from **backend servers** (Paper/Spigot), check out the companion plugin:

**[VBBridge](https://github.com/dpembo/vbbridge)**  
Allows backend servers to run `/netbroadcast <message>`, which is relayed to this plugin's `/vb` command on the proxy.

---

## License

This project is licensed under the **Apache License 2.0**.  
See the [LICENSE](LICENSE) file for details.

---

## Support

Found a bug or have a feature request?  
Please open an issue on the [GitHub Issues](https://github.com/dpembo/velocitybroadcast-reborn/issues) page.
