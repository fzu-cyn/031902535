package com.company;

import java.io.*;
import java.util.*;

public class Filter {

    private static final String ENCODING = "UTF-8";//将格式定为UTF-8
    public HashMap Mapss;
    public String pathname ;

    public Filter(String pathname) {
        this.pathname=pathname;
    }//构造函数

    private Set<String> Words(String pathnamess) throws Exception{
        Set<String> word = null;
        //读取文件
        File filein = new File(pathnamess);
        //创建输入流,此时读取的是文件的字节流，in.read()读取的是字节
        InputStream in = new FileInputStream(filein);//InputStreamReader 做的操作是将字节流转换成字符流
        InputStreamReader read = new InputStreamReader(in,ENCODING);//创建一个read的字符留缓冲区，将字符装载入缓冲区中
        try{
            //判断文件流是否存在
            if(filein.isFile()&& filein.exists()){
                word = new HashSet<String>();
                BufferedReader rd = new BufferedReader(read);//BufferedReader流读取文本
                String sensitive = null;
                //读取文件，将文件内容放到word中
                while((sensitive=rd.readLine())!=null){
                    word.add(sensitive);
                }
            }
            //抛出异常
            else{
                throw new Exception("File not found！");
            }
        }
        catch(IOException e){
            throw e;
        }
        finally{
            read.close();//关闭文件流
        }
        return word;
    }

    //初始化词库，将敏感词加入到HashMap中
    public Map lexicon(String pathnames){
        try{
            Set<String> SensitiveWords = Words(pathnames);//读取敏感词词库
            AddToLexicon(SensitiveWords);//将敏感词词库加入到HashMap中
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mapss;
    }

    private void AddToLexicon(Set<String> SensitiveWords){
        Mapss = new HashMap(SensitiveWords.size());//初始化敏感词容器，减少扩容操作
        String keys = null;
        Map New = null;
        Map<String,String> NewMap = null;//迭代
        Iterator<String> iterator = SensitiveWords.iterator();
        while(iterator.hasNext()){
            keys = iterator.next();
            New = Mapss;
            for(int i=0;i<keys.length();i++){
                char keyschar = keys.charAt(i);//将词汇转换为char型
                Object WordMap = New.get(keyschar);//获取词汇
                if(WordMap!=null){
                    //如果存在，就赋值
                    New = (Map)WordMap;
                }
                else{
                    //如果不存在，则构建另一个map
                    //不是最后一个，将isEnd设为0
                    NewMap = new HashMap<String,String>();
                    NewMap.put("isEnd","0");
                    New.put(keyschar,NewMap);
                    New = NewMap;
                }
                if(i==keys.length()-1) New.put("isEnd","1");//如果是最后一个，将isEnd设为1
            }
        }
    }
}
