package com.wizardlybump17.wlib.object;

import java.util.Map;

public interface Serializable {

    /**
     * Serializes this object into a more human code
     * @return the serialized object
     */
    Map<String, Object> serialize();
    //static Serializable deserialize(Map<String, Object> map); :C
}
