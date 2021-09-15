package com.company;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Check {
   // public Check(){}
    public static long sum = 0;
    private Map Mapss = null;
    public static int min = 1;//最小匹配规则
    public static int max = 2;//最大匹配规则
    private static final String ENCODING = "utf-8";

    private static String replaceString = null;

    public static String readToString(String fileName) throws IOException {
        //String encoding = "UTF-8";
        File filetxt = new File(fileName);
        Long filelength = filetxt.length();
        byte[] filecontent = new byte[filelength.intValue()];
        //System.out.println();
        FileInputStream in = new FileInputStream(filetxt);
        try {
            in.read(filecontent);
            return new String(filecontent, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            in.close();
        }
    }//实现一次性读入待检测文本
     public static void main(String[] args) throws IOException {
         Check Sensitive = new Check();
       //  System.out.println("敏感词的数量：" + Sensitive.Mapss.size());
         //读入待检测文本
        String txt = readToString("C:\\Users\\陈玉娜\\IdeaProjects\\Filter\\org.txt");
       //System.out.print(txt);
         Set<String> word = Sensitive.getWord(txt,1);//获取文本中的敏感词
        // System.out.println("Total:"+sum);
         File fileout = new File("C:\\Users\\陈玉娜\\IdeaProjects\\Filter\\ans.txt");//修改后缀可生成相应类型文件
         try{
             if(!fileout.exists()) fileout.createNewFile();//文件不存在，创建文件
             BufferedWriter bw = new BufferedWriter(new FileWriter(fileout));
             String str = "Total:"+sum;
             bw.write(str);
             bw.close();
         }catch(IOException e){
             e.printStackTrace();
     } 
    }
     //构造函数，初始化敏感词词库
    public Check(){
        Mapss = new Filter().lexicon();
    }

    //▲实现输出敏感词+所在行数
    //获取文本中的敏感词，最小匹配规则为1，最大匹配规则为2
    public Set<String> getWord(String txt,int type){
        Set<String> wordlist = new HashSet<String>();
        for (int i=0;i<txt.length();i++){
            //判断是否包含敏感词
            int len = search(txt,i,type);////len都是0？？？
            if(len>0){
                //如果存在，加入wordlist
                wordlist.add(txt.substring(i,i+len));
                i=i+len-1;
                //sum++;
                //System.out.print("Total:"+sum);
            }
        }
        return wordlist;
    }

    //检查文本中是否包含敏感词
    public int search(String txt,int start,int type){
        boolean leaf = false;//敏感词结束标识位：用于敏感词只有1位的情况
        int flag = 0;//匹配标识数默认为0
        char sensitiveword = 0;
        Map New =Mapss;
        for(int i=start;i<txt.length();i++){
            sensitiveword = txt.charAt(i);
            New = (Map)New.get(sensitiveword);//获取指定敏感词
            if(New!=null){
                flag++;
                if("1".equals(New.get("isEnd"))){
                    leaf = true;
                    sum++;
                    if(Check.min==type) break;
                }
            }
            else break;
        }
        if(flag<2||!leaf) flag=0;
        return flag;
    }

}
