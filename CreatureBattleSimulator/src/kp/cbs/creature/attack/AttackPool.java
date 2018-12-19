/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import kp.cbs.creature.Creature;
import kp.cbs.utils.Paths;
import kp.cbs.utils.Serializer;
import kp.udl.exception.UDLException;

/**
 *
 * @author Asus
 */
public final class AttackPool
{
    private static final HashMap<Integer, AttackModel> MODELS = new HashMap<>();
    private static List<AttackModel> ALL;
    
    public static final Attack createAttack(int modelId)
    {
        return new Attack(getModel(modelId));
    }
    
    public static final Attack createAttack(AttackModel model)
    {
        return new Attack(model);
    }
    
    public static final AttackModel getModel(int modelId)
    {
        AttackModel model = MODELS.getOrDefault(modelId, null);
        return model == null ? loadModel(modelId, true) : model;
    }
    
    public static final List<AttackModel> getAllModels(boolean copy)
    {
        if(ALL != null)
            return copy ? new LinkedList<>(ALL) : Collections.unmodifiableList(ALL);
        if(!Files.isDirectory(Paths.ATTACKS))
            return copy ? new LinkedList<>() : List.of();
        try
        {
            ALL = Files.list(Paths.ATTACKS)
                    .filter(AttackPool::isValidFile)
                    .map(AttackPool::mapFileToModel)
                    .collect(Collectors.toList());
            return copy ? new LinkedList<>(ALL) : Collections.unmodifiableList(ALL);
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return copy ? new LinkedList<>() : List.of();
        }
    }
    
    public static final AttackModel createCombatAttackModel(Creature creature)
    {
        return CombatAttackModelGenerator.generate(creature.getLevel());
    }
    
    public static final boolean registerNewOrUpdateModel(AttackModel model, boolean isNew)
    {
        if(isNew)
            model.setId(getAllModels(false).size());
        Path modelFile = Paths.concat(Paths.ATTACKS, generateFilename(model.getId()));
        try
        {
            var value = AttackModel.LOADER.serialize(model);
            Serializer.write(value, modelFile);
            MODELS.put(model.getId(), model);
            ALL.remove(model);
            ALL.add(model);
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return false;
        }
        return true;
    }
    
    private static AttackModel loadModel(int modelId, boolean showError)
    {
        Path modelFile = Paths.concat(Paths.ATTACKS, generateFilename(modelId));
        try
        {
            var model = readFileModel(modelFile);
            model.setId(modelId);
            MODELS.put(modelId, model);
            return model;
        }
        catch(IOException | UDLException ex)
        {
            if(showError)
            {
                ex.printStackTrace(System.err);
                return new AttackModel();
            }
            return null;
        }
    }
    
    private static AttackModel readFileModel(Path modelFile) throws IOException, UDLException
    {
        if(!Files.isReadable(modelFile))
            throw new IOException("File not found or not redeable");
        var base = Serializer.read(modelFile);
        var model = AttackModel.LOADER.unserialize(base);//Serializer.inject(base, AttackModel.class);
        if(model == null)
            throw new IOException("Fail to read AttackModel in file: " + modelFile);
        return model;
    }
    private static AttackModel mapFileToModel(Path modelFile)
    {
        try
        {
            var model = readFileModel(modelFile);
            MODELS.put(model.getId(), model);
            return model;
        }
        catch(IOException | UDLException ex)
        {
            ex.printStackTrace(System.err);
            return new AttackModel();
        }
    }
    
    private static final Pattern MODEL_FILE_PATTERN = Pattern.compile("[0-9]+\\.attmdl");
    
    private static boolean isValidFile(Path file)
    {
        return MODEL_FILE_PATTERN.matcher(file.getFileName().toString()).matches();
    }
    
    private static final String generateFilename(int modelId) { return modelId + ".attmdl"; }
}
