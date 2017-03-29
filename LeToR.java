import java.io.*;
import java.util.*;

/**
 * Created by sharath on 26/3/17.
 */
public class LeToR
{
    /**
     * letor:trainingQueryFile= A file of training queries.
     letor:trainingQrelsFile= A file of relevance judgments. Column 1 is the query id. Column 2 is ignored. Column 3 is the document id. Column 4 indicates the degree of relevance (0-2).
     letor:trainingFeatureVectorsFile= The file of feature vectors that your software will write for the training queries.
     letor:pageRankFile= A file of PageRank scores.
     letor:featureDisable= A comma-separated list of features to disable for this experiment. For example, "letor:featureDisable=6,9,12,15" disables all Indri features. If this parameter is missing, use all features.
     letor:svmRankLearnPath= A path to the svm_rank_learn executable.
     letor:svmRankClassifyPath= A path to the svm_rank_classify executable.
     letor:svmRankParamC= The value of the c parameter for SVMrank. 0.001 is a good default.
     letor:svmRankModelFile= The file where svm_rank_learn will write the learned model.
     letor:testingFeatureVectorsFile= The file of feature vectors that your software will write for the testing queries.
     letor:testingDocumentScores= The file of document scores that svm_rank_classify will write for the testing feature vectors.
     */

    private String trainingQueryFile;
    private String trainingQrelsFile;
    private String trainingFeatureVectorsFile;
    private String pageRankFile;
    private String featureDisable = null;
    private String svmRankLearnPath;
    private String svmRankClassifyPath;
    private double svmRankParamC;
    private String svmRankModelFile;
    private String testingFeatureVectorsFile;
    private String testingDocumentScores;
    private boolean leTorPresent=false;
    private HashSet<Integer> disabledFeatures;
    private static double INVALID = -1;


    public boolean isLeTorPresent() {
        return leTorPresent;
    }

    public void setLeTorPresent(boolean leTorPresent) {
        this.leTorPresent = leTorPresent;
    }

    public String getTrainingQueryFile() {
        return trainingQueryFile;
    }

    public void setTrainingQueryFile(String trainingQueryFile) {
        this.trainingQueryFile = trainingQueryFile;
    }

    public String getTrainingQrelsFile() {
        return trainingQrelsFile;
    }

    public void setTrainingQrelsFile(String trainingQrelsFile) {
        this.trainingQrelsFile = trainingQrelsFile;
    }

    public String getTrainingFeatureVectorsFile() {
        return trainingFeatureVectorsFile;
    }

    public void setTrainingFeatureVectorsFile(String trainingFeatureVectorsFile) {
        this.trainingFeatureVectorsFile = trainingFeatureVectorsFile;
    }

    public String getPageRankFile() {
        return pageRankFile;
    }

    public void setPageRankFile(String pageRankFile) {
        this.pageRankFile = pageRankFile;
    }

    public String getFeatureDisable() {
        return featureDisable;
    }

    public void setFeatureDisable(String featureDisable) {

        this.featureDisable = featureDisable;

        String[] splits = featureDisable.split(",");
        disabledFeatures = new HashSet<>();

        for(String split : splits){
            disabledFeatures.add(Integer.parseInt(split));
        }
    }

    public String getSvmRankLearnPath() {
        return svmRankLearnPath;
    }

    public void setSvmRankLearnPath(String svmRankLearnPath) {
        this.svmRankLearnPath = svmRankLearnPath;
    }

    public String getSvmRankClassifyPath() {
        return svmRankClassifyPath;
    }

    public void setSvmRankClassifyPath(String svmRankClassifyPath) {
        this.svmRankClassifyPath = svmRankClassifyPath;
    }

    public double getSvmRankParamC() {
        return svmRankParamC;
    }

    public void setSvmRankParamC(double svmRankParamC) {
        this.svmRankParamC = svmRankParamC;
    }

    public String getSvmRankModelFile() {
        return svmRankModelFile;
    }

    public void setSvmRankModelFile(String svmRankModelFile) {
        this.svmRankModelFile = svmRankModelFile;
    }

    public String getTestingFeatureVectorsFile() {
        return testingFeatureVectorsFile;
    }

    public void setTestingFeatureVectorsFile(String testingFeatureVectorsFile) {
        this.testingFeatureVectorsFile = testingFeatureVectorsFile;
    }

    public String getTestingDocumentScores() {
        return testingDocumentScores;
    }

    public void setTestingDocumentScores(String testingDocumentScores) {
        this.testingDocumentScores = testingDocumentScores;
    }

    private static LeToR instance = null;

    // Singleton
    public static LeToR getInstance()
    {
        if(instance==null)
        {
            instance = new LeToR();
            instance.setLeTorPresent(true);
        }

        return instance;
    }


    /**
     * Read training queries and relevance judgments from input files;
     Calculate feature vectors for training documents;
     Write the feature vectors to a file;
     Call SVMrank to train a retrieval model;
     Read test queries from an input file;
     Use BM25 to get inital rankings for test queries;
     Calculate feature vectors for the top 100 ranked documents (for each query);
     Write the feature vectors to a file;
     Call SVMrank to calculate scores for test documents;
     Read the scores produced by SVMrank; and
     Write the final ranking in trec_eval input format.
     */

    /**
     * f1: Spam score for d (read from index).
     Hint: The spam score is stored in your index as the score attribute. (We know that this is a terrible name. Sorry.)
     int spamScore = Integer.parseInt (Idx.getAttribute ("score", docid));
     f2: Url depth for d(number of '/' in the rawUrl field).
     Hint: The raw URL is stored in your index as the rawUrl attribute.
     String rawUrl = Idx.getAttribute ("rawUrl", docid);
     f3: FromWikipedia score for d (1 if the rawUrl contains "wikipedia.org", otherwise 0).
     f4: PageRank score for d (read from file).
     f5: BM25 score for <q, dbody>.
     f6: Indri score for <q, dbody>.
     f7: Term overlap score for <q, dbody>.
     Hint: Term overlap is defined as the percentage of query terms that match the document field.
     f8: BM25 score for <q, dtitle>.
     f9: Indri score for <q, dtitle>.
     f10: Term overlap score for <q, dtitle>.
     f11: BM25 score for <q, durl>.
     f12: Indri score for <q, durl>.
     f13: Term overlap score for <q, durl>.
     f14: BM25 score for <q, dinlink>.
     f15: Indri score for <q, dinlink>.
     f16: Term overlap score for <q, dinlink>.
     f17: A custom feature - use your imagination.
     f18: A custom feature - use your imagination.
     * @throws Exception
     */

    public void train() throws Exception
    {
        HashMap<String,ArrayList<RelevancePair>> relevanceJudgments = parseRelevanceJudgments();
        HashMap<String,Double> pageRankScoreMap = parsePageRankFile();

        FileInputStream fileInputStream = new FileInputStream(trainingQueryFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        String line;

        //file writing.
        FileOutputStream fileOutputStream = new FileOutputStream(trainingFeatureVectorsFile);
        BufferedWriter featureVectorWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

        if(disabledFeatures==null){
            disabledFeatures = new HashSet<>();
        }

        while((line = bufferedReader.readLine()) != null   )
        {
            String[] queryElements = QryParser.tokenizeString(line);
            String qid = queryElements[0];
            ArrayList<RelevancePair> judgements = relevanceJudgments.get(qid);

            //for each query
            HashMap<Integer,Double> maxMap = new HashMap<>();
            HashMap<Integer,Double> minMap = new HashMap<>();
            HashMap<Integer,double[]> featureVectorsMap = new HashMap<>(); // internal doc id vs features
            System.out.println("QID:"+qid);
            for(RelevancePair relevancePair : judgements)
            {
                double[] featureVector = new double[19];
                Arrays.fill(featureVector,0);

                //for each document
                String externalDocId = relevancePair.docId;
                System.out.println(externalDocId);
                if(externalDocId.equals("clueweb09-en0007-26-33126")){
                    System.out.println("2");
                }
                int internalDocId = -1;
                try{
                 internalDocId = Idx.getInternalDocid(externalDocId);
                }
                catch(Exception e )
                {
                    continue;
                }
                String rawUrl = Idx.getAttribute("rawUrl",internalDocId);

                for (int vectorNum = 0; vectorNum < 18; vectorNum++)
                {
                    double ans = 0;

                    switch (vectorNum + 1)
                    {
                        case 1:
                            ans  = Double.parseDouble(Idx.getAttribute("score",internalDocId));
                            break;

                        case 2:

                            int count=0;
                            for(char c : rawUrl.toCharArray())
                            {
                                if(c=='/')
                                    count++;
                            }
                            ans=count;
                            break;

                        case 3:
                            if(rawUrl.contains("wikipedia.org"))
                            {
                                ans=1;
                            }
                            else
                            {
                                ans=0;
                            }
                            break;

                        case 4:
                            if(pageRankScoreMap.containsKey(externalDocId))
                            {
                                ans = pageRankScoreMap.get(externalDocId);
                            }
                            break;

                        case 5:
                            ans = RetrievalModelBM25.getLeTorScore(internalDocId,"body",queryElements);
                            break;
                        case 6:
                            ans = RetrievalModelIndri.getLeToRScore(internalDocId,"body",queryElements);
                            break;
                        case 7:
                            ans = getTermOverlapScore(internalDocId,"body",queryElements);
                            break;

                        case 8:
                            ans = RetrievalModelBM25.getLeTorScore(internalDocId,"title",queryElements);
                            break;
                        case 9:
                            ans = RetrievalModelIndri.getLeToRScore(internalDocId,"title",queryElements);
                            break;
                        case 10:
                            ans = getTermOverlapScore(internalDocId,"title",queryElements);
                            break;

                        case 11:
                            ans = RetrievalModelBM25.getLeTorScore(internalDocId,"url",queryElements);
                            break;
                        case 12:
                            ans = RetrievalModelIndri.getLeToRScore(internalDocId,"url",queryElements);
                            break;
                        case 13:
                            ans = getTermOverlapScore(internalDocId,"url",queryElements);
                            break;


                        case 14:
                            ans = RetrievalModelBM25.getLeTorScore(internalDocId,"inlink",queryElements);
                            break;
                        case 15:
                            ans = RetrievalModelIndri.getLeToRScore(internalDocId,"inlink",queryElements);
                            break;
                        case 16:
                            ans = getTermOverlapScore(internalDocId,"inlink",queryElements);
                            break;

                        case 17:
                            ans = getMeanOfMeanTFIDFScore(internalDocId,queryElements);
                            break;

                        case 18:
                            ans =  getMeanTfIdfScore(internalDocId,queryElements);
                            break;


                    }


                    if(disabledFeatures.contains(vectorNum+1)==false)
                    {
                        featureVector[vectorNum] = ans;


                        if(maxMap.containsKey(vectorNum+1))
                        {
                            double max= maxMap.get(vectorNum+1);
                            double min= minMap.get(vectorNum+1);

                            if(min>ans){
                                minMap.put(vectorNum+1,ans);
                            }
                            if(max<ans){
                                maxMap.put(vectorNum+1,ans);
                            }
                        }
                        else
                        {

                            maxMap.put(vectorNum+1,ans);
                            minMap.put(vectorNum+1,ans);
                        }
                    }

                }
                featureVector[18] = relevancePair.degree;


                featureVectorsMap.put(internalDocId,featureVector);
            }

            //Normalize
            normalize(featureVectorsMap,minMap,maxMap);
            writeToFeatureVectorFile(featureVectorsMap,qid,featureVectorWriter);

            bufferedReader.close();
            featureVectorWriter.close();
            System.exit(-1);

        }


        bufferedReader.close();
        featureVectorWriter.close();


    }


    /**
     * Parses the relevance judgments for use in the svm
     * @return
     * @throws Exception
     */

    public HashMap<String,ArrayList<RelevancePair>> parseRelevanceJudgments() throws Exception
    {
        HashMap<String,ArrayList<RelevancePair>> resultMap = new HashMap<>();


        FileInputStream fileInputStream = new FileInputStream(trainingQrelsFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        String line;


        while (     (line = bufferedReader.readLine()) != null     )
        {
            String[] splits = line.split("\\s+");

            String queryId = splits[0];
            String externalDocId = splits[2];
            String degree = splits[3];
            ArrayList<RelevancePair> list = null;
            if(resultMap.containsKey(queryId))
            {
                list = resultMap.get(queryId);

            }
            else
            {
                list= new ArrayList<>();
                resultMap.put(queryId,list);
            }

            list.add(new RelevancePair(externalDocId,Integer.parseInt(degree)));


        }


        bufferedReader.close();

        return resultMap;

    }


    /**
     *
     */
    private HashMap<String,Double> parsePageRankFile() throws Exception
    {
        HashMap<String,Double> resultMap = new HashMap<>();


        FileInputStream fileInputStream = new FileInputStream(pageRankFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        String line;

        while (     (line = bufferedReader.readLine()) != null     )
        {
            String[] splits = line.split("\\t");
            resultMap.put(splits[0],Double.parseDouble(splits[1]));
        }

        return resultMap;

    }

    private double getTermOverlapScore(int docid, String field, String[] queryElements) throws Exception
    {
        TermVector termVector = new TermVector(docid,field);

        //get potential Terms
        int totalTerms = termVector.stemsLength();
        if(totalTerms==0)
        {
            return 0.0;
        }


        double matchCount=0;
        for(int stemIndex=1;stemIndex<queryElements.length;stemIndex++)
        {
            if(termVector.indexOfStem(queryElements[stemIndex])!=-1)
            {
                matchCount++;
            }

        }

        return matchCount==0 ? 0.0 : matchCount/(double) (queryElements.length-1) ;

    }

    /**
     * Mean tf-idf  of all fields
     * @param docid
     * @param queryElements
     * @return
     */
    private double getMeanOfMeanTFIDFScore(int docid, String[] queryElements) throws Exception
    {

        double finalScore = 0.0;

        String[] fields = {"body","url","inlink","title"};

        for(int elementIndex=1;elementIndex<queryElements.length;elementIndex++)
        {


            double current  = 0.0;
            for(String field:fields)
            {
                TermVector termVector = new TermVector(docid, field);
                double N = Idx.getDocCount(field);
                if (termVector.stemsLength() == 0) {
                    continue;
                }
                int stemIndex = termVector.indexOfStem(queryElements[elementIndex]);
                if (stemIndex == -1) {
                    continue;
                }
                double tf = termVector.stemFreq(stemIndex);
                double idf = 0;
                double rsjWeight = Math.log((N - termVector.stemDf(stemIndex) + 0.5) / (termVector.stemDf(stemIndex) + 0.5));
                idf = Math.max(0,rsjWeight);
                current += tf*idf;
            }

            finalScore += (current/4.0);

        }

        return finalScore/(double)(queryElements.length-1);

    }

    /**
     * Mean of Tf-IDf across body field in the document.
     * @param docid
     * @return
     */

    private double getMeanTfIdfScore(int docid,String[] queryElements) throws Exception
    {


        double finalScore = 0.0;
        double N = Idx.getDocCount("body");

        for(int elementIndex=1;elementIndex<queryElements.length;elementIndex++)
        {


            double current  = 0.0;
            TermVector termVector = new TermVector(docid, "body");

            if (termVector.stemsLength() == 0) {
                continue;
            }
            int stemIndex = termVector.indexOfStem(queryElements[elementIndex]);
            if (stemIndex == -1)
            {
                continue;
            }

            double tf = termVector.stemFreq(stemIndex);
            double idf = 0;
            double rsjWeight = Math.log((N - termVector.stemDf(stemIndex) + 0.5) / (termVector.stemDf(stemIndex) + 0.5));
            idf = Math.max(0,rsjWeight);
            finalScore += tf*idf;

        }

        return finalScore/(double)(queryElements.length-1);



    }


    private void normalize (HashMap<Integer,double[]> featureVectorsMap, HashMap<Integer,Double> minMap, HashMap<Integer,Double> maxMap)
    {
        for(int docId : featureVectorsMap.keySet())
        {
            double[] featureVector = featureVectorsMap.get(docId);

            for(int index=0;index<18;index++)
            {
                if(maxMap.containsKey(index+1))
                {
                    double max = maxMap.get(index+1);
                    double min = minMap.get(index+1);
                    if(max==min)
                    {
                        featureVector[index]=0;
                    }
                    else
                    {
                        featureVector[index] = (featureVector[index]-min)/(max-min);
                    }
                }
            }

        }

    }

    public void writeToFeatureVectorFile(HashMap<Integer,double[]> featureVectorsMap, String qid,BufferedWriter featureVectorWriter) throws Exception
    {
        String secondCol = "qid:"+qid+" ";

        for(int docid : featureVectorsMap.keySet())
        {
            double[] featureVector = featureVectorsMap.get(docid);
            StringBuilder result = new StringBuilder();

            result.append(Double.toString(featureVector[18])+" ");
            result.append(secondCol);
            for(int i=0;i<18;i++)
            {
                if(!disabledFeatures.contains(i+1))
                {
                    result.append(Integer.toString(i+1)+":"+Double.toString(featureVector[i])+" ");
                }

            }

            result.append("# ");

            result.append(Idx.getExternalDocid(docid));


            featureVectorWriter.write(result.toString());
            featureVectorWriter.newLine();

        }


    }



}
