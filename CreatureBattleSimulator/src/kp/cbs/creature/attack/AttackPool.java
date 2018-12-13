/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.cbs.creature.attack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
    
    public static final AttackModel getModel(int modelId)
    {
        AttackModel model = MODELS.getOrDefault(modelId, null);
        return model == null ? loadModel(modelId) : model;
    }
    
    public static final List<AttackModel> getAllModels()
    {
        if(ALL != null)
            return ALL;
        if(!Files.isDirectory(Paths.ATTACKS))
            return List.of();
        try
        {
            return ALL = Files.list(Paths.ATTACKS)
                    .filter(AttackPool::isValidFile)
                    .map(AttackPool::mapFileToModel)
                    .collect(Collectors.toUnmodifiableList());
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return List.of();
        }
    }
    
    private static AttackModel loadModel(int modelId)
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
            ex.printStackTrace(System.err);
            return new AttackModel();
        }
    }
    
    private static AttackModel readFileModel(Path modelFile) throws IOException, UDLException
    {
        if(!Files.isReadable(modelFile))
            throw new IOException("File not found or not redeable");
        var base = Serializer.read(modelFile);
        var model = AttackModel.SERIALIZER.unserialize(base);//Serializer.inject(base, AttackModel.class);
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
