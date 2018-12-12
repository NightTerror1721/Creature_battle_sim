/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author Asus
 */
public final class AttackPool
{
    private static final HashMap<Integer, AttackModel> MODELS = new HashMap<>();
    
    public static final Attack createAttack(int modelId)
    {
        return new Attack(getModel(modelId));
    }
    
    public static final AttackModel getModel(int modelId)
    {
        AttackModel model = MODELS.getOrDefault(modelId, null);
        return model == null ? loadModel(modelId) : model;
    }
    
    private static AttackModel loadModel(int modelId)
    {
        Path modelFile = Paths.get("data", "attacks", modelId + ".attmdl");
        if(!Files.isReadable(modelFile))
            return new AttackModel();
        //Serializer.
        return null;
    }
}
