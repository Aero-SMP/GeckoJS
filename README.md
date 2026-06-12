
# GeckoJS for NeoForge 1.21.1

GeckoJS is a powerful bridge between **GeckoLib** and **KubeJS**, allowing you to create high-quality animated items, armor, blocks, and entities using simple JavaScript scripts. This project is a remake of the original GeckoJS mod, modernized for Minecraft 1.21.1 and NeoForge.

---

## 🛠 Installation & Requirements

To use GeckoJS, you need the following mods installed:
- **NeoForge** (1.21.1)
- **KubeJS** (latest version for 1.21.1)
- **GeckoLib** (4.6.6 or higher)

---

## 📂 Asset Setup

GeckoJS relies on standard GeckoLib `.geo.json` models and `.animation.json` animations. By default, it looks for these files in your KubeJS assets folder.

### Default File Locations
When using `.defaultGeoModel()`, the following paths are assumed (replace `item_id` with your item's name):

| Type | Model Path (`assets/kubejs/geo/`) | Texture Path (`assets/kubejs/textures/`) | Animation Path (`assets/kubejs/animations/`) |
| :--- | :--- | :--- | :--- |
| **Items** | `item/item_id.geo.json` | `item/item_id.png` | `item/item_id.animation.json` |
| **Armor** | `armor/item_id.geo.json` | `armor/item_id.png` | `armor/item_id.animation.json` |
| **Blocks** | `block/item_id.geo.json` | `block/item_id.png` | `block/item_id.animation.json` |

---

## 🚀 Creating Animatable Objects

Register your objects in a **Startup Script** (`kubejs/startup_scripts/`).

### Available Builder Types
- `animatable` (Items or Blocks)
- `anim_sword`, `anim_pickaxe`, `anim_axe`, `anim_shovel`, `anim_hoe`
- `anim_shield`
- `anim_helmet`, `anim_chestplate`, `anim_leggings`, `anim_boots`

### Basic Example (Item)
```javascript
StartupEvents.registry('item', event => {
    event.create('cool_sword', 'anim_sword')
        .tier('diamond')
        .defaultGeoModel()
        .addController(controller => {
            controller.addAnimation(state => {
                if (state.isMoving()) {
                    return state.setAndContinue(RawAnimation.begin().thenLoop("walk"))
                }
                return state.setAndContinue(RawAnimation.begin().thenLoop("idle"))
            })
        })
})
```

---

## 🛡 Animatable Armor

Armor registration is similar to items but includes helpers for bone visibility, allowing you to hide specific parts of the player model or your custom model dynamically.

```javascript
event.create('fancy_helmet', 'anim_helmet')
    .material('iron')
    .defaultGeoModel()
    .armorItemUseGeoModel() // Makes the item icon use the 3D model
    .boneVisibility((renderer, slot) => {
        // Hide a specific bone on the armor model
        renderer.hideBone("extra_antenna") 
    })
```

### Bone Visibility Helpers
- `renderer.hideBone("boneName")`
- `renderer.showBone("boneName")`
- `renderer.setBoneVisible(renderer.getBone("boneName"), boolean)`
- `renderer.getHeadBone()`, `renderer.getChestBone()`, `renderer.getLeftArmBone()`, etc.

---

## 📦 Animatable Blocks

Blocks registered with the `animatable` type will automatically have an associated Block Entity that handles GeckoLib animations.

```javascript
StartupEvents.registry('block', event => {
    event.create('animated_pillar', 'animatable')
        .defaultGeoModel()
        .addController(controller => {
            controller.addAnimation(state => {
                return state.setAndContinue(RawAnimation.begin().thenLoop("spin"))
            })
        })
})
```

---

## 🧪 Advanced Animation API

### Animation Controllers
You can add multiple controllers to a single object. Each controller manages its own animation state.
- `.addController(name, controller => { ... })`
- `.addController(controller => { ... })` (uses default name)

### Scripting Helpers
The following GeckoLib classes are exposed to JavaScript:
- `RawAnimation`
- `PlayState`
- `AnimationController`
- `AnimationState`
- `GeoItem`
- `GeoBone`
- `EasingType`
- `DataTickets`
- `EquipmentSlot`

### RawAnimation Usage
- `RawAnimation.begin().thenPlay("anim")`: Plays once.
- `RawAnimation.begin().thenLoop("anim")`: Loops indefinitely.
- `RawAnimation.begin().thenWait(20).thenPlay("anim")`: Waits for 20 ticks before playing.

---

## 🏗 Building the Project

If you are a developer looking to build GeckoJS from source:

1. Clone the repository.
2. Ensure you have Java 21 installed.
3. Run the following command in the terminal:
   ```bash
   ./gradlew build
   ```
4. The compiled JAR will be in `build/libs/`.

---

## 📜 Credits

- **Original Project**: Created by **mesdag** (Original GeckoJS).
- **Remake/Port**: **MrClockwork** (Clockwork Team).
- **Dependencies**:
    - [GeckoLib](https://github.com/bernie-g/geckolib) by Bernie.
    - [KubeJS](https://kubejs.com/) by LatvianModder.
