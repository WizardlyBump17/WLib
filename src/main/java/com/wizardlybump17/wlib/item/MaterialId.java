package com.wizardlybump17.wlib.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
@Data
public class MaterialId {

    private final Material material;
    private final short id;
    private final byte data;
}
