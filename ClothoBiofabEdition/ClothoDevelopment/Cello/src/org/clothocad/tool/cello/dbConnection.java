/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.cello;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.iharder.dnd.TransferableObject.Fetcher;

import org.clothocore.api.core.Collector;
import org.clothocore.api.data.ObjLink;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Family;
import org.clothocore.api.data.Feature;
import org.clothocore.api.data.FeatureRelation;
import org.clothocore.api.data.Part;
import org.clothocore.api.plugin.ClothoConnection;
import org.clothocore.api.plugin.ClothoConnection.ClothoQuery;


/**
 *
 * @author rozagh
 */
public class dbConnection {
    
    public static ArrayList<Feature> fetchTranscriptionFactors() {
            featureList = Collector.getAll(ObjType.FEATURE);

            FeatureComp fc = new FeatureComp();
            Collections.sort((List)featureList, fc);

           ArrayList<Feature>  transFactors = new ArrayList<Feature>();
        Iterator featIter = featureList.iterator();
        while( featIter.hasNext() ) {
            Feature f = (Feature)featIter.next();

            HashSet fFam = f.getFamilyLinks();
            Iterator famIter = fFam.iterator();

            //while(famIter.hasNext()) {
            if (famIter.hasNext()) {
                String famName = Collector.get(ObjType.FAMILY, (String) famIter.next()).getName();
                if(famName.equals("smallMolecule"))
                    transFactors.add(f);
            } 
        }
        return transFactors;

    }
    
    
    static HashMap<Feature, CelloPrimitive> createTables(ArrayList<Feature> TFList, HashMap<String, HashMap<String, Feature>> inputOutputList, CelloGeneTable geneT,
            CelloIPromoterTable ipT, CelloRPromoterTable rpT, CelloRBSTable rbsT, CelloTerminatorTable tT, CelloGeneTable ogT,
            CelloRGenePromoterTable repGeneOrthT )
    {
        HashMap<Feature, CelloPrimitive> reqTFRelation = new HashMap<Feature, CelloPrimitive>();
        geneT.getTable().clear();
        ogT.getTable().clear();
        repGeneOrthT.getTable().clear();
        ipT.getTable().clear();
        rpT.getTable().clear();
        
        ArrayList<FeatureRelation> allFR = fetchRelations(TFList);
       HashMap<Feature,  ArrayList<Feature>> tfPromoter = new HashMap<Feature, ArrayList<Feature>>();
       ArrayList<Feature> reqTFs = getReqTFs(inputOutputList, "input");

        //creatGeneTable (allFR, geneT);
        creatTFPromoterTables(allFR ,tfPromoter);
        tfPromoter= sortTFXTable(tfPromoter, reqTFs);
         HashMap<Feature,  ArrayList<Feature>> origTFPromoter  = (HashMap<Feature, ArrayList<Feature>>) tfPromoter.clone();


        tfPromoter = fetchOrthogonalTable(tfPromoter, tfPromoter.size(), reqTFs);
        //creatOutGeneTable(allFR, geneT, ipT, rpT, ogT, repGeneOrthT);

        HashMap<Feature,  ArrayList<Feature>> tfGene = new HashMap<Feature, ArrayList<Feature>>();
        reqTFs = getReqTFs(inputOutputList, "input");
        reqTFs.addAll(getReqTFs(inputOutputList, "output"));
        tfGene = creatTFGeneTable (tfPromoter, allFR , origTFPromoter );
        tfGene = sortTFXTable(tfGene, reqTFs);
        reqTFs = getReqTFs(inputOutputList, "output");
        tfGene = fetchOrthogonalTable(tfGene, tfGene.size(), reqTFs);

        reqTFRelation = fillGeneTables (allFR, tfGene, tfPromoter, geneT, ogT, ipT, rpT, repGeneOrthT, getReqTFs(inputOutputList, "input"), getReqTFs(inputOutputList, "output") );

        creatRBSTable(rbsT, fetch("RBS"));
        creatTeminatorTable(tT, fetch("Terminator"));
        return reqTFRelation;
    }


        public static ArrayList<Feature> fetch(String Keywor) {
            featureList = Collector.getAll(ObjType.FEATURE);

            FeatureComp fc = new FeatureComp();
            Collections.sort((List)featureList, fc);

           ArrayList<Feature>  transFactors = new ArrayList<Feature>();
        Iterator featIter = featureList.iterator();
        while( featIter.hasNext() ) {
            Feature f = (Feature)featIter.next();

            HashSet fFam = f.getFamilyLinks();
            Iterator famIter = fFam.iterator();

            //while(famIter.hasNext()) {
            if (famIter.hasNext()) {
                String famName = Collector.get(ObjType.FAMILY, (String) famIter.next()).getName();
                if(famName.equals(Keywor))
                    transFactors.add(f);
            }
        }
        return transFactors;

    }


   public static ArrayList<FeatureRelation> fetchRelations (ArrayList<Feature> tfList)
    {
        ArrayList<FeatureRelation> allFR = Collector.getAll(ObjType.FEATURERELATION);
       ArrayList<FeatureRelation> result = new ArrayList<FeatureRelation>();
        Iterator frIter = allFR.iterator();
        int i=0;
        while( frIter.hasNext() ) {
            FeatureRelation f = (FeatureRelation) frIter.next();
            Feature f1 = (Feature) Collector.get(ObjType.FEATURE, f.getF1Uuid());
            Feature f2 = (Feature) Collector.get(ObjType.FEATURE, f.getF2Uuid());

            if (tfList.contains(f1)|tfList.contains(f2))
                result.add(f);
        }

        return result;
        
    }

    private static void creatGeneTable(ArrayList<FeatureRelation> allFR, CelloGeneTable geneT) {
         Iterator frIter = allFR.iterator();
        int i=0;
        while( frIter.hasNext() ) {
            FeatureRelation f = (FeatureRelation) frIter.next();
            if (f.getRelType().equals("Productive"))
            {               
                Feature f1 = (Feature) Collector.get(ObjType.FEATURE, f.getF1Uuid());
                CelloGene cg = new CelloGene();
                cg.setFeature(f1);
                if(!geneT.getTable().contains(cg))
                    geneT.add(cg);
            }
                
        }
    }
    
    
   private static void creatTFPromoterTables(ArrayList<FeatureRelation> allFR,  HashMap<Feature, ArrayList<Feature>> tfPromoter) {

        int numberOfPromoters = 0;
        Iterator frIter = allFR.iterator();
        while( frIter.hasNext() ) {
            FeatureRelation f = (FeatureRelation) frIter.next();
            if (f.getRelType().equals("Inducive"))
            {
                Feature f2 = (Feature) Collector.get(ObjType.FEATURE, f.getF2Uuid());
                Feature tf = (Feature) Collector.get(ObjType.FEATURE, f.getF1Uuid());

                if (!tfPromoter.containsKey(tf))
                    tfPromoter.put(tf, new ArrayList<Feature>());

                tfPromoter.get(tf).add(f2);

                numberOfPromoters++;

            }
            else if(f.getRelType().equals("Repressive"))
            {
                Feature f2 = (Feature) Collector.get(ObjType.FEATURE, f.getF2Uuid());
                Feature tf = (Feature) Collector.get(ObjType.FEATURE, f.getF1Uuid());

                if (!tfPromoter.containsKey(tf))
                    tfPromoter.put(tf, new ArrayList<Feature>());

                tfPromoter.get(tf).add(f2);

                numberOfPromoters++;
            }

        }
    }

    private static void creatOutGeneTable(ArrayList<FeatureRelation> allFR, CelloGeneTable geneT, CelloIPromoterTable ipT, CelloRPromoterTable rpT, CelloGeneTable ogT, CelloRGenePromoterTable repGeneOrthT) {
        Iterator<CelloGene> iGene = geneT.getTable().iterator();
        while(iGene.hasNext())
        {
            Feature fGene = iGene.next().getFeature();

            //find TF related
            Iterator frIter = allFR.iterator();
            int i=0;
            Feature fTF = new Feature(null) ;
            boolean flag = false;
            while( frIter.hasNext() ) {
                FeatureRelation f = (FeatureRelation) frIter.next();
                if (f.getRelType().equals("Productive"))
                {
                    Feature f1 = (Feature) Collector.get(ObjType.FEATURE, f.getF1Uuid());
                    Feature f2 = (Feature) Collector.get(ObjType.FEATURE, f.getF2Uuid());
                    if (f1.equals(fGene))
                    {
                        fTF = f2;
                        flag = true;
                        break;
                    }
                }
            }
            if (flag)
            {
                return;
            }

        }
    }

    private static HashMap<Feature, ArrayList<Feature>>  fetchOrthogonalTable(HashMap<Feature, ArrayList<Feature>> Matrix, int numOfOrtho, ArrayList<Feature> reqTFs)
    {
        Object[] keySet =  Matrix.keySet().toArray();
        if (keySet.length==0)
            return Matrix;
        Iterator<Feature> iSel = Matrix.get(keySet[0]).iterator();
        Feature currTF = (Feature) keySet[0];

        while (iSel.hasNext())
        {
            Feature currProm = iSel.next();
            
            HashMap<Feature, ArrayList<Feature>> subMatrix = creatSubMatrix(Matrix, currProm, currTF);

            if (numOfOrtho== 1)
            {
                numOfOrtho--;
                if (isRequiredTF(currTF, reqTFs))
                    reqTFs.remove(currTF);
                Matrix = subMatrix;
                Matrix.put(currTF, new ArrayList<Feature>());
                Matrix.get(currTF).add(currProm);
                return Matrix;
            }
            else{
                if (!subMatrix.isEmpty())
                {numOfOrtho--;
                 boolean flag = false;
                 if (isRequiredTF(currTF, reqTFs))
                 {
                     flag = true;
                    reqTFs.remove(currTF);
                 }
                 subMatrix = fetchOrthogonalTable(subMatrix, numOfOrtho, reqTFs);
                if (!subMatrix.isEmpty() | (!iSel.hasNext() & reqTFs.isEmpty()) ) //So if it doesn't have any other choice it's better to return a smaller list
                {
                    Matrix = subMatrix;
                    Matrix.put(currTF, new ArrayList<Feature>());
                    Matrix.get(currTF).add(currProm);
                    return Matrix;
                }else
                    numOfOrtho++;
                 if (flag)
                    reqTFs.add(currTF);
                }
            }

        }

            if (reqTFs.contains(currTF))
            {
                Matrix.clear();
                return Matrix ;
            }
        
        HashMap<Feature, ArrayList<Feature>> subMatrix = (HashMap<Feature, ArrayList<Feature>>) Matrix.clone();
        subMatrix.remove(currTF);
        if (subMatrix.isEmpty())
            return subMatrix;
        Matrix = fetchOrthogonalTable(subMatrix, numOfOrtho, reqTFs);
        //Matrix = subMatrix;
        //Matrix.put(currTF, new ArrayList<Feature>());
        return Matrix;
        
    }

    private static HashMap<Feature, ArrayList<Feature>> creatSubMatrix(HashMap<Feature, ArrayList<Feature>> Matrix, Feature currProm, Feature currTF) {
        HashMap<Feature, ArrayList<Feature>> subMatrix = new HashMap<Feature, ArrayList<Feature>>();

        subMatrix = (HashMap<Feature, ArrayList<Feature>>) Matrix.clone();
        subMatrix.remove(currTF); //Remove first column
        ArrayList<Feature> nonOrthoPromo = Matrix.get(currTF);
        Iterator<Feature> iterTF = subMatrix.keySet().iterator();

        //Remove nonOrthogonal promoters
        while(iterTF.hasNext())
        {
            Feature otherTF = iterTF.next();
            if (subMatrix.get(otherTF).contains(currProm)){ //if this TF has relation with the selected promoter
                subMatrix.remove(otherTF);                  //then remove the whole column
                iterTF = subMatrix.keySet().iterator();
            }
            else{
                Iterator<Feature> iterProm = nonOrthoPromo.iterator();
                while (iterProm.hasNext())
                {
                    Feature otherProm = iterProm.next();
                    if (subMatrix.get(otherTF).contains(otherProm)) //if this TF has relation with nonorthogonal promoter
                        subMatrix.get(otherTF).remove(otherProm);   //then remove that promoter to not be selected
                }
            }
        }

        return subMatrix;
    }

    private static LinkedHashMap<Feature, ArrayList<Feature>>  sortTFXTable(HashMap<Feature, ArrayList<Feature>> tfPromoter, ArrayList<Feature> reqTFs) {

        //we need to put the required TF's first and then sort based on the number of the promoters

        LinkedHashMap<Feature, ArrayList<Feature>> sortedMap = new LinkedHashMap<Feature, ArrayList<Feature>>();
        Set<Feature> keyList = tfPromoter.keySet();
        ArrayList<Feature> sortedList = new ArrayList<Feature>();

        Iterator<Feature> iter = keyList.iterator();
        while(iter.hasNext())
        {
            Feature TF1 = iter.next();
            int insIndex = sortedList.size();
            for (int j=sortedList.size()-1; j>=0 ; j--)
            {
                Feature TF2 = sortedList.get(j);
                if (isRequiredTF(TF2, reqTFs) &! isRequiredTF(TF1, reqTFs))
                    insIndex = insIndex;
                else if(!isRequiredTF(TF2, reqTFs) & isRequiredTF(TF1, reqTFs))
                    insIndex= j;
                else
                {
                    if (tfPromoter.get(TF2).size() > tfPromoter.get(TF1).size())
                        insIndex = j;
                }
            }
            sortedList.add(insIndex, TF1);
        }

        for(Feature f: sortedList)
        {
            sortedMap.put(f, tfPromoter.get(f));
        }
        return sortedMap;

    }
    private static boolean isRequiredTF(Feature currTF, ArrayList<Feature> reqTFs)
    {
       if (reqTFs.contains(currTF))
           return true;
        return false;
    }

    private static ArrayList<Feature> getReqTFs(HashMap<String, HashMap<String, Feature>> inputOutputList, String Keyword) {

      HashMap<String, Feature> TFs = (HashMap<String, Feature>) inputOutputList.get(Keyword).clone();
      //TFs.putAll((HashMap<String, Feature>) inputOutputList.get("output").clone());
      ArrayList<Feature> result = new ArrayList<Feature>();
      for (String port:TFs.keySet())
      {
          result.add(TFs.get(port));
      }
      return result;
    }

    private static HashMap<Feature, ArrayList<Feature>> creatTFGeneTable(HashMap<Feature, ArrayList<Feature>> tfPromoter, ArrayList<FeatureRelation> allFR, HashMap<Feature, ArrayList<Feature>> origTFPromoter) {

        HashMap<Feature, ArrayList<Feature>> tfGene = new HashMap<Feature, ArrayList<Feature>>();

        Set<Feature> orthoTF = tfPromoter.keySet();
        Set<Feature> origTF = origTFPromoter.keySet();

        Iterator frIter = allFR.iterator();
        while( frIter.hasNext() ) {
            FeatureRelation f = (FeatureRelation) frIter.next();
            if (f.getRelType().equals("Productive"))
            {
                Feature tf = (Feature) Collector.get(ObjType.FEATURE, f.getF2Uuid());

                Feature gene = (Feature) Collector.get(ObjType.FEATURE, f.getF1Uuid());

                if (orthoTF.contains(tf)| (!orthoTF.contains(tf) & !origTF.contains(tf)))
                {
                    if (!tfGene.containsKey(tf))
                        tfGene.put(tf, new ArrayList<Feature>());

                    tfGene.get(tf).add(gene);
                }
            }

        }

        return tfGene;
    }

   private static HashMap<Feature, CelloPrimitive>  fillGeneTables (  ArrayList<FeatureRelation> allFR , HashMap<Feature, ArrayList<Feature>> tfGene,
           HashMap<Feature, ArrayList<Feature>> tfPromoter, CelloGeneTable geneT, CelloGeneTable ogT, CelloIPromoterTable ipT, CelloRPromoterTable rpT,
           CelloRGenePromoterTable repGeneOrthT, ArrayList<Feature> reqITFs, ArrayList<Feature> reqOTFs) {

       ipT.getTable().clear();
       rpT.getTable().clear();
        HashMap<Feature, Feature> reqGeneTFRel = new HashMap<Feature, Feature>();
        HashMap<Feature, CelloPrimitive> reqTFRelation = new HashMap<Feature, CelloPrimitive>();

        int indexI = 0;
        int indexR = 0;
        int indexIp = 0;
        int indexRp = 0;

        for(Feature tf: tfPromoter.keySet())
            if(tfPromoter.get(tf).size()==1){
                if (reqITFs.contains(tf))
                {
                    Feature promoter = tfPromoter.get(tf).get(0);
                    String relType = getRelType (allFR, promoter, tf );
                    if (relType!= null){
                        if (relType.equals("Inducive"))
                        {
                            CelloIPromoter ip = new CelloIPromoter();
                            ip.setFeature(promoter);
                            ip.setId(indexIp++);
                            reqTFRelation.put(tf, ip);
                        }else
                            if (relType.equals("Repressive"))
                            {
                                CelloRPromoter rp = new CelloRPromoter();
                                rp.setFeature(promoter);
                                rp.setId(indexRp++);
                                reqTFRelation.put(tf, rp);

                            }
                    }
                }
                else
                {
                    if (!reqOTFs.contains(tf)){
                        Feature promoter = tfPromoter.get(tf).get(0);
                        String relType = getRelType (allFR, promoter, tf );
                        if (relType!= null){
                            if (relType.equals("Inducive"))
                            {
                                CelloIPromoter ip = new CelloIPromoter();
                                ip.setFeature(promoter);
                                ip.setId(indexI++);
                                ipT.add(ip);
                            }else
                                if (relType.equals("Repressive"))
                                {
                                    CelloRPromoter rp = new CelloRPromoter();
                                    rp.setFeature(promoter);
                                    rp.setId(indexR++);
                                    rpT.add(rp);
                                }
                        }
                    }
                }
            }


        
        ArrayList<Feature> allOrthoGenes = new ArrayList<Feature>();
        for(Feature tf: tfGene.keySet())
            if(tfGene.get(tf).size()==1){
                allOrthoGenes.add(tfGene.get(tf).get(0));
                if (reqOTFs.contains(tf))
                    reqGeneTFRel.put(tfGene.get(tf).get(0), tf);
            }
       

        int index = 0;
        int index2 = 0;
        int index3 = 0;
        for (Feature currTF : tfGene.keySet())
        {
            if (tfGene.get(currTF).size()==1)
            {
                if (reqOTFs.contains(currTF))
                {
                    CelloGene cg = new CelloGene();
                    cg.setFeature(tfGene.get(currTF).get(0));
                    cg.setId(index3++);
                    reqTFRelation.put(currTF, cg);
                    
                }else

                if (tfPromoter.containsKey(currTF))
                {
                    CelloGene cg = new CelloGene();
                    cg.setFeature(tfGene.get(currTF).get(0));
                    cg.setId(index++);
                    geneT.add(cg);
                   for(CelloRPromoter currRP : rpT.getTable())
                    {
                        if (currRP.getFeature().getUUID().equals(tfPromoter.get(currTF).get(0).getUUID()))
                        {
                            
                            repGeneOrthT.add(currRP, cg);
                            break;
                        }
                    }
                }
                else{
                        CelloGene cg = new CelloGene();
                        cg.setFeature(tfGene.get(currTF).get(0));
                        cg.setId(index2++);
                        ogT.add(cg);
                }
            }
        }

        return reqTFRelation;
    }

    private static void creatRBSTable(CelloRBSTable rbsT, ArrayList<Feature> fetch) {
        rbsT.getTable().clear();
        int index = 0;

        for(Feature F : fetch)
        {
            CelloRBS cr = new CelloRBS();
            cr.setFeature(F);
            cr.setId(index++);
            rbsT.add(cr);

        }
    }

    private static void creatTeminatorTable(CelloTerminatorTable tT, ArrayList<Feature> fetch) {
        tT.getTable().clear();
        int index = 0;

        for(Feature F : fetch)
        {
            CelloTerminator ct = new CelloTerminator();
            ct.setFeature(F);
            ct.setId(index++);
            tT.add(ct);
            
        }
    }

    private static String getRelType(ArrayList<FeatureRelation> allFR, Feature promoter, Feature tf) {

        Iterator frIter = allFR.iterator();
        while( frIter.hasNext() ) {
           FeatureRelation f = (FeatureRelation) frIter.next();
           if (f.getF2Uuid().equals(promoter.getUUID()) & f.getF1Uuid().equals(tf.getUUID()))
            return f.getRelType();
        }
        return null;
    }

    static Part getPart(CelloPrimitive aThis) {
        ClothoConnection c = Collector.getDefaultConnection();
                ClothoQuery mainQuery = c.createQuery( ObjType.PART );
                mainQuery.matches( Part.Fields.SEQUENCE, aThis._feature.getSeq().getUUID(), false );
                List<ObjBase> qResults = mainQuery.getResults();
                if (qResults.size()>0)
                return (Part) qResults.get(0);
                else return null;
    }

     public static class FeatureComp implements Comparator {
        @Override
        public int compare(Object f1, Object f2) {
            Feature F1 = (Feature) f1;
            Feature F2 = (Feature) f2;

            if (F1.getName().compareToIgnoreCase(F2.getName()) > 0) {
                return 1;
            } else if (F1.getName().compareToIgnoreCase(F2.getName()) < 0) {
                return -1;
            } else {
                return 0;
            }

        }
    }

    public static ArrayList<Feature> featureList;
    public static ArrayList<Family> familyList;
    
}
